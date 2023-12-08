package com.rmd.business.blogapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rmd.business.blogapp.R
import com.rmd.business.blogapp.domain.model.Blog
import com.rmd.business.blogapp.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: User?,
    blogs: List<Blog>,
    isLoading: Boolean,
    onSignOut: () -> Unit,
    onNavigateToBlogDetailsScreen: (Blog) -> Unit,
    onNavigateToUpdateBlogScreen: () -> Unit,
    onNavigateToSignInScreen: () -> Unit
) {

    var isDropDownMenuExpanded by remember {
        mutableStateOf(false)
    }

    var query by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = currentUser) {
        if (currentUser == null) {
            onNavigateToSignInScreen()
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "BlogApp", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
            },

            actions = {

                AsyncImage(model = currentUser?.profilePictureUrl,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_account),
                    error = painterResource(id = R.drawable.ic_account),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.padding(8.dp).size(40.dp).clip(CircleShape)
                        .clickable {
                        isDropDownMenuExpanded = !isDropDownMenuExpanded
                    })

                DropdownMenu(expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false }) {
                    DropdownMenuItem(text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "${currentUser?.username}")
                            Text(text = "Logout")
                        }
                    }, onClick = {
                        onSignOut()
                        isDropDownMenuExpanded = false
                    })
                }
            })
    }, floatingActionButton = {
        FloatingActionButton(onClick = onNavigateToUpdateBlogScreen) {
            Icon(imageVector = Icons.Filled.Create, contentDescription = null)
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {}
    }
}