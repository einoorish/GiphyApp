package com.example.giphyapp.ui

import androidx.lifecycle.*
import com.example.giphyapp.data.model.ApiResponse
import com.example.giphyapp.data.repository.MainRepository
import com.example.giphyapp.data.model.GifObject
import com.example.giphyapp.data.model.PaginationResponseObject
import com.example.giphyapp.utils.CombinedLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun getGifs(searchPhrase: String, offset: Int) = CombinedLiveData(
        getGifsFromApi(searchPhrase, offset),
        deletedLiveData)
        { data1, data2 -> filterData(data1, data2) }

    private fun filterData(resource: ApiResponse?, itemsToDelete: List<String>?): ApiResponse? {
        if (resource != null  && itemsToDelete != null) {
            var itemsDeleted = 0
            val filteredGifs = ArrayList<GifObject>()
            for (item in resource.gifs) {
                if(!itemsToDelete.contains(item.id)) {
                    filteredGifs.add(item)
                } else itemsDeleted++

            }

            return ApiResponse(filteredGifs, PaginationResponseObject(resource.pagination.count - itemsDeleted))
        } else return resource
    }

    private val deletedLiveData: LiveData<List<String>> = mainRepository.getDeletedGifIds().asLiveData()

    private fun getGifsFromApi(searchPhrase: String, offset: Int): LiveData<ApiResponse> = liveData(Dispatchers.IO){
        emit(mainRepository.getGifs(searchPhrase, offset))
    }

    fun addGifToDeleted(gif: GifObject) {
        viewModelScope.launch {
            mainRepository.addGifId(gif)
        }

    }
}