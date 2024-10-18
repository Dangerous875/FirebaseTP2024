package com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tp.spotidogs.ui.screens.firestore.realtimeDBScreen.viewmodel.RealTimeDBViewModel

@Composable
fun RealTimeDBScreen(
    navController: NavHostController,
    viewModel: RealTimeDBViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val playerAttack by viewModel.player1Attack.collectAsState()
    val player2Attack by viewModel.player2Attack.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), contentAlignment = Alignment.Center
            ) {
                Button(onClick = { viewModel.resetBattle() }) {
                    Text(text = "Reset Battle")
                }
            }

            ContentView(viewModel = viewModel, playerAttack = playerAttack, player2Attack)

        }
    }
}

@Composable
fun ContentView(viewModel: RealTimeDBViewModel, playerAttack: Boolean, player2Attack: Boolean) {
    val superHero1 by viewModel.superHero1.collectAsState()
    val superHero2 by viewModel.superHero2.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = getMessage(playerAttack, player2Attack))

        Card(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .padding(vertical = 6.dp)
                .clickable { },
            border = BorderStroke(4.dp, Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "Name : ${superHero1!!.name!!}")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Image : ${superHero1!!.image!!}")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Attack : ${superHero1!!.attack!!}")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Life : ${superHero1!!.life!!}")
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = { viewModel.attackSuperHero1() }) {
                        Text(text = "Attack")

                    }
                }
            }

        }
        Spacer(modifier = Modifier.size(30.dp))
        Text(text = getMessage(player2Attack, playerAttack))
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .padding(vertical = 6.dp)
                .clickable { },
            border = BorderStroke(4.dp, Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "Name : ${superHero2!!.name!!}")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Image : ${superHero2!!.image!!}")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Attack : ${superHero2!!.attack!!}")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "Life : ${superHero2!!.life!!}")
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = { viewModel.attackSuperHero2() }) {
                        Text(text = "Attack")

                    }
                }
            }

        }


    }
}

fun getMessage(p1: Boolean, p2: Boolean): String {
    return when {
        p1 -> "Jugador atacando"
        p2 -> "Jugador defendiendo"
        else -> "Descanso"
    }
}
