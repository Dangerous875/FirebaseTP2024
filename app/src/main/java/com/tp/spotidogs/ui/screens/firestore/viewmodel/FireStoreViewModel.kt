package com.tp.spotidogs.ui.screens.firestore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.tp.spotidogs.data.database.firestore.local.firestore_collection_favoriteDogs
import com.tp.spotidogs.data.database.firestore.model.DogStore
import com.tp.spotidogs.domain.DeleteDBUseCases
import com.tp.spotidogs.domain.DeleteDogFromDataBase
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
    private val firestore: FirebaseFirestore
) :
    ViewModel() {

    private val _dogList = MutableStateFlow<List<DogStore>>(emptyList())
    val dogList = _dogList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            listenToArtistChanges()
            delay(5000)
            if (_dogList.value.isEmpty()) {
                _isLoading.value = false
            }
        }

    }

    fun deleteDogFromDB(dog: Dog){
        viewModelScope.launch(Dispatchers.IO) {
            deleteDogFromDataBase(dog)
        }
    }

    fun deleteDB(){
        viewModelScope.launch(Dispatchers.IO){
            deleteDBUseCases()
        }
    }

    // Escuchar cambios en tiempo real
    private fun listenToArtistChanges() {
        firestore.collection(firestore_collection_favoriteDogs)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val dogList = snapshot.documents.mapNotNull {
                        it.toObject(DogStore::class.java)
                    }
                    _dogList.value = dogList
                    if (_dogList.value.isNotEmpty()){
                        _isLoading.value = false
                    }
                }
            }
    }

}




