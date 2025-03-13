package com.github.nullsafe.watchwise.compose.components.rating

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.nullsafe.watchwise.compose.theme.AppTheme
import java.math.BigDecimal
import java.math.RoundingMode

private fun Double.fiveStarRating(): Double = this / 2

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    numStars: Int = 5
) {
    val lowRange = 0.1..5.0
    val normalRange = 5.1..6.9
    val highRange = 7.0..10.0

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = BigDecimal(rating).setScale(1, RoundingMode.HALF_UP).toString(),
            style = AppTheme.typography.bodyMedium,
            color = when (rating) {
                in lowRange -> AppTheme.colors.background.alert
                in highRange -> AppTheme.colors.background.success
                in normalRange -> AppTheme.colors.type.disable
                else -> AppTheme.colors.type.disable
            },
            modifier = Modifier.padding(end = AppTheme.dimens.spaceXS)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            val fiveStarRating = rating.fiveStarRating()
            val fullStars = fiveStarRating.toInt()
            val hasHalfStar = (fiveStarRating - fullStars) >= 0.5

            repeat(numStars) { index ->
                when {
                    index < fullStars -> {
                        // Fully filled star
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimens.starSize),
                            tint = AppTheme.colors.theme.tint
                        )
                    }
                    index == fullStars && hasHalfStar -> {
                        // Half star
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.StarHalf,
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimens.starSize),
                            tint = AppTheme.colors.theme.tint
                        )
                    }
                    else -> {
                        // Empty star
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(AppTheme.dimens.starSize),
                            tint = AppTheme.colors.theme.tintGhost
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode RatingBar Preview"
)
@Composable
fun PreviewRatingBar() {
    AppTheme(darkTheme = true) { // Replace with your custom theme if needed
        Box(modifier = Modifier.padding(16.dp)) {
            RatingBar(
                rating = 7.2, // Sample rating value
                modifier = Modifier.padding(8.dp),
                numStars = 5
            )
        }
    }
}