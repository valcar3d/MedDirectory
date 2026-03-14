package com.example.meddirectory.presentation.screens.detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.meddirectory.presentation.common.parseSalaryRange
import com.example.meddirectory.ui.theme.MedDirectoryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    item: FeedItem,
    onNavigateBack: () -> Unit,
    imageLoader: ImageLoader,
    salaryStats: SalaryStats?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.detail_title))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        ItemDetail(
            item = item,
            imageLoader = imageLoader,
            salaryStats = salaryStats,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun ItemDetail(
    item: FeedItem,
    imageLoader: ImageLoader,
    salaryStats: SalaryStats?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DoctorAvatarLarge(
                    item = item,
                    imageLoader = imageLoader
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item.formattedName(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = item.specialty,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = stringResource(R.string.location),
                    value = item.location,
                    contentDescription = stringResource(R.string.cd_location_icon)
                )
                item.npi?.let { npi ->
                    DetailRow(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.npi),
                        value = npi,
                        contentDescription = stringResource(R.string.cd_person_icon)
                    )
                }
                item.salaryRange?.let { salaryRange ->
                    SalaryProgressDetail(
                        label = stringResource(R.string.salary_range),
                        value = salaryRange,
                        salaryStats = salaryStats
                    )
                }
                AcceptingPatientsDetail(accepting = item.acceptingNewPatients)
            }
        }
    }
}

@Composable
private fun DoctorAvatarLarge(
    item: FeedItem,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
) {
    val avatarUrl = remember(item.id) { item.getAvatarUrl() }

    Box(
        modifier = modifier
            .size(120.dp)
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
private fun SalaryProgressDetail(
    label: String,
    value: String,
    salaryStats: SalaryStats?,
    modifier: Modifier = Modifier
) {
    val percentage = salaryStats?.let { stats ->
        val salaryRangePair = value.parseSalaryRange()
        salaryRangePair?.let { (min, max) ->
            if (stats.range > 0) {
                ((max - stats.min).toFloat() / stats.range * 100).toInt().coerceIn(0, 100)
            } else 0
        } ?: 0
    } ?: 0
    val color = percentage.getSalaryColor()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .height(12.dp)
                    .background(color)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$percentage% of salary range",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

data class PatientStatusData(
    val icon: ImageVector,
    val statusColor: Color,
    val labelText: String,
    val valueText: String,
    val contentDescription: String
)

@Composable
private fun AcceptingPatientsDetail(
    accepting: Boolean,
    modifier: Modifier = Modifier
) {
    val statusData = if (accepting) {
        PatientStatusData(
            icon = Icons.Default.CheckCircle,
            statusColor = MaterialTheme.colorScheme.primary,
            labelText = stringResource(R.string.accepting_new_patients),
            valueText = stringResource(R.string.yes),
            contentDescription = stringResource(R.string.cd_accepting_icon)
        )
    } else {
        PatientStatusData(
            icon = Icons.Default.Close,
            statusColor = MaterialTheme.colorScheme.error,
            labelText = stringResource(R.string.accepting_new_patients),
            valueText = stringResource(R.string.no),
            contentDescription = stringResource(R.string.cd_not_accepting_icon)
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = statusData.icon,
            contentDescription = statusData.contentDescription,
            tint = statusData.statusColor,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = statusData.labelText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = statusData.valueText,
                style = MaterialTheme.typography.bodyLarge,
                color = statusData.statusColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.cd_detail_icon)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    MedDirectoryTheme {
        DetailScreen(
            item = PreviewData.mockFeedItems[0],
            onNavigateBack = {},
            imageLoader = PreviewData.mockImageLoader(),
            salaryStats = PreviewData.mockSalaryStats
        )
    }
}