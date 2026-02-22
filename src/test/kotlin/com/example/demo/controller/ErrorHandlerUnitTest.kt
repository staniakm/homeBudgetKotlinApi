package com.example.demo.controller

import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.shouldBe
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

class ErrorHandlerUnitTest {

    private val handler = ErrorHandler()

    @Test
    fun `should return standardized validation response`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.requestURI).thenReturn("/api/test")

        val bindingResult = BeanPropertyBindingResult(Any(), "request")
        bindingResult.addError(FieldError("request", "name", "must not be blank"))

        val method = this::class.java.getDeclaredMethod("sample", String::class.java)
        val parameter = MethodParameter(method, 0)
        val ex = MethodArgumentNotValidException(parameter, bindingResult)

        val response = handler.handleValidation(ex, request)

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body!!.code shouldBe "BAD_REQUEST"
        response.body!!.message shouldBe "Validation failed"
        response.body!!.path shouldBe "/api/test"
        val details = response.body!!.details as Map<*, *>
        details["name"] shouldBe "must not be blank"
        response.body!!.timestamp.shouldNotBeBlank()
    }

    @Test
    fun `should return standardized internal error response`() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.requestURI).thenReturn("/api/test")

        val response = handler.handleGeneric(RuntimeException("boom"), request)

        response.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
        response.body!!.code shouldBe "INTERNAL_SERVER_ERROR"
        response.body!!.message shouldBe "Unexpected error"
        response.body!!.path shouldBe "/api/test"
        response.body!!.timestamp.shouldNotBeBlank()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun sample(value: String) {
    }
}
