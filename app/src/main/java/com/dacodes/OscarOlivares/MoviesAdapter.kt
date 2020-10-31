package com.dacodes.OscarOlivares

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MoviesAdapter(var movies: ArrayList<Movies>, val context: Context, var listener: moviesAdapterInterface) : RecyclerView.Adapter<MoviesAdapter.mViewHolder>() {


    class mViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster = view.findViewById(R.id.imagen) as ImageView
        val titulo = view.findViewById(R.id.title) as TextView
        val fecha = view.findViewById(R.id.fecha) as TextView
        val rate = view.findViewById(R.id.rate) as TextView

        fun bind(movie:Movies, context: Context){
            titulo.text = movie.titulo
            fecha.text = movie.fecha
            rate.text = movie.rate.toString()
            val picasso = Picasso.get()
            picasso.load("https://image.tmdb.org/t/p/w500"+movie.poster).error(R.mipmap.no_image_avaiable).into(poster)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return mViewHolder(layoutInflater.inflate(R.layout.movie_item_rv_mainactivity, parent, false))
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val movie = movies.get(position)
        holder.bind(movie,context)
        holder.itemView.setOnClickListener(View.OnClickListener {
            listener.onMovieClicked(movie)
        })
    }

    override fun getItemCount(): Int {
        return movies.count()
    }

    fun updateData(moviesArrayList: java.util.ArrayList<Movies>) {
        this.movies = moviesArrayList
        notifyDataSetChanged()
    }

    public interface moviesAdapterInterface{
        fun onMovieClicked(movie: Movies)
    }
}