package com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealTimeDBViewModel @Inject constructor(private val database: FirebaseDatabase) :
    ViewModel() {

    private val _player = MutableStateFlow<Player?>(null)
    val player = _player.asStateFlow()

    init {
        getPlayer()
    }

    private fun getPlayer() {
        viewModelScope.launch {
            collectPlayer().collect {
                val player = it.getValue(Player::class.java)
                _player.value = player
                Log.i("KlyxFirestore", player.toString())
            }
        }
    }

    private fun collectPlayer(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }

            override fun onCancelled(snapshot: DatabaseError) {
                Log.i("KlyxFirestore", "Error de carga ${snapshot.message}")
                close()
            }

        }
        val ref = database.reference.child("Player")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }

    }


}