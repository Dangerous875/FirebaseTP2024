package com.tp.spotidogs.domain

import com.tp.spotidogs.core.toDataBase
import com.tp.spotidogs.data.repository.DogsRepository
import com.tp.spotidogs.domain.model.Dog
import javax.inject.Inject

class InsertDogToDataBase @Inject constructor( private val repository: DogsRepository) {

    suspend operator fun invoke(dog: Dog){
        repository.insertDogFromDataBase(dog.toDataBase())
    }
}