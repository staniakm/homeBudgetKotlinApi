package com.example.demo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ErrorHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(ErrorResponse(ex.message ?: "Unknown error"))
    }
}

data class ErrorResponse(val message: String)