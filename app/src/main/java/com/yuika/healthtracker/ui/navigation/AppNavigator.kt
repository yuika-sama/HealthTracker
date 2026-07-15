package com.yuika.healthtracker.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.yuika.healthtracker.utils.UI_LOADING_DELAY_MS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppNavigator(
    private val navController: NavHostController,
    private val scope: CoroutineScope,
    private val setLoading: (Boolean) -> Unit
)
{
    private var job: Job? = null

    fun navigate(route: Route, builder: NavOptionsBuilder.() -> Unit = {}){
        if (job?.isActive == true) return
        job = scope.launch {
            setLoading(true)
            delay(UI_LOADING_DELAY_MS)
            navController.navigate(route){builder()}
            setLoading(false)
        }
    }

    fun popBackStack(){
        if (job?.isActive == true) return
        job = scope.launch {
            setLoading(true)
            delay(UI_LOADING_DELAY_MS)
            navController.popBackStack()
            setLoading(false)
        }
    }
}
