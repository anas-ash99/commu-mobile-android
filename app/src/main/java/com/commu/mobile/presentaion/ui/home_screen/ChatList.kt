package com.commu.mobile.presentaion.ui.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commu.mobile.R
import com.commu.mobile.data.db.Data.loggedInUser
import com.commu.mobile.model.Chat
import com.commu.mobile.model.Message
import com.commu.mobile.model.User
import com.commu.mobile.presentaion.state.HomeState
import com.commu.mobile.ui.theme.AppColor
import com.commu.mobile.ui.theme.ReadIndoctrinator
import com.commu.mobile.utils.getTimeForChatItem

@Composable
fun ChatList(
    lastMessages: List<Message>,
    chats: List<Chat>,
    users: List<User>,
    onChatClick: (String, String) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(lastMessages) { message ->
            val chat = chats.firstOrNull { it.id == message.chatId } ?: Chat()
            val user =
                users.firstOrNull { it.id == chat.users.firstOrNull { userId -> userId != loggedInUser!!.id } }
                    ?: User()
            ChatItem(
                chat = chat,
                lastMessage = message,
                user = user,
                onChatClick = { onChatClick(chat.id, user.id) }
            )
        }
    }

}

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    chat: Chat,
    lastMessage: Message,
    user: User,
    onChatClick: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp),
        onClick = onChatClick
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.blank_profile_picture),
                contentDescription = "Circular Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop // Crop the image to fit
            )
            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (lastMessage.authorUserId == loggedInUser!!.id) {
                        if (lastMessage.seenAt == null || lastMessage.seenAt!!.isBlank()) {
                            Icon(
                                Icons.Filled.Check,
                                "",
                                Modifier.size(18.dp),
                                tint = Color(0xBF343434)
                            )

                        } else {
                            Icon(
                                Icons.Filled.DoneAll,
                                "",
                                Modifier.size(18.dp),
                                tint = ReadIndoctrinator
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(
                        lastMessage.content,
                        maxLines = 1,
                        color = Color(0xBF343434),
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
            Spacer(Modifier.width(5.dp))
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            )

            {
                Text(
                    lastMessage.createdAt.getTimeForChatItem(),
                    color = if (lastMessage.seenAt == null && lastMessage.authorUserId != loggedInUser!!.id) AppColor else Color(
                        0xA1000000
                    )
                )
                if (chat.unseenMessagesCount > 0 && lastMessage.authorUserId != loggedInUser!!.id)
                    Card(
                        shape = CircleShape,
                        modifier = Modifier
                            .size(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF009688))
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = chat.unseenMessagesCount.toString(),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
            }

        }
    }

}