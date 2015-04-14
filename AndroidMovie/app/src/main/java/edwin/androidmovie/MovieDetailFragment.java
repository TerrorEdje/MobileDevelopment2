package edwin.androidmovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "movie";
    private View view;
    private Movie movie;
    private AsyncMovieLoader asyncMovieLoader;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            movie = (Movie) getArguments().get(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        view = rootView;

        // Show the dummy content as text in a TextView.
        if (movie != null) {
            movie.ToView(view);
            asyncMovieLoader = new AsyncMovieLoader();
            asyncMovieLoader.execute("https://yts.to/api/v2/movie_details.json?movie_id=" + movie.getId() + "&with_images=true&with_cast=true");
        }

        Button imdbbutton = (Button) view.findViewById(R.id.imdb);
        imdbbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + movie.getImdbCode()));
                startActivity(browserIntent);
            }
        });

        Button youtubebutton = (Button) view.findViewById(R.id.youtube);
        youtubebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movie.getYoutubeCode()));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }

    public void onPause()
    {
        super.onPause();
        if (asyncMovieLoader != null) {
            asyncMovieLoader.cancel(true);
        }
    }

    private class AsyncMovieLoader extends AsyncTask<String, Void, Movie> {
        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            movie = result;
            TextView loading = (TextView) view.findViewById(R.id.loading);
            loading.setText("");
            result.ToView(getView().findViewById(R.id.movie_detail));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextView loading = (TextView) view.findViewById(R.id.loading);
            loading.setText("Loading...");
        }

        @Override
        protected Movie doInBackground(String... params) {
            Movie result;
            int responseCode = -1;
            JSONObject jsonResponse = null;
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(params[0]);

            try {
                HttpResponse response = client.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                responseCode = statusLine.getStatusCode();

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null)
                    {
                        builder.append(line);
                    }
                    jsonResponse = new JSONObject(builder.toString());
                }
                JSONObject jObjectData = jsonResponse.getJSONObject("data");

                result = Movie.CreateMovie(jObjectData);

                try {
                    URL url = new URL(result.getImageUrl());

                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    result.setImage(image);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}
