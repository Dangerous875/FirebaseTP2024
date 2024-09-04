package com.tp.spotidogs.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsBreeds @Inject constructor() {

    var dogBreeds : List<String> = emptyList()

}