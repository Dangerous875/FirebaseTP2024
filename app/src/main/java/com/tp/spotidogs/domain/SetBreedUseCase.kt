package com.tp.spotidogs.domain

import com.tp.spotidogs.data.repository.DogsRepository
import javax.inject.Inject

class SetBreedUseCase @Inject constructor(private val dogsRepository: DogsRepository) {

    operator fun invoke(breed: String) {
        dogsRepository.setSelectBreed(breed)
    }
}