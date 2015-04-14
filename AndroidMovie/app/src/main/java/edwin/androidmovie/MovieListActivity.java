package edwin.androidmovie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieListFragment} and the item details
 * (if present) is a {@link MovieDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MovieListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MovieListActivity extends ActionBarActivity
        implements MovieListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("TEST", "TEST1111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        MovieListFragment movieListFragment = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            movieListFragment.setActivateOnItemClick(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("URL") != null) {
                movieListFragment.RunQuery(extras.getString("URL"));
            }
            else if (extras.getBoolean("LIKES")) {
                movieListFragment.ChangeList(Movie.getMovies(getSharedPreferences("MOVIES", Context.MODE_PRIVATE)));
            }
        }
        // TODO: If exposing deep links into your app, handle intents here.
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_list, menu);
        if (mTwoPane)
        {
            inflater.inflate(R.menu.menu_activity_detail,menu);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.likes_button) {
            Intent i = new Intent(getApplicationContext(), MovieListActivity.class);
            i.putExtra("LIKES",true);
            startActivity(i);
        }
        else if (id == R.id.like_button && movie != null) {
            Movie.saveMovie(getSharedPreferences("MOVIES", Context.MODE_PRIVATE),movie);
        }
        else if (id == R.id.home_button)
        {
            Intent i = new Intent(getApplicationContext(),MovieListActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link MovieListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            this.movie = movie;
            Bundle arguments = new Bundle();
            arguments.putSerializable(MovieDetailFragment.ARG_ITEM_ID,movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, movie);
            startActivity(detailIntent);
        }
    }
}
