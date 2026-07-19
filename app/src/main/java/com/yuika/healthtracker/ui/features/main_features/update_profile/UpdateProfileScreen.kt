package com.yuika.healthtracker.ui.features.main_features.update_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.features.main_features.update_profile.components.AvatarEditor
import com.yuika.healthtracker.ui.features.main_features.update_profile.components.UpdateProfileForm
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun UpdateProfileScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: UpdateProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.onIntent(UpdateProfileIntent.LoadProfile)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is UpdateProfileEffect.ShowSuccess -> {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }
                    is UpdateProfileEffect.NavigateBack -> {
                        onBackClick()
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = spacing.large)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Spacer(modifier = Modifier.height(16.dp))

            AvatarEditor(onAvatarClick = onAvatarClick)
            
            Spacer(modifier = Modifier.height(32.dp))

            if (state.errorMessage != null){
                ErrorText(state.errorMessage!!)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.isSuccess && !state.isLoading && !state.isSaving && state.errorMessage == null) {
                SuccessText("Profile updated")
            }

            if (state.isLoading) {
                LoadingIndicator()
            } else {
                UpdateProfileForm(
                    state = state,
                    onIntent = { viewModel.onIntent(it) }
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.onIntent(UpdateProfileIntent.SaveProfile) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        LoadingIndicator()
                    } else {
                        Text(
                            text = "Save changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
    }
}
