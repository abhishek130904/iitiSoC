package com.example.travel.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(FlightSearchException::class)
    fun handleFlightSearchException(ex: FlightSearchException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.status, ex.errorMessage)
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.status))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error: ${ex.message}")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(
    val status: Int,
    val message: String
)