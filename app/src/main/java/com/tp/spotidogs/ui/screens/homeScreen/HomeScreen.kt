package com.tp.spotidogs.ui.screens.homeScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.tp.spotidogs.R
import com.tp.spotidogs.data.local.OrientationScreen
import com.tp.spotidogs.data.navigation.FireStoreRoute
import com.tp.spotidogs.data.navigation.LoginScreenRoute
import com.tp.spotidogs.data.navigation.MainScreenRoute
import com.tp.spotidogs.ui.components.ExitConfirmation
import com.tp.spotidogs.ui.components.SetOrientationScreen
import com.tp.spotidogs.ui.components.ShowLoading
import com.tp.spotidogs.ui.screens.homeScreen.viewmodel.HomeScreenViewModel
import com.tp.spotidogs.ui.theme.Green

@Composable
fun HomeScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val isLoading by viewModel.isLoading.collectAsState()
    val allBreeds by viewModel.allBreeds.collectAsState()
    val context = LocalContext.current
    var showExitConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    SetOrientationScreen(context = context, orientation = OrientationScreen.PORTRAIT.orientation)

    if (isLoading) {
        ShowLoading()
    } else {
        ShowContent(allBreeds, viewModel, navController, auth)
    }

    if (showExitConfirmation) {
        ExitConfirmation(
            show = showExitConfirmation,
            onDismiss = { showExitConfirmation = false },
            onConfirm = {
                val activity = context as Activity
                activity.finishAffinity()
            },
            title = "Do you want to exit app ? ",
            message = "Exit Confirmation"
        )
    }

    BackHandler {
        showExitConfirmation = true
    }


}

@Composable
fun ShowContent(
    allBreeds: List<String>,
    viewModel: HomeScreenViewModel,
    navController: NavHostController,
    auth: FirebaseAuth
) {
    TopBarHome(navController, auth)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 46.dp)
            .background(Green), contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Razas disponibles ", fontSize = 24.sp, color = Color.White)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Green), contentPadding = PaddingValues(4.dp)
            ) {
                items(allBreeds) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clickable {
                                viewModel.setSelectBreed(it)
                                navController.navigate(MainScreenRoute)
                            },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(16.dp),
                        colors = CardColors(
                            contentColor = Color.Black,
                            containerColor = Green,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.White
                        ), border = BorderStroke(2.dp, Color.Black)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_dogtwo),
                                    contentDescription = "decoration"
                                )
                                Text(
                                    text = it,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_dogtwo),
                                    contentDescription = "decoration"
                                )
                            }


                        }
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(navController: NavHostController, auth: FirebaseAuth) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    var logOutConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    ExitConfirmation(
        show = logOutConfirmation,
        onDismiss = { logOutConfirmation = false },
        onConfirm = {
            auth.signOut()
            navController.navigate(LoginScreenRoute) {
                popUpTo(MainScreenRoute) { inclusive = true }
            }
        },
        title = "Do you want LOG OUT ? ",
        message = "Log out confirmation"
    )
    TopAppBar(
        modifier = Modifier.height(48.dp),
        title = {
            Text(
                text = " SearchDogsApp ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Start,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Black),
        actions = {
            Text(
                text = "Favorites",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.clickable { navController.navigate(FireStoreRoute) })
            IconButton(onClick = { navController.navigate(FireStoreRoute) }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            IconButton(onClick = { setExpanded(true) }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {}
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                logOutConfirmation = true
                            }
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Text(
                            text = "Log Out",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }

                }
            }
        }
    )
}

