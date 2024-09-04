package com.tp.spotidogs.data.model

import com.google.gson.annotations.SerializedName

data class DogNameResponse(
    @SerializedName("message") var message: Map<String, List<String>>,
    @SerializedName("status") var status: String
)
