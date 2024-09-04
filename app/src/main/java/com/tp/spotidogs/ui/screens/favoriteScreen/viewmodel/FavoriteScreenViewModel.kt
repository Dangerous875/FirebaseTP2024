package com.tp.spotidogs.ui.screens.favoriteScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tp.spotidogs.domain.DeleteDBUseCases
import com.tp.spotidogs.domain.DeleteDogFromDataBase
import com.tp.spotidogs.domain.GetAllDogsFromDataBase
import com.tp.spotidogs.domain.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val getAllDogsFromDataBase: GetAllDogsFromDataBase,
    private val deleteDogFromDataBase: DeleteDogFromDataBase,
    private val deleteDBUseCases: DeleteDBUseCases
) :
    ViewModel() {

    private val _dogList = MutableStateFlow<List<Dog>>(emptyList())
    val dogList = _dogList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        _isLoading.value = true
        viewModelScope.launch {
            _dogList.value = getAllDogsFromDataBase()
            _isLoading.value = false
        }
    }

    fun deleteDogFromDB(dog:Dog){
        viewModelScope.launch(Dispatchers.IO) {
            deleteDogFromDataBase(dog)
            _dogList.value = getAllDogsFromDataBase()
        }
    }

    fun deleteDB(){
        viewModelScope.launch(Dispatchers.IO){
            deleteDBUseCases()
            _dogList.value = getAllDogsFromDataBase()
        }
    }
}