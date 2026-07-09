package com.yuika.healthtracker.ui.features.auth.create_new_password

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.glance.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuika.healthtracker.ui.core.components.AuthHeader
import com.yuika.healthtracker.ui.features.auth.create_new_password.components.CreateNewPasswordForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun CreateNewPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateNewPasswordViewModel = hiltViewModel(),
    onBackToLoginClick: () -> Unit = {},
    onResetPasswordClick: () -> Unit = { }
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect)
            {
                is CreateNewPasswordEffect.NavigateToPasswordChanged -> onResetPasswordClick()
                is CreateNewPasswordEffect.NavigateToLogin -> onBackToLoginClick()
                is CreateNewPasswordEffect.ShowToast ->
                {
                    // show toast
                }
            }
        }
    }

    LaunchedEffect(state.value.errorMessage) {
        state.value.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.large),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.large)
            ) {
                AuthHeader(
                    title = "Create New Password",
                    subtitle = "Your new password must be different from previously used passwords.",
                    icon = Icons.Outlined.LockReset,
                    iconShape = RoundedCornerShape(16.dp),
                    iconContainerSize = 56.dp,
                    iconSize = 28.dp
                )

                Box(modifier = Modifier.padding(top = 32.dp)) {
                    CreateNewPasswordForm(
                        newPassword = state.value.newPassword,
                        newPasswordErr = state.value.newPasswordError,
                        isShowNewPassword = state.value.isShowNewPassword,
                        confirmPassword = state.value.confirmNewPassword,
                        confirmPasswordErr = state.value.confirmNewPasswordError,
                        isShowConfirmPassword = state.value.isShowConfirmNewPassword,
                        onNewPasswordChange = {
                            viewModel.onIntent(
                                CreateNewPasswordIntent.NewPasswordChanged(
                                    it
                                )
                            )
                        },
                        onConfirmPasswordChange = {
                            viewModel.onIntent(
                                CreateNewPasswordIntent.ConfirmNewPasswordChanged(
                                    it
                                )
                            )
                        },
                        onShowNewPassword = { viewModel.onIntent(CreateNewPasswordIntent.ShowNewPassword) },
                        onShowConfirmPassword = { viewModel.onIntent(CreateNewPasswordIntent.ShowConfirmNewPassword) },
                        onResetPasswordClick = { viewModel.onIntent(CreateNewPasswordIntent.ResetPasswordClick) },
                        isLoading = state.value.isLoading,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun CreateNewPasswordScreenPreview()
{
    CreateNewPasswordScreen()
}
