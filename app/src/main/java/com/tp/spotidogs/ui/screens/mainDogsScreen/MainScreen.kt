package com.tp.spotidogs.ui.screens.mainDogsScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tp.spotidogs.data.local.OrientationScreen
import com.tp.spotidogs.data.navigation.HomeScreenRoute
import com.tp.spotidogs.data.navigation.ZoomScreenRoute
import com.tp.spotidogs.domain.model.Dog
import com.tp.spotidogs.ui.components.SetOrientationScreen
import com.tp.spotidogs.ui.components.ShowLoading
import com.tp.spotidogs.ui.components.TopActionBar
import com.tp.spotidogs.ui.screens.mainDogsScreen.viewmodel.MainScreenViewModel
import com.tp.spotidogs.ui.theme.Black
import com.tp.spotidogs.ui.theme.Green

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainScreenViewModel = hiltViewModel()) {
    val context = LocalContext.current
    SetOrientationScreen(context = context, orientation = OrientationScreen.PORTRAIT.orientation)
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val allBreeds by viewModel.allBreeds.collectAsState()
    SetOrientationScreen(context = context, orientation = OrientationScreen.PORTRAIT.orientation)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        val (idTopBar, idContentView) = createRefs()
        Box(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(idTopBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(idContentView.top)
            }) { TopActionBar(navController) { showDialog = it } }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Green)
                .constrainAs(idContentView) {
                    top.linkTo(idTopBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
            ContentView(navController, viewModel)
        }
    }

    if (showDialog) {
        ShowAlertDialog(viewModel, allBreeds) { showDialog = it }
    }

    BackHandler {
        navController.navigate(HomeScreenRoute) {
            popUpTo<HomeScreenRoute> { inclusive = true }
        }
    }
}

@Composable
fun ContentView(navController: NavHostController, viewModel: MainScreenViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val dogList by viewModel.lisDogs.collectAsState()

    if (isLoading) {
        ShowLoading()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(dogList) { dog ->
                DogCard(dog = dog, navController, viewModel)
            }
        }
    }
}

@Composable
fun DogCard(dog: Dog, navController: NavHostController, viewModel: MainScreenViewModel) {
    val isFavorite = remember { mutableStateOf(dog.iconFavorite) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .height(250.dp)
            .clickable {
                navController.navigate(ZoomScreenRoute(dog.imageUrl))
            },
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
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                onClick = {
                    isFavorite.value = !isFavorite.value
                    dog.iconFavorite = isFavorite.value
                    if (dog.iconFavorite) {
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                        viewModel.insertDog(dog)
                    } else {
                        Toast.makeText(context, "Deleted to favorites", Toast.LENGTH_SHORT).show()
                        viewModel.deleteFromDB(dog)
                    }
                }
            ) {
                Icon(
                    imageVector = if (isFavorite.value) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun ShowAlertDialog(
    viewModel: MainScreenViewModel,
    allBreeds: List<String>,
    onChangeDialog: (Boolean) -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .height(600.dp)
            .background(color = Color(0xFF42A8F8), shape = RoundedCornerShape(0.dp)),
        containerColor = Color(0xFF81C6FC),
        onDismissRequest = { onChangeDialog(false) },
        title = { Text(text = "Select an breed", fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                allBreeds.forEach { breed ->
                    Text(
                        text = breed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.getDogsByBreeds(breed)
                                onChangeDialog(false)
                            }
                            .padding(16.dp), fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().size(4.dp), color = Color.Black)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeDialog(false)
                },
                colors = ButtonColors(
                    containerColor = Color(0xFF42A8F8),
                    contentColor = Color.Black,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.White
                )
            ) {
                Text(text = "Close", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    )
}






























