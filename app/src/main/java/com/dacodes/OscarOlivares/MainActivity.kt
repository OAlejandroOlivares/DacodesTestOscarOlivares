package com.dacodes.OscarOlivares

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.disposables.CompositeDisposable
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() , MoviesAdapter.moviesAdapterInterface {
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var currentPage: Int = 1
    private var visibleThreshold = 5
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private var moviesArrayList: ArrayList<Movies> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MoviesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var myCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myCompositeDisposable = CompositeDisposable()
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            viewManager = GridLayoutManager(this, 2)
        }else{
            viewManager = GridLayoutManager(this, 4)
        }
        recyclerView = findViewById(R.id.rv)
        viewAdapter = MoviesAdapter(moviesArrayList,this,this)
        recyclerView.adapter = viewAdapter
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            moviesArrayList = ArrayList()
            setAdapter()
            currentPage = 1
            getMovies(currentPage)
        }
        recyclerView.layoutManager = viewManager
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = viewManager.itemCount
                //last visible item position
                lastVisibleItem = (viewManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold))
                {
                    getMovies(currentPage+1)
                    isLoading = true
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
        if (savedInstanceState!=null){
            moviesArrayList = savedInstanceState.getParcelableArrayList<Movies>("movies") as ArrayList<Movies>
            currentPage = savedInstanceState.getInt("page")
            setAdapter()
        }else{
            getMovies(currentPage)
        }
        Log.d("test",LogicTestFunction(1,1))
        Log.d("test",LogicTestFunction(2,2))
        Log.d("test",LogicTestFunction(3,1))
        Log.d("test",LogicTestFunction(3,3))
    }

    private fun getMovies(page:Int){
        swipeRefreshLayout.isRefreshing = true
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular?api_key=a28c4bc831b590dc669ef8a459fdbff7&laguage=es&page="+page)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val json :JSONObject = JSONObject(response.body!!.string())
                    val results: JSONArray = json.getJSONArray("results")
                    for (i in 0 until results.length()){
                        val result:JSONObject = results.get(i) as JSONObject
                        moviesArrayList.add(Movies(result.getString("id"),result.getString("title"),result.getString("poster_path"),result.getString("release_date"),result.getDouble("vote_average"),result.getString("overview"),result.getString("popularity"),result.getJSONArray("genre_ids").toString()))
                    }
                    this@MainActivity.runOnUiThread(Runnable {
                        setAdapter()

                    })
                }
            }
        })
    }

    private fun setAdapter() {
        viewAdapter.updateData(moviesArrayList)
        swipeRefreshLayout.isRefreshing = false
        isLoading=false
        currentPage +=1
    }

    override fun onMovieClicked(movie: Movies) {
        val intent = Intent(this,MovieDetail::class.java).apply {
            putExtra("movie",movie)
        }
        startActivity(intent)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("movies",moviesArrayList)
        outState.putInt("page",currentPage)
        super.onSaveInstanceState(outState)
    }

    fun LogicTestFunction(rows : Int, columns:Int): String {
        if (rows > columns)
        {
            if (columns % 2 == 0)
                return "U"
            else
                return "D"
        }
        else
        {
            if (rows % 2 == 0)
                return "L"
            else
                return "R"
        }
    }

}