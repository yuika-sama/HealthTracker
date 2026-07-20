package com.yuika.healthtracker.ui.features.main_features.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.ui.core.components.AvatarSourceDialog
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.core.i18n.activityLevelTitle
import com.yuika.healthtracker.ui.core.i18n.appLanguageLabel
import com.yuika.healthtracker.ui.core.i18n.bmiCategoryLabel
import com.yuika.healthtracker.ui.core.i18n.fontSizeLabel
import com.yuika.healthtracker.ui.core.i18n.goalTitle
import com.yuika.healthtracker.ui.core.i18n.themeColorLabel
import com.yuika.healthtracker.ui.core.i18n.themeModeLabel
import com.yuika.healthtracker.ui.features.main_features.profile.components.CurrentGoalBanner
import com.yuika.healthtracker.ui.features.main_features.profile.components.NotificationSwitchItem
import com.yuika.healthtracker.ui.features.main_features.profile.components.ProfileHeaderCard
import com.yuika.healthtracker.ui.features.main_features.profile.components.SettingsChoiceDialog
import com.yuika.healthtracker.ui.features.main_features.profile.components.SettingsGroup
import com.yuika.healthtracker.ui.features.main_features.profile.components.SettingsItem
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.utils.copyAvatarToAppStorage

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var showAvatarDialog by rememberSaveable { mutableStateOf(false) }
    var avatarDraft by rememberSaveable { mutableStateOf("") }
    val dailyNotificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted)
        {
            viewModel.onIntent(ProfileIntent.ChangeNotificationEnabled(true))
        }
    }
    val testNotificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted)
        {
            viewModel.onIntent(ProfileIntent.ChangeTestNotificationEnabled(true))
        }
    }
    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            runCatching { copyAvatarToAppStorage(context, uri) }
                .onSuccess { avatarDraft = it }
                .onFailure { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
        }
    }
    val needsNotificationPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
    val onNotificationToggle: (Boolean) -> Unit = { enabled ->
        if (enabled && needsNotificationPermission)
        {
            dailyNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        else
        {
            viewModel.onIntent(ProfileIntent.ChangeNotificationEnabled(enabled))
        }
    }
    val onTestNotificationToggle: (Boolean) -> Unit = { enabled ->
        if (enabled && needsNotificationPermission)
        {
            testNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        else
        {
            viewModel.onIntent(ProfileIntent.ChangeTestNotificationEnabled(enabled))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileIntent.LoadProfile)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect)
                {
                    is ProfileEffect.NavigateToEditProfile ->
                    {
                        onEditProfileClick()
                    }
                }
            }
        }
    }

    if (showAvatarDialog) {
        AvatarSourceDialog(
            avatarValue = avatarDraft,
            onAvatarValueChange = { avatarDraft = it },
            onPickLocal = { avatarPicker.launch("image/*") },
            onDismiss = { showAvatarDialog = false },
            onSave = {
                viewModel.onIntent(ProfileIntent.SaveAvatar(it))
                showAvatarDialog = false
            }
        )
    }

    state.activeSettingsDialog?.let { dialog ->
        when (dialog)
        {
            ProfileSettingsDialog.LANGUAGE -> SettingsChoiceDialog(
                title = stringResource(R.string.profile_language),
                options = AppLanguage.entries,
                selectedOption = state.language,
                labelProvider = { appLanguageLabel(it) },
                onSelect = { viewModel.onIntent(ProfileIntent.ChangeLanguage(it)) },
                onDismiss = { viewModel.onIntent(ProfileIntent.DismissSettingsDialog) }
            )

            ProfileSettingsDialog.THEME_MODE -> SettingsChoiceDialog(
                title = stringResource(R.string.profile_theme),
                options = ThemeMode.entries,
                selectedOption = state.themeMode,
                labelProvider = { themeModeLabel(it) },
                onSelect = { viewModel.onIntent(ProfileIntent.ChangeThemeMode(it)) },
                onDismiss = { viewModel.onIntent(ProfileIntent.DismissSettingsDialog) }
            )

            ProfileSettingsDialog.THEME_COLOR -> SettingsChoiceDialog(
                title = stringResource(R.string.profile_theme_color),
                options = ThemeColorPreset.entries,
                selectedOption = state.themeColorPreset,
                labelProvider = { themeColorLabel(it) },
                onSelect = { viewModel.onIntent(ProfileIntent.ChangeThemeColor(it)) },
                onDismiss = { viewModel.onIntent(ProfileIntent.DismissSettingsDialog) }
            )

            ProfileSettingsDialog.FONT_SIZE -> SettingsChoiceDialog(
                title = stringResource(R.string.profile_font_size),
                options = AppFontSize.entries,
                selectedOption = state.fontSize,
                labelProvider = { fontSizeLabel(it) },
                onSelect = { viewModel.onIntent(ProfileIntent.ChangeFontSize(it)) },
                onDismiss = { viewModel.onIntent(ProfileIntent.DismissSettingsDialog) }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = spacing.large)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text(
                    text = stringResource(R.string.profile_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.profile_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            if (state.errorMessage != null)
            {
                ErrorText(state.errorMessage!!)
            }

            if (state.isSuccess && !state.isLoading && state.errorMessage == null)
            {
                SuccessText(stringResource(R.string.profile_loaded))
            }

            if (state.isLoading)
            {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }
            else
            {
                ProfileHeaderCard(
                    name = state.name,
                    subtitle = activityLevelTitle(state.activityLevel),
                    weight = state.weight,
                    height = state.height,
                    bmi = "${state.bmi} (${bmiCategoryLabel(state.bmiCategory)})",
                    avatarPath = state.avatarPath,
                    onAvatarClick = {
                        avatarDraft = state.avatarPath.orEmpty()
                        showAvatarDialog = true
                    }
                )

                CurrentGoalBanner(
                    title = stringResource(R.string.profile_current_goal),
                    description = stringResource(
                        R.string.profile_goal_description,
                        goalTitle(state.goal),
                        state.goalCalories
                    )
                )
            }

            SettingsGroup(title = stringResource(R.string.profile_group_profile)) {
                SettingsItem(
                    icon = Icons.Outlined.PersonOutline,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = stringResource(R.string.profile_edit_profile),
                    subtitle = stringResource(R.string.profile_edit_profile_subtitle),
                    showDivider = false,
                    onClick = { viewModel.onIntent(ProfileIntent.EditProfile) }
                )
            }

            SettingsGroup(title = stringResource(R.string.profile_config_display)) {
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = stringResource(R.string.profile_language),
                    value = appLanguageLabel(state.language),
                    onClick = { viewModel.onIntent(ProfileIntent.LanguageClick) }
                )

                SettingsItem(
                    icon = Icons.Outlined.DarkMode,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = stringResource(R.string.profile_theme),
                    value = themeModeLabel(state.themeMode),
                    onClick = { viewModel.onIntent(ProfileIntent.ThemeModeClick) }
                )

                SettingsItem(
                    icon = Icons.Outlined.Palette,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = stringResource(R.string.profile_theme_color),
                    value = themeColorLabel(state.themeColorPreset),
                    onClick = { viewModel.onIntent(ProfileIntent.ThemeColorClick) }
                )

                SettingsItem(
                    icon = Icons.Outlined.TextFields,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    iconTintColor = MaterialTheme.colorScheme.tertiary,
                    title = stringResource(R.string.profile_font_size),
                    value = fontSizeLabel(state.fontSize),
                    onClick = { viewModel.onIntent(ProfileIntent.FontSizeClick) }
                )
            }

            SettingsGroup(title = stringResource(R.string.profile_reminders)) {
                NotificationSwitchItem(
                    title = stringResource(R.string.profile_diary_reminders),
                    subtitle = stringResource(R.string.profile_diary_reminders_subtitle),
                    checked = state.notificationEnabled,
                    onCheckedChange = onNotificationToggle
                )
                NotificationSwitchItem(
                    title = stringResource(R.string.profile_test_reminders),
                    subtitle = stringResource(R.string.profile_test_reminders_subtitle),
                    checked = state.testNotificationEnabled,
                    onCheckedChange = onTestNotificationToggle
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
    }
}
