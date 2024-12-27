package com.commu.mobile.presentaion.ui.chat_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.commu.mobile.data.db.Data.loggedInUser
import com.commu.mobile.model.User
import com.commu.mobile.presentaion.events.ChatScreenEvents
import com.commu.mobile.presentaion.viewmodel.ChatViewModel
import com.commu.mobile.utils.getDateForChat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    navController: NavController,
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val state by viewModel.state.collectAsState()
    val messages by viewModel.messages.collectAsState(emptyList())
    val user by viewModel.userChattingWith.collectAsState(User())

    val listState = rememberLazyListState()

    var isScrolling by remember { mutableStateOf(false) }


    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.scrollToItem(messages.lastIndex)
        }
    }

    // Observe scroll state
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collectLatest { scrolling ->
                isScrolling = scrolling
                if (!scrolling) {
                    println("User stopped scrolling!")
                    viewModel.onEvent(ChatScreenEvents.OnStopScrolling)
                }
            }
    }
    // Collect scrolling updates
    LaunchedEffect(listState.firstVisibleItemIndex) {
        viewModel.onEvent(ChatScreenEvents.OnScroll(listState.firstVisibleItemIndex))
    }
    LaunchedEffect(state.scrollPosition) {
        val index = state.messages.lastIndex
        if (index > 0) {
            listState.scrollToItem(state.scrollPosition)
        }
    }
    Scaffold(
        topBar = {
            ChatAppTopBar(user ?: User(), viewModel::onEvent, navController)
        },
        bottomBar = {
            ChatBottomBar(state, viewModel::onEvent)
        }
    ) { innerPadding ->

        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .background(Color(0xFFf3f3f3))
                    .fillMaxSize()
            ) {
                itemsIndexed(messages) { index , message ->
                    val date = message.createdAt.getDateForChat()
                    if (index == 0 || (index > 0 && messages[index - 1].createdAt.getDateForChat() != date)){
                        Box {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                DateLabel(modifier = Modifier.align(Alignment.Center), date = date)
                            }
                        }
                    }
                    MessageItem(message, loggedInUser!!.id, viewModel::onEvent)
                }
            }

            AnimatedVisibility(
                visible = state.isDateLabelVisible,
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                DateLabel(date = state.dateLabelText)
            }

        }

    }

}







