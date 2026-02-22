package com.example.demo.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException, request: HttpServletRequest): ResponseEntity<ApiErrorResponse> {
        return errorResponse(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid request", request.requestURI)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ApiErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        return errorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = "Validation failed",
            path = request.requestURI,
            details = fieldErrors
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidBody(ex: HttpMessageNotReadableException, request: HttpServletRequest): ResponseEntity<ApiErrorResponse> {
        return errorResponse(HttpStatus.BAD_REQUEST, "Malformed request body", request.requestURI)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: HttpServletRequest): ResponseEntity<ApiErrorResponse> {
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request.requestURI)
    }

    private fun errorResponse(
        status: HttpStatus,
        message: String,
        path: String,
        details: Any? = null
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(status).body(
            ApiErrorResponse(
                code = status.name,
                message = message,
                details = details,
                timestamp = Instant.now().toString(),
                path = path
            )
        )
    }
}

data class ApiErrorResponse(
    val code: String,
    val message: String,
    val details: Any? = null,
    val timestamp: String,
    val path: String
)
