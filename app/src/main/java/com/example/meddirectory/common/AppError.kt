package com.example.meddirectory.common

sealed class AppError {
    data object NetworkError : AppError()
    data object NotFoundError : AppError()
    data class ServerError(val code: Int, val message: String?) : AppError()
    data class UnknownError(val message: String?) : AppError()
}

class NotFoundException : Exception()
class AppErrorException(val error: AppError) : Exception()
