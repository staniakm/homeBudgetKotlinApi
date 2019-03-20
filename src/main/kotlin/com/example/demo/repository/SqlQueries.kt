package com.example.demo.repository

import com.example.demo.repository.SqlQueries.QUERY_TYPE.*

object SqlQueries {

    enum class QUERY_TYPE{
        GET_INVOICE,
        GET_INVOICE_DETAILS,
        GET_CATEGORY_LIST,
        GET_CATEGORY_DETAILS,
        GET_SHOP_LIST,
        GET_SHOP_MONTH_ITEMS,
        GET_SHOP_YEAR_ITEMS,
        GET_ITEM,
        GET_MONTH_SUMMARY_CHART_DATA
    }

    fun getQuerry(type: QUERY_TYPE): String {
        return when (type){

            GET_INVOICE -> getInvoices()
            GET_INVOICE_DETAILS -> getInvoiceDetails()
            GET_CATEGORY_LIST -> getCategoryList()
            GET_CATEGORY_DETAILS -> getCategoryDetails()
            GET_SHOP_LIST -> getShopList()
            GET_SHOP_MONTH_ITEMS -> getShopMonthShoppings()
            GET_SHOP_YEAR_ITEMS -> getShopYearShoppings()
            GET_ITEM -> getItemById()
            GET_MONTH_SUMMARY_CHART_DATA -> getMonthSummary()
        }
    }

    private fun getMonthSummary(): String {
        return "select k.nazwa, sum(ps.cena) suma from paragony p " +
                "join paragony_szczegoly ps on ps.id_paragonu = p.ID " +
                "join kategoria k on k.id = ps.kategoria " +
                "where year(p.data) = year(getdate()) and month(p.data) = ? " +
                "group by k.nazwa"
    }

    private fun getItemById(): String {
        return ""
    }

    private fun getShopYearShoppings(): String {
        return "select ps.id, ps.cena, ps.opis, ps.ilosc, ps.cena_za_jednostke, a.NAZWA from paragony p " +
                "join paragony_szczegoly ps on p.ID = ps.id_paragonu " +
                "join ASORTYMENT a on a.id = ps.ID_ASO and a.del = 0 " +
                "where p.ID_sklep = ? " +
                "and year(p.data) = year(getdate())"
    }

    private fun getShopMonthShoppings(): String {
        return "select ps.id, ps.cena, ps.opis, ps.ilosc, ps.cena_za_jednostke, a.NAZWA from paragony p " +
                "join paragony_szczegoly ps on p.ID = ps.id_paragonu " +
                "join ASORTYMENT a on a.id = ps.ID_ASO and a.del = 0 " +
                "where p.ID_sklep = ? " +
                "and p.data >= DATEADD(m, -1, DATEADD(d, 1, EOMONTH(getdate())))"
    }

    private fun getShopList(): String {
        return "select s.id, s.sklep nazwa, y.yearSum, isnull(m.monthSummary, 0.00) monthSum from sklepy s\n" +
                "join (select id_sklep, sum(suma) yearSum from paragony where year(data)= year(getdate()) group by ID_sklep) as y on y.ID_sklep = s.ID\n" +
                "left join (select p.ID_sklep, sum(p.suma) monthSummary from paragony p \n" +
                "where p.data >= DATEADD(m, -1, DATEADD(d, 1, EOMONTH(getdate()))) group by ID_sklep) m on m.ID_sklep = s.ID \n" +
                "order by s.sklep"

    }

    private fun getCategoryList(): String {
        return "select k.id id, nazwa, isnull(ps.price,0.00) monthSummary, pr.price yearSummary from kategoria k \n" +
                "join (select sum(cena) price, kategoria from paragony_szczegoly ps \n" +
                "join paragony p on p.ID = ps.id_paragonu where year(p.data) = year(getdate())\n" +
                "group by kategoria) as pr on pr.kategoria = k.id\n" +
                "left join (select sum(cena) price, kategoria from paragony_szczegoly ps \n" +
                "join paragony p on p.ID = ps.id_paragonu where p.data>= DATEADD(d,1,(DATEADD(m, -1, EOMONTH(GETDATE()))))\n" +
                "group by kategoria) as ps on ps.kategoria = k.id\n" +
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