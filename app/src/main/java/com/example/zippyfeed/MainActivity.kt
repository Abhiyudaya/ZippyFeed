package com.example.zippyfeed

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.ui.features.auth.AuthScreen
import com.example.zippyfeed.ui.features.auth.login.SignInScreen
import com.example.zippyfeed.ui.features.auth.signup.SignUpScreen
import com.example.zippyfeed.ui.navigation.AuthScreen
import com.example.zippyfeed.ui.navigation.Home
import com.example.zippyfeed.ui.navigation.Login
import com.example.zippyfeed.ui.navigation.SignUp
import com.example.zippyfeed.ui.theme.ZippyFeedTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showSplashScreen = true

    @Inject
    lateinit var foodApi: FoodApi
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0f
                )
                zoomY.duration = 500
                zoomX.duration = 500
                zoomX.interpolator = OvershootInterpolator()
                zoomY.interpolator = OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZippyFeedTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = AuthScreen,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        composable<SignUp>() {
                            SignUpScreen(navController)
                        }
                        composable<AuthScreen> {
                            AuthScreen(navController)
                        }
                        composable<Login> (
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                    animationSpec = tween(300)
                                ) + fadeIn(animationSpec = tween(300))
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = tween(300)
                                ) + fadeIn(animationSpec = tween(300))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                            }
                        ) {
                            SignInScreen(navController)
                        }
                        composable<Home> {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red)) {

                            }
                        }
                    }
                }
            }
        }

        if (::foodApi.isInitialized) {
            Log.d("MainActivity", "FoodApi initialized")
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            showSplashScreen = false
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZippyFeedTheme {
        Greeting("Android")
    }
}