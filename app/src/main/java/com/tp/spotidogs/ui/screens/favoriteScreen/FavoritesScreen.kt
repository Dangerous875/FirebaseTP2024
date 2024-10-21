package com.tp.spotidogs.ui.screens.favoriteScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tp.spotidogs.data.navigation.MainScreenRoute
import com.tp.spotidogs.domain.model.Dog
import com.tp.spotidogs.ui.components.ShowLoading
import com.tp.spotidogs.ui.screens.favoriteScreen.viewmodel.FavoriteScreenViewModel
import com.tp.spotidogs.ui.theme.Black
import com.tp.spotidogs.ui.theme.Green

@Composable
fun FavoritesScreen(
    navHostController: NavHostController,
    viewModel: FavoriteScreenViewModel = hiltViewModel()
) {

    val dogList by viewModel.dogList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    if (isLoading) {
        ShowLoading()
    } else {
        TopBar(navHostController, dogList) { showDialog.value = it }
        if (dogList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 46.dp)
                    .background(
                        color = Green,
                        shape = RoundedCornerShape(0.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "EmptyList", fontWeight = FontWeight.Bold, fontSize = 34.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 42.dp)
                    .background(color = Green, shape = RoundedCornerShape(0.dp))
            ) {
                items(dogList) { dog ->
                    CardDog(dog, viewModel)
                }
            }
        }

        if (showDialog.value) {
            ShowDialog(viewModel, context) { showDialog.value = it }
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navHostController: NavHostController, dogList: List<Dog>, onDelete: (Boolean) -> Unit) {
    TopAppBar(
        modifier = Modifier.height(48.dp),
        title = {
            Text(
                text = "Favorite breeds RoomDB",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Start,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navHostController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Black),
        actions = {
            IconButton(
                onClick = {
                    onDelete(true)
                },
                enabled = dogList.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }


        }
    )
}

@Composable
fun ShowDialog(viewModel: FavoriteScreenViewModel, context: Context, onDismiss: (Boolean) -> Unit) {
    AlertDialog(
        modifier = Modifier.background(color = Color(0xFF42A8F8), shape = RoundedCornerShape(0.dp)),
        containerColor = Color(0xFF81C6FC),
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = "Select an option", fontWeight = FontWeight.Bold) },
        text = { Text(text = " Are you sure you want to delete everything?") },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteDB()
                    onDismiss(false)
                    Toast.makeText(
                        context,
                        "All your favorites have been deleted",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                },
                colors = ButtonColors(
                    containerColor = Color(0xFF42A8F8),
                    contentColor = Color.Black,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White
                )
            ) {
                Text(text = "Confirm action", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss(false) },
                colors = ButtonColors(
                    containerColor = Color(0xFF42A8F8),
                    contentColor = Color.Black,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White
                )
            ) {
                Text(text = "Cancel action", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun CardDog(dog: Dog, viewModel: FavoriteScreenViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .height(250.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ), border = BorderStroke(4.dp, Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(dog.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Icon(
                imageVector = getIconF(dog),
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(
                        Alignment.BottomStart
                    )
                    .padding(16.dp), tint = Color.Red
            )
            IconButton(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), onClick = {
                viewModel.deleteDogFromDB(dog)
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "deleteIcon",
                    tint = Color.White
                )
            }
        }

    }
}


fun getIconF(dog: Dog): ImageVector {
    return if (dog.iconFavorite) {
        Icons.Default.Favorite
    } else {
        Icons.Default.FavoriteBorder
    }
}