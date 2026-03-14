package com.example.meddirectory.data.repository

import com.example.meddirectory.common.AppError
import com.example.meddirectory.common.AppErrorException
import com.example.meddirectory.common.NotFoundException
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toAppError(): AppError = when (this) {
    is AppErrorException -> error
    is NotFoundException -> AppError.NotFoundError
    is IOException -> AppError.NetworkError
    is HttpException -> AppError.ServerError(code(), response()?.errorBody()?.string())
    else -> AppError.UnknownError(message)
}

fun <T> Result<T>.toAppResult(): Result<T> =
    exceptionOrNull()?.let { Result.failure(AppErrorException(it.toAppError())) } ?: this