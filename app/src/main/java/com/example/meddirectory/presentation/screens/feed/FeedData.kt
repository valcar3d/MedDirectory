package com.example.meddirectory.presentation.screens.feed

import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.common.SalaryStats

data class FeedData(
    val items: List<FeedItem>,
    val salaryStats: SalaryStats?
)