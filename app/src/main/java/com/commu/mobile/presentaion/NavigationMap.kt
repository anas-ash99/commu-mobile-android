package com.commu.mobile.presentaion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.commu.mobile.presentaion.ui.chat_screen.ChatScreen
import com.commu.mobile.presentaion.ui.home_screen.HomeScreen
import com.commu.mobile.presentaion.ui.login_screen.LoginScreen
import com.commu.mobile.presentaion.viewmodel.UserViewModel
import com.commu.mobile.shared.AppScreen

@Composable
fun NavigationMap( lifecycleOwner: LifecycleOwner) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = if (userViewModel.loggedUser == null) AppScreen.LOGIN_SCREEN.name else AppScreen.HOME_SCREEN.name
    ) {
        composable(
            route = AppScreen.HOME_SCREEN.name,
        ) {
            HomeScreen(navController, lifecycleOwner)
        }
        composable(
            route = AppScreen.LOGIN_SCREEN.name,

            ) {
            LoginScreen(navController, userViewModel)
        }

        composable(
            route = AppScreen.CHAT_SCREEN.name + "/{chatId}/{otherUserId}",
        ) {
//            val chatId = backStackEntry.arguments?.getString("chatId")
//            val otherUserId = backStackEntry.arguments?.getString("otherUserId")
            ChatScreen(navController)
        }

    }

}


