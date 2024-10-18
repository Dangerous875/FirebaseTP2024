package com.tp.spotidogs.ui.screens.firestore.firestoreScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.tp.spotidogs.ui.screens.firestore.firestoreScreen.local.firestore_collection
import com.tp.spotidogs.ui.screens.firestore.firestoreScreen.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FireStoreViewModel @Inject constructor(private val firestore: FirebaseFirestore) :
    ViewModel() {

    private val _list = MutableStateFlow<List<Artist>>(emptyList())
    val list = _list.asStateFlow()

    init {
        listenToArtistChanges()
    }

    // Escuchar cambios en tiempo real
    private fun listenToArtistChanges() {
        firestore.collection(firestore_collection)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("KlyxFirestore", "Error al escuchar cambios", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val artists = snapshot.documents.mapNotNull {
                        it.toObject(Artist::class.java)
                    }
                    _list.value = artists
                }
            }
    }

    fun createArtist() {
        val random = (1..10000).random()
        val artist = Artist(name = "Random $random", number = random)
        firestore.collection(firestore_collection).add(artist)
            .addOnSuccessListener {
                Log.i("KlyxFirestore", "Artista creado exitosamente")
            }
            .addOnFailureListener {
                Log.e("KlyxFirestore", "Error al crear el artista", it)
            }
    }

    fun deleteArtist(artist: Artist) {
        viewModelScope.launch {
            try {
                val artistQuery = firestore.collection(firestore_collection)
                    .whereEqualTo("number", artist.number)
                    .get()
                    .await()

                for (document in artistQuery.documents) {
                    firestore.collection(firestore_collection)
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.i("KlyxFirestore", "Artista eliminado exitosamente")
                        }
                        .addOnFailureListener {
                            Log.e("KlyxFirestore", "Error al eliminar el artista", it)
                        }
                }
            } catch (e: Exception) {
                Log.e("KlyxFirestore", "Error al buscar/eliminar el artista", e)
            }
        }
    }

    fun updateNumberInBD(artist: Artist) {
        val random = (1..10000).random()
        viewModelScope.launch {
            try {
                val artistQuery = firestore.collection(firestore_collection)
                    .whereEqualTo("number", artist.number)
                    .get()
                    .await()

                for (document in artistQuery.documents) {
                    firestore.collection(firestore_collection)
                        .document(document.id)
                        .update("number", random) // update
                        .addOnSuccessListener {
                            Log.i("KlyxFirestore", "Nombre del artista actualizado exitosamente")
                        }
                        .addOnFailureListener {
                            Log.e("KlyxFirestore", "Error al actualizar el nombre del artista", it)
                        }
                }
            } catch (e: Exception) {
                Log.e("KlyxFirestore", "Error al buscar/actualizar el nombre del artista", e)
            }
        }
    }
}