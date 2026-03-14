package com.example.meddirectory.presentation.screens.feed.components


import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.example.meddirectory.R
import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.PreviewData
import com.example.meddirectory.presentation.common.SalaryStats
import com.example.meddirectory.presentation.common.formattedName
import com.example.meddirectory.presentation.common.getAvatarUrl
import com.example.meddirectory.presentation.common.getSalaryColor
import com.example.meddirectory.presentation.common.getSalaryPercentage
import com.example.meddirectory.ui.theme.MedDirectoryTheme

@Composable
fun FeedItemCard(
    item: FeedItem,
    salaryStats: SalaryStats?,
    imageLoader: ImageLoader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val salaryPercentage = remember(item.salaryRange, salaryStats) {
        salaryStats?.let { item.getSalaryPercentage(it) } ?: 0
    }
    val salaryColor = remember(salaryPercentage) { salaryPercentage.getSalaryColor() }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DoctorAvatar(
                    item = item,
                    imageLoader = imageLoader
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.formattedName(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = item.specialty,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = item.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AcceptingPatientsIndicator(accepting = item.acceptingNewPatients)
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (salaryStats != null && item.salaryRange != null) {
                SalaryProgressBar(
                    salaryRange = item.salaryRange,
                    salaryPercentage = salaryPercentage,
                    salaryColor = salaryColor
                )
            }
        }
    }
}

@Composable
private fun SalaryProgressBar(
    salaryRange: String,
    salaryPercentage: Int,
    salaryColor: Color
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(salaryPercentage / 100f)
                    .height(4.dp)
                    .background(salaryColor)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = salaryRange,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DoctorAvatar(
    item: FeedItem,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val avatarUrl = remember(item.id) { item.getAvatarUrl() }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = stringResource(R.string.cd_provider_avatar, item.formattedName()),
            imageLoader = imageLoader,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun AcceptingPatientsIndicator(
    accepting: Boolean,
    modifier: Modifier = Modifier
) {
    val indicatorData = if (accepting) {
        IndicatorData(
            icon = Icons.Default.CheckCircle,
            contentDescription = stringResource(R.string.cd_accepting_icon),
            text = stringResource(R.string.accepting),
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        IndicatorData(
            icon = Icons.Default.Close,
            contentDescription = stringResource(R.string.cd_not_accepting_icon),
            text = stringResource(R.string.not_accepting),
            color = MaterialTheme.colorScheme.error
        )
    }

    StatusIndicator(
        data = indicatorData,
        modifier = modifier
    )
}

data class IndicatorData(
    val icon: ImageVector,
    val contentDescription: String,
    val text: String,
    val color: Color
)

@Composable
private fun StatusIndicator(
    data: IndicatorData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = data.contentDescription,
            tint = data.color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = data.text,
            style = MaterialTheme.typography.labelSmall,
            color = data.color
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedItemCardHighSalaryPreview() {
    MedDirectoryTheme {
        FeedItemCard(
            item = PreviewData.mockFeedItemHighSalary,
            salaryStats = PreviewData.mockSalaryStats,
            imageLoader = PreviewData.mockImageLoader(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedItemCardLowSalaryPreview() {
    MedDirectoryTheme {
        FeedItemCard(
            item = PreviewData.mockFeedItemLowSalary,
            salaryStats = PreviewData.mockSalaryStats,
            imageLoader = PreviewData.mockImageLoader(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedItemCardNoSalaryPreview() {
    MedDirectoryTheme {
        FeedItemCard(
            item = PreviewData.mockFeedItemNoSalary,
            salaryStats = PreviewData.mockSalaryStats,
            imageLoader = PreviewData.mockImageLoader(),
            onClick = {}
        )
    }
}
