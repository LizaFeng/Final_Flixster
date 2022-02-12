package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG= "MainActivity"
private const val NOW_PLAYING_URL =
    "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
class MainActivity : AppCompatActivity() {
    //Define an instance variable that will initially be empty but once we get the API request back, we
    //will fill it. (data source variable)
    private val movies = mutableListOf<Movie>()
    //For step 5 of creating the RecyclerView
    private lateinit var rvMovies: RecyclerView
    //creating the recyclerview
    //1.Define a data model class as the data source - DONE:creation of the Movie.kt (will be datasource)
    //2. Add the RecyclerView to the layout-DONE
        //our layout is described in the R.layout.activity_main
        //go to activity_main.xml and drag the recyclerview out
        //applied the constraints to 0dp to fill the whole screen
    //3. Create a custom row layout XML file to visualize the item. -DONE
        //The recyclerView shows many different rows of data.
        //Now we want to create a row layout to customize how each row displays
        //Go to the file res->layout->create new resource file
    //4. Create an Adapter and ViewHolder to render the item -DONE
        //We will get a reference to the adapter in the main activity and have it call a constructor
        //We will define an adapter in another file.
    //5. Bind the adapter to the data source to populate the RecyclerView. - DONE
        //Get reference to the RecyclerView in the MainActivity
    //6. Bind a layout manager to the RecyclerView.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //For step 5 of creating a RecyclerView
        rvMovies=findViewById(R.id.rvMovies)
        //The reference ot the adapter. It calls the constructor and pass in context & data source
        //The "this" refers to the Main activity (which is an example of context)
        //The "movies" refers to the data source.
        val movieAdapter= MovieAdapter(this, movies)

        //Part of step 5 of creating RecyclerView: Bind movieAdapter to be the adapter for this recycler view
        rvMovies.adapter=movieAdapter
        //Step 6 of creating a RecyclerView
        rvMovies.layoutManager = LinearLayoutManager(this)

        //create an object/instance of asyncHttpClient
        val client = AsyncHttpClient()
        //making a get request on a particular url and passing in parameters and response handler
        //Since it is an anonymous param we have to do () followed by opening curly braces
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                //For now, simply log out the result of whatever happened in this callback function
                //Log.e=logging at error level because this is an onFailure callback (something has gone wrong)
                //don't type msg, that should show up by itself
                Log.e(TAG, "onFailure $statusCode")
            }

            //the ? means that the thing is nullable (ie the json is nullable) but because
            //we are adding the onSuccess, we know that is is not going to be a null so we can remove the "?"
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                //now we want to parse out the data in the json file and get list of movies
                //defining a variable to store the array for the json movies
                    //This is something that lives in the json parameter. Then we know that the data we
                    //are getting back all lives within the json object. then we say the method we want to use.
                    //Next we would pass in the key that we care about
                //If any of the keys mentioned in Movie.kt changes, our app will crash
                //We would like to catch that json exception to inform the developer that something went wrong
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    //After step 6: When we get data from onSuccess callback, we need to notify the
                        // adapter that the underlying data has changed
                    movieAdapter.notifyDataSetChanged()
                    //log out the parse movie objects
                    Log.i(TAG, "Movie list $movies")
                    }
                //e for error type (I assumed)
                catch (e: JSONException){
                        Log.e(TAG, "Encountered exception $e")
                    }

            }

        })

    }

}
