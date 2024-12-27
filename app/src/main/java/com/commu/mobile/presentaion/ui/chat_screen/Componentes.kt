package com.commu.mobile.presentaion.ui.chat_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.commu.mobile.R
import com.commu.mobile.model.Message
import com.commu.mobile.model.User
import com.commu.mobile.presentaion.events.ChatScreenEvents
import com.commu.mobile.presentaion.state.ChatScreenState
import com.commu.mobile.ui.theme.AppColor
import com.commu.mobile.ui.theme.ReadIndoctrinator
import com.commu.mobile.utils.getDayTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppTopBar(user: User, onAction: (ChatScreenEvents) -> Unit, navController: NavController) {

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAction(ChatScreenEvents.OnUserHeaderClick)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blank_profile_picture),
                    contentDescription = "Circular Image",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ){
                    Text(user.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    AnimatedVisibility(visible = user.isOnline) {
                        Text("Online", fontSize = 15.sp, color = Color(0XFF40d160))
                    }
                }
            }

        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Search")
            }
        },

        actions = {
            IconButton(onClick = { /* Handle search action */ }) {
                Icon(painter = painterResource(id = R.drawable.phone), contentDescription = "phone", Modifier.size(30.dp))
            }
        }
    )

}


@Composable
fun MessageItem(message: Message, loggedUserId:String, onAction: (ChatScreenEvents) -> Unit) {
    LaunchedEffect (Unit){
        if (message.seenAt == null){
            onAction(ChatScreenEvents.OnMessageSeen(message))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
    ) {
        val backgroundColor = if (message.authorUserId == loggedUserId) AppColor else Color.White
        val alignment = if (message.authorUserId == loggedUserId) Alignment.CenterEnd else Alignment.CenterStart
        val textColor = if (message.authorUserId == loggedUserId) Color.White else Color.Black

        Row(
            modifier = Modifier
                .align(alignment)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .clickable {  },
            verticalAlignment = Alignment.Bottom
        ) {
            Spacer(Modifier.width(10.dp))
            Text(
                text = message.content,
                fontSize = 15.sp,
                color = textColor,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .weight(1f, fill = false)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = message.createdAt.getDayTime(),
                fontSize = 12.sp,
                color = textColor,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )
            if (message.authorUserId == loggedUserId){
                Spacer(Modifier.width(2.dp))
                if (message.deliveredAt == null){
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        "message pending",
                        tint = Color.White,
                        modifier = Modifier.padding(bottom = 5.dp).size(13.dp)
                    )
                }
                else if (message.seenAt == null){
                    Icon(
                        Icons.Default.Check,
                        "message delivered",
                        tint = Color.White,
                        modifier = Modifier.padding(bottom = 5.dp).size(18.dp)
                    )
                }else{
                    Icon(
                       imageVector = Icons.Filled.DoneAll,
                        "message read",
                        tint = ReadIndoctrinator,
                        modifier = Modifier.padding(bottom = 4.dp).size(18.dp)
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
        }
    }
}
@Composable
fun ChatBottomBar(state: ChatScreenState, onAction: (ChatScreenEvents) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button (
                onClick = {  },
                modifier = Modifier
                    .size(50.dp),
                shape = CircleShape,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = AppColor,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "add", Modifier.size(30.dp))
            }
            Spacer(Modifier.width(10.dp))
            MessageTextField(state.messageContent, state.trailingIcon, onAction)
        }
    }
}

@Composable
fun MessageTextField(message:TextFieldValue, trailingIcon:Int, onAction: (ChatScreenEvents) -> Unit) {
    TextField(
        value = message,
        onValueChange = { onAction(ChatScreenEvents.TypeMessage(it)) },
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .fillMaxWidth()
            .background(Color(0xFFf2f3f5)),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            cursorColor = AppColor,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ) ,
        placeholder = {
            Text("Type here", color = Color(0xFFa7a8b3))
        },
        maxLines = 10,
        trailingIcon = {
            IconButton(onClick = {
                onAction(ChatScreenEvents.OnSendClick)
            }) {
                Icon(
                    painter = painterResource(trailingIcon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "mic"
                )
            }
        },
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
    )
}

@Composable
fun DateLabel(modifier: Modifier = Modifier, date:String){
    Box(
        modifier
            .padding(top = 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x57020202))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(date, modifier = Modifier.align(Alignment.Center), color = Color.White)
    }

}