package edwin.androidmovie.activitys;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edwin.androidmovie.R;
import edwin.androidmovie.models.Movie;

public class MovieDetails extends Activity {
    public Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle extras = getIntent().getExtras();
        movie = (Movie) extras.get("MOVIE");

        movie.ToView(getWindow().getDecorView().findViewById(android.R.id.content));

        (new AsyncMovieLoader()).execute("https://yts.to/api/v2/movie_details.json?movie_id=" + movie.getId() + "&with_images=true&with_cast=true");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsyncMovieLoader extends AsyncTask<String, Void, Movie> {
        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            TextView loading = (TextView) MovieDetails.this.getWindow().getDecorView().findViewById(R.id.loading);
            loading.setText("");
            result.ToView(MovieDetails.this.getWindow().getDecorView().findViewById(android.R.id.content));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextView loading = (TextView) MovieDetails.this.getWindow().getDecorView().findViewById(R.id.loading);
            loading.setText("Loading...");
        }

        @Override
        protected Movie doInBackground(String... params) {
            Movie result = new Movie();

            try {
                URL u = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");

                conn.connect();
                InputStream is = conn.getInputStream();

                // Read the stream
                byte[] b = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(b) != -1)
                    baos.write(b);

                JSONObject jObject = new JSONObject(baos.toString());
                JSONObject jObjectData = jObject.getJSONObject("data");

                result = Movie.CreateMovie(jObjectData);

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}
