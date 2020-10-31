package com.dacodes.OscarOlivares

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MovieDetail : AppCompatActivity() {
    lateinit var poster:ImageView
    lateinit var titulo:TextView
    lateinit var fecha:TextView
    lateinit var rate:TextView
    lateinit var duracion:TextView
    lateinit var generos:TextView
    lateinit var descripcion:TextView
    lateinit var movie:Movies


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail)
        poster = findViewById(R.id.imageView);
        titulo = findViewById(R.id.titulo);
        fecha = findViewById(R.id.fechaValue);
        rate = findViewById(R.id.calificacionValue);
        duracion = findViewById(R.id.duracionValue);
        generos = findViewById(R.id.generosValue);
        descripcion = findViewById(R.id.descripcionValue);
        if (savedInstanceState!= null){
            movie = savedInstanceState.getParcelable<Movies>("movie") as Movies
            duracion.text = savedInstanceState.getString("duracion")
            generos.text = savedInstanceState.getString("generos")
        }else {
            movie = intent.getParcelableExtra("movie")
            getMovieDetails(movie.id)
        }
        val picasso = Picasso.get()
        picasso.load("https://image.tmdb.org/t/p/w500"+movie.poster).error(R.mipmap.no_image_avaiable).into(poster)
        titulo.text = movie.titulo
        fecha.text = movie.fecha
        rate.text = movie.rate.toString()
        //generos.text = movie.generos
        descripcion.text = movie.descripcion
    }

    private fun getMovieDetails(id: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/"+id+"?api_key=a28c4bc831b590dc669ef8a459fdbff7&laguage=es")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val json : JSONObject = JSONObject(response.body!!.string())
                    val generosArray: JSONArray =json.getJSONArray("genres")
                    var generosString:String = ""
                    for (i in 0 until generosArray.length()){
                        var tmp:JSONObject = generosArray.getJSONObject(i)
                        generosString = generosString + tmp.getString("name") + " "
                    }
                    this@MovieDetail.runOnUiThread(Runnable {
                        duracion.text = json.getString("runtime")
                        generos.text = generosString.trim()
                    })
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("movie",movie)
        outState.putString("duracion",duracion.text.toString())
        outState.putString("generos",generos.text.toString())
        super.onSaveInstanceState(outState)
    }
}
