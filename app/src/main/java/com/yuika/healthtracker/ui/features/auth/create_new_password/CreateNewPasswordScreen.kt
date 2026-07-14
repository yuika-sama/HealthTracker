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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
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

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect)
                {
                    is CreateNewPasswordEffect.NavigateToPasswordChanged -> onResetPasswordClick()
                    is CreateNewPasswordEffect.NavigateToLogin -> onBackToLoginClick()
                    is CreateNewPasswordEffect.ShowToast ->
                    {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
                        state = state,
                        onIntent = viewModel::onIntent
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
