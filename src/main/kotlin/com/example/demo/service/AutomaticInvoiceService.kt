package com.example.demo.service

import com.example.demo.repository.InvoiceRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AutomaticInvoiceService(private val invoiceRepository: InvoiceRepository) {

    @Scheduled(initialDelay = 5000, fixedRate = 1000 * 60 * 60 * 24)
    fun createAutomaticInvoice() {
        val logger = LoggerFactory.getLogger(this.javaClass)
        logger.info("Execute Auto Invoice procedure")
        invoiceRepository.createAutoInvoice().block()
    }
}