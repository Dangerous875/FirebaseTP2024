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
fun RealTimeDBScreen(navController: NavHostController,viewModel: RealTimeDBViewModel = hiltViewModel()) {
    val player by viewModel.player.collectAsState()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), contentAlignment = Alignment.Center
        ) {
            Button(onClick = {  }) {
                Text(text = "Añadir artista")
            }
        }

        if (player == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "EmptyList")
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .height(500.dp)
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
                                Text(text = player!!.artists!!.name!!)
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(text = player!!.artists!!.image!!)
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(text = player!!.artists!!.description!!)
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(text = player!!.play!!.toString())
                                Button(onClick = { }) {
                                    Text(text = "Update Number BD")

                                }
                            }
                        }

                    }


            }

        }
    }

}