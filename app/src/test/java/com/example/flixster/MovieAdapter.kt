package com.example.flixster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//After creating the inner class, have MovieAdapter extend the RecyclerView adapter.
//RecyclerView adapter is parameterized by a ViewHolder and the one we will pass in here is the one we just defined
class MovieAdapter(private val context: Context, private val movies:List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    //ViewHolder is a component that is already defined by RecyclerView.
    //This ViewHolder that we defined is going to extend from the RecyclerView ViewHolder
    //We also have to add in a constructor parameter (in this case, itemView)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //Grab individual components in itemView (image view & 2 TextView) &
            // populate with correct data in movie
        private val tvTitle=itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview=itemView.findViewById<TextView>(R.id.tvOverview)
        private val ivPoster=itemView.findViewById<ImageView>(R.id.ivPoster)
        //Now that we have references to the ViewHolder, we can grab references
        fun bind(movie: Movie){
            tvTitle.text=movie.title
            tvOverview.text=movie.overview
            //TODO: populate imageview (require the use of another library
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
        //Get the data at that position
        val movie = movies[position]
        //Define "bind" in the inner class ViewHolder
        holder.bind(movie)
    }

    //Inline one line functions
    override fun getItemCount()= movies.size


}
