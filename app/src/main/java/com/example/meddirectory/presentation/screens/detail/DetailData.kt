package com.example.meddirectory.presentation.screens.detail

import com.example.meddirectory.domain.model.FeedItem
import com.example.meddirectory.presentation.common.SalaryStats

data class DetailData(
    val item: FeedItem,
    val salaryStats: SalaryStats?
)