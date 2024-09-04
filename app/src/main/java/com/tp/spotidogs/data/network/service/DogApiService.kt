package com.tp.spotidogs.data.network.service

import com.tp.spotidogs.data.network.APIService
import javax.inject.Inject

class DogApiService @Inject constructor(private val api: APIService) {

    suspend fun getDogsBreedsByName(query: String): List<String> {
        val response = api.getDogsByApi(query)
        return response.body()?.message ?: emptyList()
    }

    suspend fun getAllNamesFromApi():List<String>{
        val response = api.getAllBreedsNames()
        val listNames: List<String>? = response.body()?.message?.map { it.key }
        return listNames ?: emptyList()
    }
}