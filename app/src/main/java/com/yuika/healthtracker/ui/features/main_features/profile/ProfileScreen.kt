package com.yuika.healthtracker.ui.features.main_features.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.features.main_features.profile.components.CurrentGoalBanner
import com.yuika.healthtracker.ui.features.main_features.profile.components.ProfileHeaderCard
import com.yuika.healthtracker.ui.features.main_features.profile.components.SettingsGroup
import com.yuika.healthtracker.ui.features.main_features.profile.components.SettingsItem
import com.yuika.healthtracker.ui.theme.InfoBlue
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onTabClick: (String) -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileIntent.LoadProfile)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is ProfileEffect.ShowError -> {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }
                    is ProfileEffect.NavigateToLogin -> {
                        onLogoutClick()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "profile", onTabClick = onTabClick)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text(
                    text = "Profile & settings",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Config your information & your experiences.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
            
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    LoadingIndicator()
                }
            } else {
                ProfileHeaderCard(
                    name = state.name,
                    subtitle = state.subtitle,
                    weight = state.weight,
                    height = state.height,
                    bmi = state.bmi
                )
                
                CurrentGoalBanner(
                    title = state.goalTitle,
                    description = state.goalDescription
                )
            }
            
            SettingsGroup(title = "ACCOUNT") {
                SettingsItem(
                    icon = Icons.Outlined.PersonOutline,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = "Edit Profile",
                    subtitle = "Name, Weight, Height, Activities, Goals",
                    showDivider = false,
                    onClick = onEditProfileClick
                )
            }
            
            SettingsGroup(title = "CONFIG DISPLAY") {
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = "Language",
                    value = "Vietnamese"
                )
                
                SettingsItem(
                    icon = Icons.Outlined.DarkMode,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = "Theme",
                    value = "System"
                )
                
                SettingsItem(
                    icon = Icons.Outlined.TextFields,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = "Font size",
                    value = "Normal",
                    showDivider = false
                )
            }
            
            OutlinedButton(
                onClick = { viewModel.onIntent(ProfileIntent.Logout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, Color(0xFFE53935)), // Red
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFE53935), // Red
                    containerColor = Color.Transparent
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

