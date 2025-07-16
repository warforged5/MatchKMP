package io.github.warforged5.mashkmp.components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import io.github.warforged5.mashkmp.dataclasses.MashTemplate
import io.github.warforged5.mashkmp.enumclasses.MashType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateItem(
    template: MashTemplate,
    index: Int,
    onSelect: () -> Unit
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = index * 50,
            easing = FastOutSlowInEasing
        )
    )

    Card(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(animatedAlpha),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = when (template.type) {
                    MashType.CLASSIC -> MaterialTheme.colorScheme.primary
                    MashType.HYBRID -> MaterialTheme.colorScheme.secondary
                    MashType.CUSTOM -> MaterialTheme.colorScheme.tertiary
                }.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        when (template.type) {
                            MashType.CLASSIC -> Icons.Rounded.Home
                            MashType.HYBRID -> Icons.Rounded.Shuffle
                            MashType.CUSTOM -> Icons.Rounded.Build
                        },
                        contentDescription = null,
                        tint = when (template.type) {
                            MashType.CLASSIC -> MaterialTheme.colorScheme.primary
                            MashType.HYBRID -> MaterialTheme.colorScheme.secondary
                            MashType.CUSTOM -> MaterialTheme.colorScheme.tertiary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    template.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${template.categories.size} categories",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}