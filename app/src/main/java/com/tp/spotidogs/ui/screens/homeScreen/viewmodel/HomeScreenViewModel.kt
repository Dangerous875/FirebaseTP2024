package com.tp.spotidogs.ui.screens.homeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tp.spotidogs.domain.GetAllBreedsUseCase
import com.tp.spotidogs.domain.SetBreedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllBreedsUseCase: GetAllBreedsUseCase,
    private val setBreedUseCase: SetBreedUseCase
) :
    ViewModel() {

    private val _allBreeds = MutableStateFlow<List<String>>(emptyList())
    val allBreeds = _allBreeds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        _isLoading.value = true
        viewModelScope.launch {
            _allBreeds.value = getAllBreedsUseCase()
            delay(1000)
            _isLoading.value = false
        }
    }

    fun setSelectBreed(breed : String){
        setBreedUseCase(breed)
    }
}