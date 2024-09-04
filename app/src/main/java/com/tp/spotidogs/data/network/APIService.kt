package com.tp.spotidogs.data.network

import com.tp.spotidogs.data.model.DogNameResponse
import com.tp.spotidogs.data.model.DogResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("breed/{dogBreed}/images")
    suspend fun getDogsByApi(@Path("dogBreed") query: String): Response<DogResponse>

    @GET("breeds/list/all")
    suspend fun getAllBreedsNames():Response<DogNameResponse>
}