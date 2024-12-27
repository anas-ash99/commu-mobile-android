package com.commu.mobile.presentaion.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.commu.mobile.model.User
import com.commu.mobile.presentaion.events.HomeScreenEvents
import com.commu.mobile.presentaion.viewmodel.HomeViewModel
import com.commu.mobile.shared.AppScreen

@Composable
fun HomeScreen(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val state by homeViewModel.state.collectAsState()
    val chats by homeViewModel.chats.collectAsState(emptyList())
    val lastMessages by homeViewModel.lastMessages.collectAsState(emptyList())
    val friends by homeViewModel.friends.collectAsState(emptyList())
    TrackUserActivity(lifecycleOwner) {
        homeViewModel.onEvent(HomeScreenEvents.OnLifecycleChange(it))
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                onClick = { homeViewModel.onEvent(HomeScreenEvents.RequestDialog(true)) },
                shape = CircleShape,
                containerColor = Color(0xFF009688),
                contentColor = Color.White
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Check Action",
                    Modifier.size(30.dp)
                )
            }
        }
    ) { innerPadding ->
        if (state.isChatsLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Loading ..")
            }

        } else {
            Text(state.uiTrigger.toString(), Modifier.size(0.dp))
            Column(
                Modifier.padding(innerPadding)
            ) {
                Header(homeViewModel::onEvent)
                MainTabs()
                ChatList(
                    lastMessages,
                    chats,
                    friends,
                ) { chatId, userId ->
                    navController.navigate(AppScreen.CHAT_SCREEN.name + "/$chatId/$userId")
                }
                if (state.isUserListDialogShown) {
                    UsersDialog(
                        users = friends,
                        onDismiss = {
                            homeViewModel.onEvent(HomeScreenEvents.RequestDialog(false))
                        },
                        onUserClick = { user ->
                            navController.navigate(AppScreen.CHAT_SCREEN.name + "/null/${user.id}")
                            homeViewModel.onEvent(HomeScreenEvents.RequestDialog(false))
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun UsersDialog(
    onUserClick: (User) -> Unit,
    onDismiss: () -> Unit,
    users: List<User>
) {
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .background(Color.White)
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "Please choose a user to chat with:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 18.sp
                )
                users.onEach {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUserClick(it)
                            }
                            .padding(10.dp)
                    ) {
                        Text(it.name)
                    }
                }

            }
        }

    )
}


