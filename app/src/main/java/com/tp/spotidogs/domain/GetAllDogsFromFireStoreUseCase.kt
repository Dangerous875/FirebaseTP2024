package com.tp.spotidogs.domain

import com.tp.spotidogs.data.database.firestore.model.DogStore
import com.tp.spotidogs.data.repository.DogsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDogsFromFireStoreUseCase @Inject constructor(private val repository: DogsRepository) {
    operator fun invoke(): Flow<List<DogStore>> {
        return repository.getAllDogsFromFireStore()
    }
}