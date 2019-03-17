package com.example.demo.repository

import com.example.demo.repository.SqlQueries.QUERY_TYPE.*

object SqlQueries {

    enum class QUERY_TYPE{
        GET_INVOICE,
        GET_INVOICE_DETAILS,
        GET_CATEGORY_LIST,
        GET_CATEGORY_DETAILS,
        GET_SHOP_LIST
    }

    fun getQuerry(type: QUERY_TYPE): String {
        return when (type){

            GET_INVOICE -> getInvoices()
            GET_INVOICE_DETAILS -> getInvoiceDetails()
            GET_CATEGORY_LIST -> getCategoryList()
            GET_CATEGORY_DETAILS -> getCategoryDetails()
            GET_SHOP_LIST -> getShopList()
        }
    }

    private fun getShopList(): String {
        return "select id, sklep nazwa from sklepy order by sklep"

    }

    private fun getCategoryList(): String {
        return "select k.id id, nazwa, isnull(ps.price,0.00) monthSummary, pr.price yearSummary from kategoria k \n" +
                "\tjoin (select sum(cena) price, kategoria from paragony_szczegoly ps \n" +
                "\t\t\tjoin paragony p on p.ID = ps.id_paragonu where year(p.data) = year(getdate())\n" +
                "\t\t\tgroup by kategoria) as pr on pr.kategoria = k.id\n" +
                "\tleft join (select sum(cena) price, kategoria from paragony_szczegoly ps \n" +
                "\t\t\tjoin paragony p on p.ID = ps.id_paragonu where p.data>= DATEADD(d,1,(DATEADD(m, -1, EOMONTH(GETDATE()))))\n" +
                "\t\t\tgroup by kategoria) as ps on ps.kategoria = k.id\n" +
                "order by nazwa\t\n"
    }

    private fun getCategoryDetails(): String {
        return "select sum(cena) cena,  a.NAZWA from paragony_szczegoly ps " +
                "join paragony p on p.ID = ps.id_paragonu " +
                "join ASORTYMENT a on a.id = ps.ID_ASO " +
                "where p.data>= DATEADD(d,1,(DATEADD(m, -1, EOMONTH(GETDATE())))) " +
                "and ps.kategoria = ? " +
                "group by a.NAZWA"
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