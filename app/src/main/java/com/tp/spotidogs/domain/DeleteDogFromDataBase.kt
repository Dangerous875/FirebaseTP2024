package com.tp.spotidogs.domain

import com.tp.spotidogs.data.repository.DogsRepository
import com.tp.spotidogs.domain.model.Dog
import javax.inject.Inject

class DeleteDogFromDataBase @Inject constructor(private val dogsRepository: DogsRepository) {

    suspend operator fun invoke(dog: Dog){
        dogsRepository.deleteItemFromDB(dog)
    }
}