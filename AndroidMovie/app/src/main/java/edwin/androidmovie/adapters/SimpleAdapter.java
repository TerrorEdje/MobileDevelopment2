package edwin.androidmovie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import edwin.androidmovie.R;
import edwin.androidmovie.models.Movie;

/**
 * Created by Edwin on 8-4-2015.
 */
public class SimpleAdapter extends ArrayAdapter<Movie> {

    private List<Movie> itemList;
    private Context context;

    public SimpleAdapter(List<Movie> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Movie getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Movie movie = itemList.get(position);
        movie.ToView(v);

        return v;

    }

    public List<Movie> getItemList() {
        return itemList;
    }

    public void setItemList(List<Movie> itemList) {
        this.itemList = itemList;
    }


}
