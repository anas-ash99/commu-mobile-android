package com.commu.mobile.presentaion.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commu.mobile.presentaion.viewmodel.HomeViewModel


@Composable
fun HomeScreen() {
    var viewModel: HomeViewModel = hiltViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                onClick = { },
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
        Column(
            Modifier.padding(innerPadding)
        ) {
            Header { }
            MainTabs()
            ChatBody(viewModel)
        }

    }
}



