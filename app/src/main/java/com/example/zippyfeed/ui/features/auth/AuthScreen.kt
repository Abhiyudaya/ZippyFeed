package com.example.zippyfeed.ui.features.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zippyfeed.R
import com.example.zippyfeed.ui.GroupSocialButtons
import com.example.zippyfeed.ui.features.auth.signup.SignUpViewModel
import com.example.zippyfeed.ui.navigation.Home
import com.example.zippyfeed.ui.navigation.Login
import com.example.zippyfeed.ui.navigation.SignUp
import com.example.zippyfeed.ui.theme.Orange
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AuthScreen(navController : NavController,viewModel: AuthScreenViewModel = hiltViewModel()) {

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.Black
        ),
        startY = 300f
    )

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when(event){
                is AuthScreenViewModel.AuthInNavigationEvent.NavigationToHome -> {
                    navController.navigate(Home){
                        popUpTo(com.example.zippyfeed.ui.navigation.AuthScreen){
                            inclusive = true
                        }
                    }
                }
                is AuthScreenViewModel.AuthInNavigationEvent.NavigationToSignUp -> {
                    navController.navigate(SignUp)
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush)
        )

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.skip), color = Orange)
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                color = Color.Black,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.app_name),
                color = Orange,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.zippy_feed_description),
                color = Color.DarkGray,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GroupSocialButtons(viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(SignUp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text(text = stringResource(R.string.sign_with_email), color = Color.White)
            }

            TextButton(
                onClick = {
                    navController.navigate(Login)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    color = Color.White
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(rememberNavController())
}
