package edwin.androidmovie;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Edwin on 13-4-2015.
 */
public class SearchResultsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
   }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent i = new Intent(getApplicationContext(), MovieListActivity.class);
            i.putExtra("URL","http://yts.to/api/v2/list_movies.json?sort_by=rating&query_term=" + query);
            startActivity(i);

        }
    }
}