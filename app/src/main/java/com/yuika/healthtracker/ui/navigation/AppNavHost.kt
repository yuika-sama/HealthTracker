package com.yuika.healthtracker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.navigation.components.AppBottomBar
import com.yuika.healthtracker.ui.navigation.components.AppTopBar

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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val barsState = appBarsStateFor(backStackEntry?.destination?.route)
    val onTabClick: (String) -> Unit = remember(appNavigator) {
        { tab ->
            val targetRoute = when (tab) {
                "home" -> Route.Dashboard
                "diary" -> Route.Diary
                "activity" -> Route.Activity
                "trends" -> Route.Trends
                "profile" -> Route.Profile
                else -> Route.Dashboard
            }

            appNavigator.navigate(targetRoute) {
                popUpTo(Route.Dashboard) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Box(modifier.fillMaxSize()){
        if (startRoute == null) {
            LoadingIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    AppTopBar(
                        showMainBar = barsState.mainTab != null,
                        title = barsState.title,
                        showBackButton = barsState.showBackButton,
                        onBackClick = { appNavigator.popBackStack() }
                    )
                },
                bottomBar = {
                    AppBottomBar(
                        currentTab = barsState.mainTab,
                        onTabClick = onTabClick
                    )
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startRoute!!,
                    modifier = Modifier.fillMaxSize()
                ){
                    onboardingNavGraph(
                        appNavigator = appNavigator,
                        contentPadding = innerPadding
                    )
                    mainNavGraph(
                        appNavigator = appNavigator,
                        contentPadding = innerPadding
                    )
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
