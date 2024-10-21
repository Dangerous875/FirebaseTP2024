package com.tp.spotidogs.ui.screens.firestore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tp.spotidogs.data.database.firestore.model.DogStore
import com.tp.spotidogs.domain.DeleteDBUseCases
import com.tp.spotidogs.domain.DeleteDogFromDataBase
import com.tp.spotidogs.domain.GetAllDogsFromFireStoreUseCase
import com.tp.spotidogs.domain.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireStoreViewModel @Inject constructor(
    private val deleteDogFromDataBase: DeleteDogFromDataBase,
    private val deleteDBUseCases: DeleteDBUseCases,
    private val getAllDogsFromFireStoreUseCase: GetAllDogsFromFireStoreUseCase

) :
    ViewModel() {

    private val _dogList = MutableStateFlow<List<DogStore>>(emptyList())
    val dogList = _dogList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            getAllFavoriteDogs()
        }
    }

    private suspend fun getAllFavoriteDogs() {
        getAllDogsFromFireStoreUseCase().collect { dogList ->
            if (dogList.isNotEmpty()) {
                _dogList.value = dogList
                _isLoading.value = false
            } else {
                _isLoading.value = true
                delay(1000)
                _isLoading.value = false
            }
        }
    }

    fun deleteDogFromDB(dog: Dog) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteDogFromDataBase(dog)
        }
    }

    fun deleteDB() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteDBUseCases()
        }
    }

}




