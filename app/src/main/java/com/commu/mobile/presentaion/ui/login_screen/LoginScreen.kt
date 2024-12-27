package com.commu.mobile.presentaion.ui.login_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.commu.mobile.presentaion.viewmodel.UserViewModel
import com.commu.mobile.shared.AppScreen

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel){
    val users by userViewModel.allUsers.collectAsState(emptyList())
    Scaffold { innerPadding ->
        Column (
            Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Spacer(Modifier.height(20.dp))
            Text("Please choose a user to counties as:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp), fontSize = 20.sp)
            users.onEach { user ->
                Box(
                    Modifier.fillMaxWidth().clickable {
                        userViewModel.saveUser(user)
                        navController.navigate(AppScreen.HOME_SCREEN.name) {
                            popUpTo(AppScreen.LOGIN_SCREEN.name) { inclusive = true }
                        }
                    }.padding(10.dp)
                ){
                    Text(user.name)
                }
            }


        }
    }








}
