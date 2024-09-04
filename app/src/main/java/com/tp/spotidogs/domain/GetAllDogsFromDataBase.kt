package com.tp.spotidogs.domain

import com.tp.spotidogs.core.toDomain
import com.tp.spotidogs.data.repository.DogsRepository
import com.tp.spotidogs.domain.model.Dog
import javax.inject.Inject

class GetAllDogsFromDataBase @Inject constructor( private val repository: DogsRepository) {

    suspend operator fun invoke():List<Dog>{
        return repository.getAllDogsFromDataBase().map { it.toDomain() }
    }
}