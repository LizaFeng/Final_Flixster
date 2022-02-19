package com.example.flixster

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import org.json.JSONArray


//Purpose of this class: represent one movie object that we
//will display in our UI. We want to bundle up pieces of data (ie title, overview, and image url).
//Kotlin's way of bundling everything up is by making it a data class.
//We now want to declare different attribute on this data class.
//what attributes do we want to parse out?

@Parcelize
class Movie (
    //Declaring attributes we want
    val movieId: Int,
    val voteAverage: Double,
    //Since the person using the Movie class doesnt care about the posterPath since they
    //will just be using the posterImageUrl.
    private val posterPath: String,
    val title: String,
    val overview: String,
    ) : Parcelable {


    //Add a value for the poster image's url https://image.tmdb.org/t/p/w342/6bCplVkhowCjTHXWv49UjRPn0eK.jpg)
    //We will change the 6bCplVkhowCjTHXWv49UjRPn0eK.jpg to be what we parsed out from the poster path (? dont understand)
    @IgnoredOnParcel //See doc for notes
    val posterImageUrl="https://image.tmdb.org/t/p/w342/$posterPath"
    //Add a companion object (allows us to call methods on the movie class without having an instance
    companion object{
        //companion object ie: in MainActivity.kt, we call the function without creating instance
        //Objective: iterate through array and return a list of movie data classes
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie> {
            val movies=mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()){
                val movieJson=movieJsonArray.getJSONObject(i)
                //Now we want to add a movie, corresponding with movieJson that we just defined.
                movies.add(
                    //Now call the movie constructor that takes in the 4 parameters that we defined.
                    Movie(
                        //the names are keys that we get from the Json file (attributes we want)
                        movieJson.getInt("id"),
                        movieJson.getDouble("vote_average"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("title"),
                        movieJson.getString("overview")
                    )
                )
            }
            return movies
        }

    }


}