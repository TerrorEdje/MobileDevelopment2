package edwin.androidmovie.activitys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edwin.androidmovie.R;
import edwin.androidmovie.adapters.SimpleAdapter;
import edwin.androidmovie.models.Movie;


public class MovieList extends ListActivity {
    public SimpleAdapter adpt;
    //public EditText inputSearch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        adpt  = new SimpleAdapter(new ArrayList<Movie>(), this);

        ListView lView = (ListView) findViewById(android.R.id.list);
        lView.setAdapter(adpt);

        //inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Exec async load task
        (new AsyncListViewLoader()).execute("http://yts.to/api/v2/list_movies.json?sort_by=rating");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Movie selected = adpt.getItem(position);
        Intent intent = new Intent(this,MovieDetails.class);
        intent.putExtra("MOVIE",selected);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Movie>> {
        private final ProgressDialog dialog = new ProgressDialog(MovieList.this);

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            adpt.setItemList(result);
            adpt.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Searching movies...");
            dialog.show();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> result = new ArrayList<Movie>();

            try {
                URL u = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");

                conn.connect();
                InputStream is = conn.getInputStream();

                // Read the stream
                byte[] b = new byte[2048];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(b) != -1)
                    baos.write(b);

                JSONObject jObject = new JSONObject(baos.toString());
                JSONObject jObjectData = jObject.getJSONObject("data");
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
