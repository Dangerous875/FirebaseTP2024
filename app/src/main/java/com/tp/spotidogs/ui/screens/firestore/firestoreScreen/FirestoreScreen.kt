package com.tp.spotidogs.ui.screens.firestore.firestoreScreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tp.spotidogs.ui.screens.firestore.firestoreScreen.viewmodel.FireStoreViewModel

@Composable
fun FireStoreScreen(
    navController: NavHostController,
    viewModel: FireStoreViewModel = hiltViewModel()
) {
    val artists by viewModel.list.collectAsState()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), contentAlignment = Alignment.Center
        ) {
            Button(onClick = { viewModel.createArtist() }) {
                Text(text = "Añadir artista")
            }
        }

        if (artists.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "EmptyList")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(artists) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .height(200.dp)
                            .padding(vertical = 6.dp)
                            .clickable { viewModel.deleteArtist(it) },
                        border = BorderStroke(4.dp, Color.Black)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(text = it.name!!)
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(text = it.number.toString())
                                Button(onClick = { viewModel.updateNumberInBD(it) }) {
                                    Text(text = "Update Number BD")

                                }
                            }
                        }

                    }
                }

            }

        }
    }
}



