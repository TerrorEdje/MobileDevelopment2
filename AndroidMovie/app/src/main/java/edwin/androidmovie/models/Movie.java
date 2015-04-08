package edwin.androidmovie.models;

import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edwin.androidmovie.R;

/**
 * Created by Edwin on 8-4-2015.
 */
public class Movie implements Serializable{
    private String title;
    private int year;
    private int id;
    private double rating;
    private String language;
    private String imdbCode;
    private String[] genres;
    private String descriptionIntro;
    private String descriptionFull;
    private Actor[] actors;

    public static Movie CreateMovie(JSONObject json)
    {
        try {
            Movie movie = new Movie();
            if (json.has("title")) { movie.setTitle(json.getString("title")); }
            if (json.has("year")) { movie.setYear(json.getInt("year")); }
            if (json.has("id")) { movie.setId(json.getInt("id")); }
            if (json.has("rating")) { movie.setRating(json.getDouble("rating")); }
            if (json.has("language")) { movie.setLanguage(json.getString("language")); }
            if (json.has("imdb_code")) { movie.setImdbCode(json.getString("imdb_code")); }
            if (json.has("description_intro")) { movie.setDescriptionIntro(json.getString("description_intro")); }
            if (json.has("description_full")) { movie.setDescriptionFull(json.getString("description_full")); }
            if (json.has("genres")) {
                JSONArray jsongenres = json.getJSONArray("genres");
                String[] genres = new String[jsongenres.length()];
                for (int i = 0; i < jsongenres.length(); i++) {
                    genres[i] = jsongenres.getString(i);
                }
                movie.setGenres(genres);
            }
            if (json.has("actors"))
            {
                JSONArray jsonactors = json.getJSONArray("actors");
                Actor[] actors = new Actor[jsonactors.length()];
                for (int i = 0; i < jsonactors.length(); i++) {
                    JSONObject jsonactor = jsonactors.getJSONObject(i);
                    actors[i] = new Actor(jsonactor.getString("name"),jsonactor.getString("character_name"));
                }
                movie.setActors(actors);
            }
            return movie;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void ToView(View view)
    {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getTitle());

        TextView year = (TextView) view.findViewById(R.id.year);
        year.setText(Integer.toString(getYear()));

        String genrestotal = getGenres()[0];
        for (int i = 1; i < getGenres().length; i++) {
            genrestotal = getGenres()[i] + ", " + genrestotal;
        }
        TextView genres = (TextView) view.findViewById(R.id.genres);
        genres.setText(genrestotal);

        TextView rating = (TextView) view.findViewById(R.id.rating);
        rating.setText(String.valueOf(getRating()));

        TextView descriptionIntro = (TextView) view.findViewById(R.id.descriptionIntro);
        if (descriptionIntro != null && getDescriptionIntro() != null)
            descriptionIntro.setText(String.valueOf(getDescriptionIntro()));

        TextView descriptionFull = (TextView) view.findViewById(R.id.descriptionFull);
        if (descriptionFull != null && getDescriptionFull() != null)
            descriptionFull.setText(String.valueOf(getDescriptionFull()));

        TextView language = (TextView) view.findViewById(R.id.language);
        if (language != null && getLanguage() != null)
            language.setText(String.valueOf(getLanguage()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescriptionIntro() {
        return descriptionIntro;
    }

    public void setDescriptionIntro(String descriptionIntro) {
        this.descriptionIntro = descriptionIntro;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String descriptionFull) {
        this.descriptionFull = descriptionFull;
    }

    public Actor[] getActors() {
        return actors;
    }

    public void setActors(Actor[] actors) {
        this.actors = actors;
    }

}
