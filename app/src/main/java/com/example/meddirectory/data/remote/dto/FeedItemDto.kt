package com.example.meddirectory.data.remote.dto

import com.example.meddirectory.domain.model.FeedItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("suffix")
    val suffix: String? = null,
    @SerialName("specialty")
    val specialty: String,
    @SerialName("npi")
    val npi: String? = null,
    @SerialName("location")
    val location: LocationDto,
    @SerialName("salary_range")
    val salaryRange: String? = null,
    @SerialName("accepting_new_patients")
    val acceptingNewPatients: Boolean
) {
    fun toDomain(): FeedItem = FeedItem(
        id = id.toString(),
        firstName = firstName,
        lastName = lastName,
        suffix = suffix,
        specialty = specialty,
        npi = npi,
        location = "${location.city}, ${location.state}",
        salaryRange = salaryRange,
        acceptingNewPatients = acceptingNewPatients
    )
}

@Serializable
data class LocationDto(
    @SerialName("city")
    val city: String,
    @SerialName("state")
    val state: String
)