package edwin.androidmovie;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 13-4-2015.
 */
public class SearchResultsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            (new AsyncListViewLoader()).execute("http://yts.to/api/v2/list_movies.json?sort_by=rating&query_term=" + query);
        }
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Movie>> {
        //private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.setMessage("Searching movies...");
            //dialog.show();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> result = new ArrayList<Movie>();
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
                JSONArray jArray = jObjectData.getJSONArray("movies");

                for (int i=0; i < jArray.length(); i++) {
                    result.add(Movie.CreateMovie(jArray.getJSONObject(i)));
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