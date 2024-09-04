package com.tp.spotidogs.domain

import com.tp.spotidogs.data.repository.DogsRepository
import javax.inject.Inject

class DeleteDBUseCases @Inject constructor(private val repository: DogsRepository) {

    suspend operator fun invoke(){
        repository.deleteAllItemsFromDB()
    }
}