package com.commu.mobile.presentaion.ui.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.commu.mobile.R
import com.commu.mobile.presentaion.events.ChatScreenEvents
import com.commu.mobile.presentaion.events.HomeScreenEvents

@Composable
fun MainTabs() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Chats", "Calls")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = Color.Black // Indicator color
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (index == 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.chats),
                                "chat icon",
                                Modifier
                                    .size(25.dp)
                                    .padding(end = 2.dp)
                            )
                        } else {
                            Icon(painter = painterResource(id = R.drawable.phone), contentDescription = "phone", Modifier.size(25.dp))
                        }
                        Spacer(Modifier.width(5.dp))
                        Text(title, fontSize = 17.sp)
                    }
                }
            )
        }
    }

}



@Composable
fun Header(onEvent:(HomeScreenEvents) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(Icons.Default.Menu, "menu")
        }
        Spacer(Modifier.width(5.dp))
        Text("Commu", fontSize = 20.sp, color = Color(0xC1000000))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {}
        ) {
            Icon(Icons.Default.Search, "menu")
        }
    }

}



@Composable
fun TrackUserActivity(lifecycleOwner: LifecycleOwner, onStateChange: (Lifecycle.Event) -> Unit) {
    val lifecycle = lifecycleOwner.lifecycle

    // Observe lifecycle changes
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            onStateChange(event)
//            when (event) {
//                Lifecycle.Event.ON_START -> onStateChange("App Resumed")
//                Lifecycle.Event.ON_STOP -> onStateChange("App Backgrounded")
//                Lifecycle.Event.ON_DESTROY -> onStateChange("App Exited")
//                else -> { /* No action */ }
//            }
        }

        lifecycle.addObserver(observer)

        // Clean up when the Composable is removed
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

