package com.yuika.healthtracker.ui.features.main_features.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yuika.healthtracker.ui.core.components.StatCard
import java.io.File

@Composable
fun ProfileHeaderCard(
    modifier: Modifier = Modifier,
    name: String = "Yuika",
    subtitle: String = "Pro vip",
    weight: String = "65 kg",
    height: String = "170 cm",
    bmi: String = "22.5",
    avatarPath: String? = null,
    onAvatarClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar with Edit Icon
        Box(
            modifier = Modifier
                .size(88.dp)
                .clickable(onClick = onAvatarClick)
        ) {
            // Mock Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                val avatarModel = avatarPath?.takeIf { it.isNotBlank() }?.let {
                    if (it.startsWith("http", ignoreCase = true)) it else File(it)
                }

                if (avatarModel == null){
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    AsyncImage(
                        model = avatarModel,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            // Edit Badge
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .align(Alignment.BottomEnd)
                    .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Name and Subtitle
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatCard(
                title = "WEIGHT",
                value = weight,
                unit = null,
                valueColor = MaterialTheme.colorScheme.onBackground,
                labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                showContainer = false
            )

            HorizontalDivider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )
            
            StatCard(
                title = "HEIGHT",
                value = height,
                unit = null,
                valueColor = MaterialTheme.colorScheme.onBackground,
                labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                showContainer = false
            )

            HorizontalDivider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )

            StatCard(
                title = "BMI",
                value = bmi,
                unit = null,
                valueColor = MaterialTheme.colorScheme.secondary,
                labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                showContainer = false
            )
        }
    }
}
