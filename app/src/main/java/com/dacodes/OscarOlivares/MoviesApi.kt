package com.dacodes.OscarOlivares

import io.reactivex.Observable
import retrofit2.http.GET
import java.util.*
import kotlin.collections.ArrayList


interface MoviesApi {
    @GET("movie/latest?api_key=a28c4bc831b590dc669ef8a459fdbff7&laguage=es")
    fun getMovies() : Observable<ArrayList<Movies>>
}