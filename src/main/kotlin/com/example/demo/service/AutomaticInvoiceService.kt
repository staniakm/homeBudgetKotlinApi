package com.example.demo.service

import com.example.demo.repository.InvoiceRepository
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class AutomaticInvoiceService(private val invoiceRepository: InvoiceRepository) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(initialDelay = 5000, fixedRate = 1000 * 60 * 60 * 24)
    fun createAutomaticInvoice() {
        logger.info("Execute Auto Invoice procedure")
        invoiceRepository.createAutoInvoice().block()
    }
}