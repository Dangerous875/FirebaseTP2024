package com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.model.CombatArena
import com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.model.SuperHeroCombat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealTimeDBViewModel @Inject constructor(private val database: FirebaseDatabase) :
    ViewModel() {

    private var _superHero1 = MutableStateFlow<SuperHeroCombat?>(null)
    val superHero1 = _superHero1.asStateFlow()
    private var _superHero2 = MutableStateFlow<SuperHeroCombat?>(null)
    val superHero2 = _superHero2.asStateFlow()
    private var _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _player1Attack = MutableStateFlow(false)
    val player1Attack = _player1Attack.asStateFlow()
    private val _player2Attack = MutableStateFlow(false)
    val player2Attack = _player2Attack.asStateFlow()

    init {
        getCombatArena()
    }

    private fun getCombatArena() {
        viewModelScope.launch {
            collectCombatArena().collect { snapshot ->
                val combatArena = snapshot.getValue(CombatArena::class.java)
                _superHero1.value = combatArena!!.superHero1!!  // Assign new instance
                _superHero2.value = combatArena.superHero2!!  // Assign new instance
                if (_superHero1.value != null) {
                    Log.i(
                        "KlyxFirestore",
                        "charge arena ${_superHero1.value!!.life} && ${_superHero2.value!!.life}"
                    )
                    _isLoading.value = false
                }

            }
        }

    }


    private fun collectCombatArena(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
                Log.i("KlyxFirestore", "Carga exitosa")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("KlyxFirestore", "Error al cargar CombatArena: ${error.message}")
                close(error.toException())
            }
        }
        val ref = database.reference.child("CombatArena") // Referencia a CombatArena en Firebase
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun attackSuperHero1() {
        _player1Attack.value = true
        viewModelScope.launch {
            val superHero1 = _superHero1.value
            val superHero2 = _superHero2.value
            val newLife = (superHero2!!.life!!) - (superHero1!!.attack!!)
            superHero2.life = newLife
            delay(1000)
            val playerRef = database.reference.child("CombatArena/superHero2")
            playerRef.setValue(superHero2)
            delay(1000)
            _player1Attack.value = false
        }
    }

    fun attackSuperHero2() {
        _player2Attack.value = true
        viewModelScope.launch {
            val superHero1 = _superHero1.value
            val superHero2 = _superHero2.value
            val newLife = (superHero1!!.life!!) - (superHero2!!.attack!!)
            superHero1.life = newLife
            delay(1000)
            val playerRef = database.reference.child("CombatArena/superHero1")
            playerRef.setValue(superHero1)
            _player2Attack.value = false
        }
    }

    fun resetBattle() {
        _isLoading.value = true
        viewModelScope.launch {
            val superHero1 = _superHero1.value!!.copy(life = 1000)
            val superHero2 = _superHero2.value!!.copy(life = 1000)
            val player1 = database.reference.child("CombatArena/superHero1")
            player1.setValue(superHero1)
            val player2 = database.reference.child("CombatArena/superHero2")
            player2.setValue(superHero2)
            delay(5000)
            _isLoading.value = false
        }
    }

}