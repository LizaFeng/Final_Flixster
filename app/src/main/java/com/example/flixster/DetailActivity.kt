package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

//created an api key (not in android studio)
private const val YOUTUBE_API_KEY = "AIzaSyC8M9vazEaciOtOjnZ1Yzx2zm8OWc7jQHc"
//TODO: worked on this while waiting to diagnose jar error
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TAG= "DetailActivity"
//Change class DetailActivity : AppCompatActivity() to class DetailActivity : YouTubeBaseActivity()
class DetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview= findViewById(R.id.tvOverview)
        ratingBar= findViewById(R.id.rbVoteAverage)
        ytPlayerView= findViewById(R.id.player)

        //Get the movie object out of the intent extra.
        //The extra that we are getting out is of type Movie <Movie>
        //The name is going to be "MOVIE_EXTRA"
        //We are going to cast this as a Movie because by default it is a nullable movie
            //but we know that we should always have a valid movie extract in the intent
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i(TAG, "Movie is $movie")
        //Taking information from the movie object and putting it into the various widgets onscreen
        tvTitle.text= movie.title
        tvOverview.text= movie.overview
        //Get rating of overview
        ratingBar.rating = movie.voteAverage.toFloat()


        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results= json.jsonObject.getJSONArray("results")
                //Grab the first element out of that Json Results and use for youtube trailing
                if(results.length()==0){
                    //print a warning to say there are no trailers found
                    Log.w(TAG, "No movie trailers found")
                    return
                }
                val movieTrailerJson=results.getJSONObject(0)
                val youtubeKey= movieTrailerJson.getString("key")
                //Now our goal is to play the youtube video with this trailer
                //call youtube.initialize and pass in the youtube key instead of the hard coded youtube key
                //We used android studios auto-complete to create the method for initializeYoutube
                initializeYoutube(youtubeKey)
                //notice that we only initialize when we are onSuccess

            }

        })



    }

    private fun initializeYoutube(youtubeKey: String?) {
        //the vtPlayerView.initialize was actually created first but then we put it in  fun intializeYoutube
        //After we grab a reference to the playerView, we will call .initialize and then pass
        //in the callback function for when the initialization succeeded or failed
        ytPlayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                //Original variable names p0,p1, &p2 are not specific so lets change that
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                //youTubePlayer.cueVideo("5xVh-7ywKpE"); The youTuberPlayer refers to the player variable
                //so we make the following changes
                //we use the ? because player is nullable
                player?.cueVideo(youtubeKey);
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure")
            }


        })

    }
}