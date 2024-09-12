package com.tp.spotidogs.ui.screens.loginScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.tp.spotidogs.R
import com.tp.spotidogs.data.navigation.AuthenticationScreenRoute
import com.tp.spotidogs.data.navigation.HomeScreenRoute
import com.tp.spotidogs.data.navigation.LoginScreenRoute
import com.tp.spotidogs.data.navigation.RegisterScreenRoute
import com.tp.spotidogs.ui.theme.Black
import com.tp.spotidogs.ui.theme.Gray
import com.tp.spotidogs.ui.theme.Green

@Composable
fun LoginScreen(navController: NavHostController, auth: FirebaseAuth) {
    val activity = LocalContext.current as Activity

    LaunchedEffect(auth) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate(HomeScreenRoute) {
                popUpTo(LoginScreenRoute) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Gray, Black), startY = 0f, endY = 600f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.clip(CircleShape),
            painter = painterResource(id = R.drawable.iv_spotify),
            contentDescription = null
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.HomeTittle),
            color = Color.White,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.FreeString),
            color = Color.White,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp),
            onClick = { navController.navigate(RegisterScreenRoute) },
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            Text(text = stringResource(id = R.string.SignUpFree), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.size(8.dp))

        ButtonLogin(R.drawable.ic_google, R.string.Google) {/*TODO*/ }

        Spacer(modifier = Modifier.size(8.dp))

        ButtonLogin(R.drawable.ic_facebook, R.string.Facebook) {throw RuntimeException("Explotar") }

        Spacer(modifier = Modifier.size(24.dp))

        Text(
            modifier = Modifier.clickable { navController.navigate(AuthenticationScreenRoute) },
            text = "Log in",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

    }

    BackHandler {
        activity.finish()
    }

}

@Composable
fun ButtonLogin(image: Int, message: Int, onClick: () -> Unit) {
    OutlinedButton(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .height(60.dp), onClick = { onClick() }) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(24.dp),
                alignment = Alignment.CenterStart,
                painter = painterResource(id = image),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp),
                text = stringResource(id = message),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}
