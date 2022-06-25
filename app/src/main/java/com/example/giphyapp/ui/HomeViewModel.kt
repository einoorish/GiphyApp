package com.example.giphyapp.ui

import androidx.lifecycle.*
import com.example.giphyapp.data.model.ApiResponse
import com.example.giphyapp.data.model.DownsampledImageURL
import com.example.giphyapp.data.repository.MainRepository
import com.example.giphyapp.data.model.GifObject
import com.example.giphyapp.data.model.PaginationResponseObject
import com.example.giphyapp.utils.CombinedLiveData
import com.example.giphyapp.utils.Resource
import com.example.giphyapp.utils.ResourceStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getGifs(searchPhrase: String, offset: Int) = CombinedLiveData(
        getGifsFromApi(searchPhrase, offset),
        deletedLiveData)
        { data1, data2 -> filterData(data1, data2) }

    private fun filterData(resource: ApiResponse?, itemsToDelete: List<DownsampledImageURL>?): ApiResponse? {
        if (resource != null  && itemsToDelete != null) {
            var itemsDeleted = 0
            val filteredGifs = ArrayList<GifObject>()
            for (item in resource.gifs) {
                if(!itemsToDelete.contains(item.images.downsampledImage)) {
                    filteredGifs.add(item)
                } else itemsDeleted++

            }

            return ApiResponse(filteredGifs, PaginationResponseObject(resource.pagination.count - itemsDeleted))
        } else return resource
    }

    private val deletedLiveData: LiveData<List<DownsampledImageURL>> = mainRepository.getDeletedGifs().asLiveData()

    private fun getGifsFromApi(searchPhrase: String, offset: Int): LiveData<ApiResponse> = liveData(Dispatchers.IO){
        emit(mainRepository.getGifs(searchPhrase, offset))
    }

    fun addGifToDeleted(url: String) {
        viewModelScope.launch {
            mainRepository.addGif(url)
        }

    }
}