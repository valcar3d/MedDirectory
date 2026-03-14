package com.example.meddirectory.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedItem(
    val id: String,
    val firstName: String,
    val lastName: String,
    val suffix: String? = null,
    val specialty: String,
    val npi: String? = null,
    val location: String,
    val salaryRange: String? = null,
    val acceptingNewPatients: Boolean
)
