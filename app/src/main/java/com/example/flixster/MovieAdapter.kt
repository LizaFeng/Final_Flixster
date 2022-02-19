package com.example.flixster

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val MOVIE_EXTRA= "MOVIE_EXTRA"
//After creating the inner class, have MovieAdapter extend the RecyclerView adapter.
//RecyclerView adapter is parameterized by a ViewHolder and the one we will pass in here is the one we just defined
class MovieAdapter(private val context: Context, private val movies:List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    //ViewHolder is a component that is already defined by RecyclerView.
    //This ViewHolder that we defined is going to extend from the RecyclerView ViewHolder
    //We also have to add in a constructor parameter (in this case, itemView)
    //the interface View.OnClickListener
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        //Grab individual components in itemView (image view & 2 TextView) &
        // populate with correct data in movie
        private val tvTitle=itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview=itemView.findViewById<TextView>(R.id.tvOverview)
        private val ivPoster=itemView.findViewById<ImageView>(R.id.ivPoster)

        //We are not actually doing anything until we register the clickLisenter
        // Registering the click listener.
        init{
            //everytime we create a new viewHolder, we will call the function and pass "this" in
            itemView.setOnClickListener(this)

        }

        //Now that we have references to the ViewHolder, we can grab references
        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            //populate imageview (require the use of another library)
            Glide.with(context).load(movie.posterImageUrl).into(ivPoster)
        }

        override fun onClick(v: View?) {
            //Goal 1: Get notified of the particular movie which was clicked
            //The ViewHolder has a method called adapterPosition that will tell you the position or where is interacted
            //the newer version of adapterPosition/getAdapterPosition()
            val movie = movies[bindingAdapterPosition]
            //toast appears on the emulator itself and log appears in logcat
            //Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()

            //Goal 2: Use the intent system to navigate to the new activity
            //start by creating a new activity called DetailedActivity.kt
            //create a new intent. 2nd parameter is destination where to go and class to navigate to
            val intent= Intent(context, DetailActivity::class.java)
            //break down the movie object to its component parts band pass those into the intent extra
                //then retrieve them in the detail activity. we can do intent.putExtra("movie_title", movie.title)
                //where the movie_title is the key but then it would be tedious to have to change the key every time
                //But the putExtra function only works with specific items and not a whole object.
                //We added Parcelable and then turn the key into a constant which we declared at the top
            intent.putExtra(MOVIE_EXTRA, movie)
            //use our context and start the method startActivity and pass intent
            context.startActivity(intent)
        }
    }

    //After the getItemCount(), we work on this.
    //Objective: create a view holder of a type viewholder that we defined in ViewHolder(itemView: View) and return that
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Call layout inflater, get instance of it from context and inflate a layout.
        //The first parameter is the layout that we customized (item_movie.xml).
        //The second parameter is the parent view group.
        //The third parameter is false
        //Store all that in a variable to pass to the inner class ViewHolder
        val view=LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        //return a ViewHolder and pass that view in as a constructor parameter
        return ViewHolder(view)
    }

    //Given ViewHolder & position, get the data at that position and bind into that ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Define "bind" in the inner class ViewHolder
        val movie=movies[position]
        holder.bind(movie)
    }

    //Inline one line functions
    override fun getItemCount()= movies.size


}

