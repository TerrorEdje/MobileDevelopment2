package edwin.androidmovie;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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
    private String youtubeCode;
    private String director;
    private String imageUrl;
    private transient Bitmap image;
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
            if (json.has("yt_trailer_code")) { movie.setYoutubeCode(json.getString("yt_trailer_code")); }
            if (json.has("description_intro")) { movie.setDescriptionIntro(json.getString("description_intro")); }
            if (json.has("description_full")) { movie.setDescriptionFull(json.getString("description_full")); }
            if (json.has("description_full")) { movie.setDescriptionFull(json.getString("description_full")); }
            if (json.has("images")) {
                JSONObject jsonImages = json.getJSONObject("images");
                if (jsonImages.has("medium_cover_image")) {
                    movie.setImageUrl(jsonImages.getString("medium_cover_image"));
                }
            }
            if (json.has("genres")) {
                JSONArray jsonGenres = json.getJSONArray("genres");
                String[] genres = new String[jsonGenres.length()];
                for (int i = 0; i < jsonGenres.length(); i++) {
                    genres[i] = jsonGenres.getString(i);
                }
                movie.setGenres(genres);
            }
            if (json.has("actors"))
            {
                JSONArray jsonActors = json.getJSONArray("actors");
                Actor[] actors = new Actor[jsonActors.length()];
                for (int i = 0; i < jsonActors.length(); i++) {
                    JSONObject jsonActor = jsonActors.getJSONObject(i);
                    actors[i] = new Actor(jsonActor.getString("name"),jsonActor.getString("character_name"));
                }
                movie.setActors(actors);
            }
            if (json.has("directors"))
            {
                JSONArray jsonDirectors = json.getJSONArray("directors");
                JSONObject jsonDirector = jsonDirectors.getJSONObject(0);
                movie.setDirector(jsonDirector.getString("name"));
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

        TextView director = (TextView) view.findViewById(R.id.director);
        if (director != null && getDirector() != null)
            director.setText("Director: " + String.valueOf(getDirector()));

        TextView descriptionFull = (TextView) view.findViewById(R.id.descriptionFull);
        if (descriptionFull != null && getDescriptionFull() != null)
            descriptionFull.setText(String.valueOf(getDescriptionFull()));

        TextView language = (TextView) view.findViewById(R.id.language);
        if (language != null && getLanguage() != null)
            language.setText(String.valueOf(getLanguage()));

        ImageView image = (ImageView) view.findViewById(R.id.image);
        if (image != null && getImage() != null)
            image.setImageBitmap(getImage());

        TextView actorOne = (TextView) view.findViewById(R.id.actorOne);
        if (actorOne != null && getActors() != null)
            actorOne.setText(getActors()[0].getName() + " as " + getActors()[0].getCharacterName());

        TextView actorTwo = (TextView) view.findViewById(R.id.actorTwo);
        if (actorTwo != null && getActors() != null)
            actorTwo.setText(getActors()[1].getName() + " as " + getActors()[1].getCharacterName());

        TextView actorThree = (TextView) view.findViewById(R.id.actorThree);
        if (actorThree != null && getActors() != null)
            actorThree.setText(getActors()[2].getName() + " as " + getActors()[2].getCharacterName());
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
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

    public String getYoutubeCode() {
        return youtubeCode;
    }

    public void setYoutubeCode(String youtubeCode) {
        this.youtubeCode = youtubeCode;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Actor[] getActors() {
        return actors;
    }

    public void setActors(Actor[] actors) {
        this.actors = actors;
    }

    public static boolean saveMovie(SharedPreferences preferences, Movie movie)
    {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            movies = (ArrayList<Movie>) ObjectSerializer.deserialize(preferences.getString("MOVIES", ObjectSerializer.serialize(new ArrayList<Movie>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(Movie mov : movies)
        {
            if (mov.getId() == movie.getId())
            {
                return false;
            }
        }
        movies.add(movie);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString("MOVIES", ObjectSerializer.serialize(movies));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
        return true;
    }

    public static ArrayList<Movie> getMovies(SharedPreferences preferences)
    {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            movies = (ArrayList<Movie>) ObjectSerializer.deserialize(preferences.getString("MOVIES", ObjectSerializer.serialize(new ArrayList<Movie>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
