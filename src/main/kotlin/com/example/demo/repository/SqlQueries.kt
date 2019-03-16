package com.example.demo.repository

import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE
import com.example.demo.repository.SqlQueries.QUERY_TYPE.GET_INVOICE_DETAILS

object SqlQueries {

    enum class QUERY_TYPE{
        GET_INVOICE,
        GET_INVOICE_DETAILS
    }

    fun getQuerry(type: QUERY_TYPE): String {
        return when (type){

            GET_INVOICE -> getInvoices()
            GET_INVOICE_DETAILS -> getInvoiceDetails()
        }
    }

    private fun getInvoices(): String {
        return "select p.ID, DATA, NR_PARAGONU, SUMA, s.sklep FROM dbo.paragony p\n" +
                "\tjoin sklepy s on s.ID = p.ID_sklep where p.data >= '2019-01-01' order by data desc"
    }

    private fun getInvoiceDetails(): String {
        return "select ps.id, cena, opis, ilosc, cena_za_jednostke, a.NAZWA from paragony_szczegoly ps\n" +
                "join ASORTYMENT a on a.id = ps.ID_ASO where id_paragonu = ?"
    }

}