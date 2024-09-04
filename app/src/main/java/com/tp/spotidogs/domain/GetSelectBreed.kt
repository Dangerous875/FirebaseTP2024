package com.tp.spotidogs.domain

import com.tp.spotidogs.data.repository.DogsRepository
import javax.inject.Inject

class GetSelectBreed @Inject constructor(private val dogsRepository: DogsRepository) {

    operator fun invoke(): String {
        return dogsRepository.getSelectBreed()
    }
}