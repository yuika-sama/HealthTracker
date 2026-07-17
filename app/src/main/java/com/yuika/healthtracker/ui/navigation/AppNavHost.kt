package com.yuika.healthtracker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yuika.healthtracker.ui.core.components.LoadingIndicator

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startViewModel: AppStartViewModel = hiltViewModel()
){
    var isNavigationLoading by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val appNavigator = remember(navController, scope) {
        AppNavigator(navController, scope) { isNavigationLoading = it }
    }
    val startRoute by startViewModel.startRoute.collectAsStateWithLifecycle()

    Box(modifier.fillMaxSize()){
        if (startRoute == null) {
            LoadingIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Scaffold(
                modifier = modifier
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startRoute!!,
                    modifier = Modifier.padding(innerPadding)
                ){
                    onboardingNavGraph(appNavigator = appNavigator)
                    mainNavGraph(appNavigator = appNavigator)
                }
            }
        }

        if (isNavigationLoading){
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha = 0.72f)),
                contentAlignment = Alignment.Center
            ){
                LoadingIndicator()
            }
        }
    }
}
