package com.example.giphyapp.ui.home

import androidx.lifecycle.*
import com.example.giphyapp.data.repository.MainRepository
import com.example.giphyapp.data.response.ApiResponse
import com.example.giphyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getGifs(searchPhrase: String, offset: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getGifs(searchPhrase, offset)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}