package com.rmd.business.blogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.SignInClient
import com.rmd.business.blogapp.ui.screen.SignInScreen
import com.rmd.business.blogapp.ui.screen.vm.UserViewModel
import com.rmd.business.blogapp.ui.theme.BlogAppTheme
import com.rmd.business.blogapp.ui.utils.GoogleAuthUiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var googleAuthUiHelper: GoogleAuthUiHelper

    @Inject
    lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlogAppTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val userViewModel: UserViewModel = hiltViewModel()
                    val uiState = userViewModel.uiState.value
                    NavHost(navController = navController, startDestination = "signIn") {
                        composable("signIn") {
                            val launcher =
                                rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiHelper.getSignInResultFromIntentAndSignIn(
                                                        intent = result.data ?: return@launch
                                                    )
                                                userViewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    })

                            LaunchedEffect(key1 = Unit) {
                                if (uiState.currentUser != null) {
                                    navController.navigate("home")
                                }
                            }

                            LaunchedEffect(key1 = uiState.isSignInSuccessfull) {
                                if (uiState.isSignInSuccessfull) {
                                    navController.navigate("home")
                                    userViewModel.resetSignInState()
                                }
                            }

                            SignInScreen(
                                isLoading = uiState.isLoading,
                                currentUser = uiState.currentUser,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiHelper.actionSignIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}