package com.example.giphyapp.ui

import androidx.lifecycle.*
import com.example.giphyapp.data.repository.MainRepository
import com.example.giphyapp.utils.Resource
import kotlinx.coroutines.Dispatchers

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