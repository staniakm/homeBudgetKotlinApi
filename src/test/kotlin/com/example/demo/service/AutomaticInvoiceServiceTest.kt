package com.example.demo.service

import com.example.demo.repository.InvoiceRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class AutomaticInvoiceServiceTest {

    @Test
    fun `should call repository when scheduled method runs`() {
        val invoiceRepository = mock(InvoiceRepository::class.java)
        val service = AutomaticInvoiceService(invoiceRepository)

        service.createAutomaticInvoice()

        verify(invoiceRepository, times(1)).createAutoInvoice()
    }
}
