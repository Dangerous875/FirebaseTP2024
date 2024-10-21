package com.tp.spotidogs.ui.screens.mainDogsScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tp.spotidogs.domain.DeleteDogFromDataBase
import com.tp.spotidogs.domain.GetAllBreedsUseCase
import com.tp.spotidogs.domain.GetDogsByBreedsUseCase
import com.tp.spotidogs.domain.GetSelectBreed
import com.tp.spotidogs.domain.InsertDogToDataBase
import com.tp.spotidogs.domain.SetBreedUseCase
import com.tp.spotidogs.domain.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getDogsByBreedsUseCase: GetDogsByBreedsUseCase,
    private val getSelectBreed: GetSelectBreed,
    getAllBreedsUseCase: GetAllBreedsUseCase,
    private val insertDogToDataBase: InsertDogToDataBase,
    private val deleteDogFromDataBase: DeleteDogFromDataBase,
    private val setBreedUseCase: SetBreedUseCase
) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _allBreeds = MutableStateFlow<List<String>>(emptyList())
    val allBreeds = _allBreeds.asStateFlow()
    private val _listDogs = MutableStateFlow<List<Dog>>(emptyList())
    val lisDogs = _listDogs.asStateFlow()

    init {

        _isLoading.value = true
        viewModelScope.launch {
            _allBreeds.value = getAllBreedsUseCase()
            _listDogs.value = getDogsByBreedsUseCase(getSelectBreed())
            _isLoading.value = false
        }
    }

    fun getDogsByBreeds(query: String) {
        _isLoading.value = true
        setBreedUseCase(query)
        viewModelScope.launch {
            _listDogs.value = getDogsByBreedsUseCase(query)
            delay(1000)
            _isLoading.value = false
        }
    }

    fun insertDog(dog:Dog){
        viewModelScope.launch(Dispatchers.IO) {
            insertDogToDataBase(dog)
            Log.i("klyx",dog.toString())
        }
    }

    fun deleteFromDB(dog: Dog){
        viewModelScope.launch(Dispatchers.IO) {
            deleteDogFromDataBase(dog)
        }
    }
}