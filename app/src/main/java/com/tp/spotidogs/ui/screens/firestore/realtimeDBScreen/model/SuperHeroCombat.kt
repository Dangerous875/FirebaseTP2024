package com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.model


data class SuperHeroCombat(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val damageAbsMax: Int? = null,
    var attack: Int? = null,
    var defense: Int? = null,
    var life: Int? = null,
    var damageAbs: Int? = null,
    var damagePenance: Int? = null,
    var healingPotion: Int? = null,
)

data class CombatArena(
    val superHero1: SuperHeroCombat? = null,
    val superHero2: SuperHeroCombat? = null
)