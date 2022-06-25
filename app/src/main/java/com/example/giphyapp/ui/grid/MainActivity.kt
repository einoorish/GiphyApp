package com.example.giphyapp.ui.grid

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.giphyapp.R
import com.example.giphyapp.data.api.GiphyApiHelper
import com.example.giphyapp.data.api.ITEMS_PER_PAGE_LIMIT
import com.example.giphyapp.data.api.RetrofitBuilder
import com.example.giphyapp.data.database.RoomDatabaseBuilder
import com.example.giphyapp.data.repository.MainRepository.Companion.DEFAULT_SEARCH_PHRASE
import com.example.giphyapp.data.model.GifObject
import com.example.giphyapp.databinding.ActivityMainBinding
import com.example.giphyapp.ui.HomeViewModel
import com.example.giphyapp.ui.ViewModelFactory
import com.example.giphyapp.utils.PaginationScrollListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: HomeViewModel

    private lateinit var adapter: GifsGridAdapter
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private var currentSearchPhrase = DEFAULT_SEARCH_PHRASE
    private var totalCountOfGifs = 0L

    private lateinit var searchView: SearchView
    private lateinit var searchQueryTextListener: SearchView.OnQueryTextListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(GiphyApiHelper(RetrofitBuilder.apiService),
                RoomDatabaseBuilder.buildDatabase(this)))
            .get(HomeViewModel::class.java)

        setupRecyclerView()
        loadData(currentSearchPhrase, 0, true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = true

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrBlank())
                    loadData(query, 0, true)
                return true
            }
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> return true
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(searchPhrase: String, offset: Int, isNewSearchData:Boolean){
        viewModel.getGifs(searchPhrase, offset).observe(this, Observer {
            it?.let { resource ->
                resource.let { response ->
                    totalCountOfGifs = response.pagination.count
                    addToList(response.gifs, isNewSearchData)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.columns))
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if(adapter.getItemViewType(position) == adapter.FOOTER_VIEW)
                    return 2
                return 1
            }
        }
        binding.rvAll.layoutManager = layoutManager
        binding.rvAll.addOnScrollListener(object: PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                isLoading = true
                loadData(currentSearchPhrase, currentPage * ITEMS_PER_PAGE_LIMIT, false);
                currentPage+=1
            }

            override fun isLoading(): Boolean = isLoading

            override fun isLastPage(): Boolean = isLastPage
        })

        adapter = GifsGridAdapter(arrayListOf()) { url -> viewModel.addGifToDeleted(url) }
        binding.rvAll.adapter = adapter
    }

    private fun addToList(gifs: List<GifObject>, isNewSearchData: Boolean) {
        adapter.apply{
            // footer is always the last element, so it should be removed before pushing new items
            removeLoadingFooter()

            if(isNewSearchData) {
                notifyItemRangeRemoved(0, getCurrentNumOfGifs());
                addNewGifs(gifs)
            } else addNextGifs(gifs)

            if(getCurrentNumOfGifs() < totalCountOfGifs){
                isLastPage = false
                addLoadingFooter()
            } else isLastPage = true

            notifyItemRangeInserted(itemCount, gifs.size - 1);
            isLoading = false
        }
    }
}