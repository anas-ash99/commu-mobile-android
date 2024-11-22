package com.commu.mobile.presentaion.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commu.mobile.R
import com.commu.mobile.model.Message
import com.commu.mobile.model.User
import com.commu.mobile.presentaion.viewmodel.HomeViewModel
import com.commu.mobile.utils.getTimeForChatItem

@Composable
fun ChatBody(viewModel: HomeViewModel) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(viewModel.getChats()) { chat ->
            ChatItem(lastMessage = viewModel.getLastMessage(chat.id)!!, user = viewModel.getUser(chat.users), unReadMessages = viewModel.getUnreadMessagesCount(chat.id) )
        }
    }

}

@Composable
fun ChatItem(modifier: Modifier = Modifier, lastMessage: Message, user: User, unReadMessages:Int) {
    Row(
        modifier = modifier.padding(10.dp).height(55.dp),
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
            Spacer(Modifier.height(5.dp))
            Text(
                lastMessage.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                )
        }
        Spacer(Modifier.width(5.dp))
        Column(
            Modifier.fillMaxHeight().padding(vertical = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        )

        {
            Text(lastMessage.createdAt.getTimeForChatItem(), color = if (lastMessage.seenAt == null) Color(0xFF009688) else Color.Black)
            if (unReadMessages != 0)
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
                        text = unReadMessages.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }


    }


}