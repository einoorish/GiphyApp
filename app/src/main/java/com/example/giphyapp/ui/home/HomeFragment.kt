package com.example.giphyapp.ui.home

import android.app.SearchManager
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.giphyapp.R
import com.example.giphyapp.data.api.GiphyApiHelper
import com.example.giphyapp.data.api.ITEMS_PER_PAGE_LIMIT
import com.example.giphyapp.data.api.RetrofitBuilder
import com.example.giphyapp.data.response.GifObject
import com.example.giphyapp.databinding.FragmentHomeBinding
import com.example.giphyapp.utils.PaginationScrollListener
import com.example.giphyapp.utils.ResourceStatus


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private lateinit var adapter: GifsAdapter
    private var currentPage = 1;
    private var isLoading = false
    private var isLastPage = false
    private var currentSearchPhrase = "hi"
    private var totalCountOfGifs = 0L

    private lateinit var searchView: SearchView
    private lateinit var searchQueryTextListener: SearchView.OnQueryTextListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProviders.of(this, ViewModelFactory(GiphyApiHelper(RetrofitBuilder.apiService))).get(HomeViewModel::class.java)

        setHasOptionsMenu(true)

        setupRecyclerView()
        loadData(currentSearchPhrase, 0, true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = true

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrBlank())
                    loadData(query, 0, true)
                return true
            }
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> return true
        }
        searchView.setOnQueryTextListener(searchQueryTextListener)
        return super.onOptionsItemSelected(item)
    }

    fun loadData(searchPhrase: String, offset: Int, isNewSearchData:Boolean){
        viewModel.getGifs(searchPhrase, offset).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    ResourceStatus.SUCCESS -> {
                        binding.rvAll.visibility = View.VISIBLE
                        resource.data?.let { response ->
                            totalCountOfGifs = response.pagination.count
                            addToList(response.gifs, isNewSearchData)
                        }
                    }
                    ResourceStatus.ERROR -> {
                        Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                    }
                    ResourceStatus.LOADING -> {
                        if (isNewSearchData)
                            binding.rvAll.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(context,2)
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

        adapter = GifsAdapter(arrayListOf())
        binding.rvAll.adapter = adapter
    }


    private fun addToList(gifs: List<GifObject>, isNewSearchData: Boolean) {
        adapter.apply{
            // footer is always the last element, so it should be removed before pushing new items
            adapter.removeLoadingFooter()

            if(isNewSearchData) {
                notifyItemRangeRemoved(0, adapter.getCurrentNumOfGifs());
                addNewGifs(gifs)
            } else addNextGifs(gifs)

            if(adapter.getCurrentNumOfGifs() < totalCountOfGifs){
                isLastPage = false
                adapter.addLoadingFooter()
            } else isLastPage = true

            adapter.notifyItemRangeInserted(adapter.itemCount, gifs.size - 1);
            isLoading = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}