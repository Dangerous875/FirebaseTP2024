package com.tp.spotidogs.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tp.spotidogs.data.database.firestore.local.firestore_collection_favoriteDogs
import com.tp.spotidogs.data.database.firestore.model.DogStore
import com.tp.spotidogs.data.database.room.dao.DogsDao
import com.tp.spotidogs.data.database.room.entities.DogEntity
import com.tp.spotidogs.data.database.room.entities.toFireStore
import com.tp.spotidogs.data.local.DogsBreeds
import com.tp.spotidogs.data.local.SelectBreed
import com.tp.spotidogs.data.network.service.DogApiService
import com.tp.spotidogs.domain.model.Dog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DogsRepository @Inject constructor(
    private val dogApiService: DogApiService,
    private val dogsBreeds: DogsBreeds,
    private val selectBreed: SelectBreed,
    private val dogsDao: DogsDao,
    private val firestore: FirebaseFirestore
) {

    suspend fun getDogsByBreeds(query: String): List<String> {
        return dogApiService.getDogsBreedsByName(query)
    }

    suspend fun getAllBreeds(): List<String> {
        if (dogsBreeds.dogBreeds.isEmpty()) {
            dogsBreeds.dogBreeds = dogApiService.getAllNamesFromApi()
        }
        return dogsBreeds.dogBreeds

    }

    fun setSelectBreed(breed: String) {
        selectBreed.selectBreed = breed
    }

    fun getSelectBreed(): String {
        return selectBreed.selectBreed!!
    }

    suspend fun getAllDogsFromDataBase(): List<DogEntity> {
        return dogsDao.getAllDogs()
    }

    suspend fun insertDogFromDataBase(dog: DogEntity) {
        dogsDao.insertDogDataBase(dog)
        addFavoriteDogFireStore(dog.toFireStore())
    }

    suspend fun deleteItemFromDB(dog: Dog) {
        dogsDao.deleteDogByUrl(dog.imageUrl)
        deleteItemFromFiresStore(dog.imageUrl)
    }

    suspend fun deleteAllItemsFromDB() {
        dogsDao.deleteAllFavoritesBreed()
        deleteAllDocumentsFireStore()
    }

    private fun addFavoriteDogFireStore(dogStore: DogStore) {
        firestore.collection(firestore_collection_favoriteDogs).add(dogStore)
    }

    private fun deleteItemFromFiresStore(imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val artistQuery = firestore.collection(firestore_collection_favoriteDogs)
                    .whereEqualTo("imageUrl", imageUrl)
                    .get()
                    .await()

                for (document in artistQuery.documents) {
                    firestore.collection(firestore_collection_favoriteDogs)
                        .document(document.id)
                        .delete()
                }
            } catch (e: Exception) {
                Log.e("KlyxFirestore", "Error al buscar/eliminar el dog", e)
            }
        }
    }

    private fun deleteAllDocumentsFireStore() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val collection = firestore.collection(firestore_collection_favoriteDogs)
                val documents = collection.get().await()
                for (document in documents.documents) {
                    collection.document(document.id).delete()
                }
            } catch (e: Exception) {
                Log.e("KlyxFirestore", "Error al eliminar todos los documentos", e)
            }
        }
    }

    fun getAllDogsFromFireStore(): Flow<List<DogStore>> = callbackFlow {
        val listener = firestore.collection(firestore_collection_favoriteDogs)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Cierra el Flow si hay un error
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val dogList = snapshot.documents.mapNotNull {
                        it.toObject(DogStore::class.java)
                    }
                    trySend(dogList).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }
}

