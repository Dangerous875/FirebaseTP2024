package com.tp.spotidogs.data.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
object MainScreenRoute

@Serializable
data class ZoomScreenRoute(val urlImage : String)

@Serializable
object FavoriteScreenRoute

@Serializable
object LoginScreenRoute

@Serializable
object AuthenticationScreenRoute

@Serializable
object RegisterScreenRoute

@Serializable
object TestFireStoreRoute
