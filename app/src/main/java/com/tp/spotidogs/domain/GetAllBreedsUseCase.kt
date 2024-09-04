package com.tp.spotidogs.domain

import com.tp.spotidogs.data.repository.DogsRepository
import javax.inject.Inject

class GetAllBreedsUseCase @Inject constructor(private val dogsRepository: DogsRepository) {

    suspend operator fun invoke(): List<String> {
        return dogsRepository.getAllBreeds()
    }
}