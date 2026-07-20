package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.core.components.BasicInputField
import com.yuika.healthtracker.ui.core.components.DateOfBirthInput
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SegmentedSelector
import com.yuika.healthtracker.ui.core.i18n.genderLabel
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.OnboardingField
import com.yuika.healthtracker.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun OnboardingPage1Screen(
    viewModel: OnboardingPage1ViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onNavigateBack: () -> Unit = {},
    onNavigateNext: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is OnboardingPage1Effect.NavigateToPage2 -> onNavigateNext()
                    is OnboardingPage1Effect.NavigateBack -> onNavigateBack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.onboarding_step, 1, 4),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.onboarding_personal_details),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.onboarding_lets_get_to_know),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = stringResource(R.string.onboarding_profile_help),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(28.dp))

        OnboardingField(
            icon = Icons.Outlined.Person,
            label = stringResource(R.string.update_profile_full_name)
        ) {
            BasicInputField(
                value = state.name, 
                onValueChange = { viewModel.onIntent(OnboardingPage1Intent.NameChanged(it)) },
                isError = state.nameError != null
            )
            state.nameError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(it)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        OnboardingField(
            icon = Icons.Outlined.CalendarToday,
            label = stringResource(R.string.update_profile_date_of_birth)
        ) {
            DateOfBirthInput(
                value = state.dateOfBirth,
                isError = state.dateOfBirthError != null,
                onDateSelected = { viewModel.onIntent(OnboardingPage1Intent.DateOfBirthChanged(it)) }
            )
            state.dateOfBirthError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(it)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        OnboardingField(
            icon = Icons.Default.People,
            label = stringResource(R.string.update_profile_gender)
        ) {
            val genders = listOf("Male", "Female")
            SegmentedSelector(
                options = genders,
                selectedOption = state.gender,
                onOptionSelected = { viewModel.onIntent(OnboardingPage1Intent.GenderChanged(it)) },
                labelProvider = { genderLabel(it) }
            )
            state.genderError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(it)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                OnboardingField(icon = Icons.Default.MonitorWeight, label = stringResource(R.string.stat_weight)) {
                    BasicInputField(
                        value = state.weight,
                        onValueChange = { viewModel.onIntent(OnboardingPage1Intent.WeightChanged(it)) },
                        suffix = "kg",
                        isError = state.weightError != null
                    )
                    state.weightError?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorText(it)
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                OnboardingField(icon = Icons.Default.Height, label = stringResource(R.string.stat_height)) {
                    BasicInputField(
                        value = state.height,
                        onValueChange = { viewModel.onIntent(OnboardingPage1Intent.HeightChanged(it)) },
                        suffix = "cm",
                        isError = state.heightError != null
                    )
                    state.heightError?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorText(it)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, OutlineLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Emerald
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.onboarding_bmr_tdee_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.onboarding_vitality_active),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        state.errorMessage?.let {
            ErrorText(it)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { viewModel.onIntent(OnboardingPage1Intent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                enabled = !state.isLoading
            ) {
                if(state.isLoading){
                    LoadingIndicator()
                } else {
                    Text(stringResource(R.string.action_continue), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
