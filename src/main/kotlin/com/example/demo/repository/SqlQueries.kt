package com.example.demo.repository

import com.example.demo.repository.SqlQueries.QUERY_TYPE.*

object SqlQueries {

    enum class QUERY_TYPE {
        GET_INVOICE,
        GET_INVOICE_DETAILS,
        GET_CATEGORY_LIST,
        GET_CATEGORY_DETAILS,
        GET_CATEGORY_BY_ID,
        GET_SHOP_LIST,
        GET_SHOP_MONTH_ITEMS,
        GET_SHOP_YEAR_ITEMS,
        GET_MONTH_SUMMARY_CHART_DATA,
        GET_SHOP_ITEMS,
        GET_MONTH_BUDGET,
        GET_MONTH_BUDGE_DETAILS,
        UPDATE_MONTH_BUDGE_DETAILS,
        GET_PRODUCT_DETAILS,
        GET_MONTH_BUDGET_FOR_CATEGORY,
        GET_ACCOUNT_LIST
    }

    fun getQuery(type: QUERY_TYPE): String {
        return when (type) {

            GET_INVOICE -> getInvoices()
            GET_INVOICE_DETAILS -> getInvoiceDetails()
            GET_CATEGORY_LIST -> getCategoryList()
            GET_CATEGORY_DETAILS -> getCategoryDetails()
            GET_SHOP_LIST -> getShopList()
            GET_SHOP_MONTH_ITEMS -> getShopMonthShoppings()
            GET_SHOP_YEAR_ITEMS -> getShopYearShoppings()
            GET_MONTH_SUMMARY_CHART_DATA -> getMonthSummary()
            GET_SHOP_ITEMS -> getShopItems()
            GET_MONTH_BUDGET -> getMonthBudget()
            GET_CATEGORY_BY_ID -> getCategoryById()
            GET_MONTH_BUDGE_DETAILS -> getMonthBudgetDetails()
            UPDATE_MONTH_BUDGE_DETAILS -> updatePlanedBudget()
            GET_PRODUCT_DETAILS -> getProductDetails()
            GET_MONTH_BUDGET_FOR_CATEGORY -> getMonthBudgetForCategory()
            GET_ACCOUNT_LIST -> getAccountList();
        }
    }

    private fun getAccountList(): String {
        return "select k.id, k.nazwa, isnull(k.kwota,0) kwota, isnull(ex.wydatki,0) wydatki, isnull(i.przychody,0) przychody " +
                "from konto k " +
                "left join (select sum(suma) wydatki, konto from paragony where del = 0 and year(data) = ? and month(data) = ? group by konto) ex on ex.konto = k.ID " +
                "left join (select sum(kwota) przychody, konto from przychody where year(data) = ? and month(data) = ? group by konto) i on i.konto = k.ID " +
                "where k.del = 0 and id > 1;"
    }

    private fun updatePlanedBudget(): String {
        return "update b set b.planed = ? " +
                "from budzet b " +
                "join kategoria c on c.id = b.category " +
                "where c.nazwa = ? " +
                "and b.rok = ? " +
                "and b.miesiac = ?"
    }

    private fun getMonthBudgetDetails(): String {

        return "select spend outcome, planed, isnull(przychod,0) income " +
                "from " +
                "(select sum(used) spend, sum(planed) planed, rok, miesiac from budzet b group by rok, miesiac) b " +
                "left join(select sum(kwota) przychod, year(data) rok, month(data) miesiac from przychody group by year(data) , month(data) ) as p on p.rok = b.rok and p.miesiac = b.miesiac " +
                "where b.rok = ? and b.miesiac = ?"
    }

    private fun getMonthBudget(): String {
        return "select b.id, b.miesiac, k.nazwa category, b.planed planned, b.used spent, b.percentUsed percentage from budzet b " +
                "join kategoria k on k.id = b.category " +
                "where rok = ? and miesiac = ? and b.used > 0" +
                "order by k.nazwa"
    }

    private fun getMonthBudgetForCategory(): String {
        return "select b.id, b.miesiac, k.nazwa category, b.planed planned, b.used spent, b.percentUsed percentage from budzet b " +
                "join kategoria k on k.id = b.category " +
                "where rok = ? and miesiac = ? and k.nazwa = ? and b.used > 0" +
                "order by k.nazwa"
    }

    private fun getShopItems(): String {
        return "select a.id, a.NAZWA from ASORTYMENT_SKLEP aso_s " +
                "join sklepy s on s.ID = aso_s.id_sklep " +
                "join ASORTYMENT a on a.id = aso_s.id_aso " +
                "where aso_s.del = 0 and a.del = 0 and s.ID = ? order by a.nazwa"
    }

    private fun getMonthSummary(): String {
        return "select k.nazwa, sum(ps.cena) suma from paragony p " +
                "join paragony_szczegoly ps on ps.id_paragonu = p.ID " +
                "join kategoria k on k.id = ps.kategoria " +
                "where year(p.data) = year(getdate()) and month(p.data) = ? " +
                "group by k.nazwa order by suma"
    }

    private fun getShopYearShoppings(): String {
        return "select a.id, a.NAZWA name, " +
                "sum(ilosc) quantity, " +
                "min(cena_za_jednostke) min_price_for_unit, " +
                "max(cena_za_jednostke) max_price_for_unit, " +
                "sum(rabat) discount_sum, " +
                "sum(cena) total_spend " +
                "from paragony_szczegoly ps " +
                "join ASORTYMENT a on a.id = ps.ID_ASO and a.del = 0 " +
                "where ps.id_paragonu in (select id from paragony p where p.ID_sklep = ? " +
                "and p.del = 0" +
                "and year(p.data) = year(getdate())) " +
                "group by a.id, a.NAZWA"
    }

    private fun getShopMonthShoppings(): String {
        return "select a.id, a.NAZWA name, " +
                "sum(ilosc) quantity, " +
                "min(cena_za_jednostke) min_price_for_unit, " +
                "max(cena_za_jednostke) max_price_for_unit, " +
                "sum(rabat) discount_sum, " +
                "sum(cena) total_spend " +
                "from paragony_szczegoly ps " +
                "join ASORTYMENT a on a.id = ps.ID_ASO and a.del = 0 " +
                "where ps.id_paragonu in (select id from paragony p where p.ID_sklep = ? " +
                "and p.del = 0" +
                "and p.data >= DATEADD(m, -1, DATEADD(d, 1, EOMONTH(getdate())))) " +
                "group by a.id, a.NAZWA"
    }

    private fun getShopList(): String {
        return "select s.id, s.sklep nazwa, y.yearSum, isnull(m.monthSummary, 0.00) monthSum from sklepy s " +
                "join (select id_sklep, sum(suma) yearSum from paragony where year(data)= ? group by ID_sklep) as y on y.ID_sklep = s.ID " +
                "left join (select p.ID_sklep, sum(p.suma) monthSummary from paragony p " +
                "where year(p.data) = ? and month(p.data) = ? " +
                "group by ID_sklep) m on m.ID_sklep = s.ID  " +
                "order by s.sklep"

    }

    private fun getCategoryById(): String {
        return "select k.id id, nazwa, isnull(ps.price,0.00) monthSummary, pr.price yearSummary from kategoria k " +
                "join (select sum(cena) price, kategoria from paragony_szczegoly ps " +
                "join paragony p on p.ID = ps.id_paragonu where year(p.data) = year(getdate()) " +
                "group by kategoria) as pr on pr.kategoria = k.id " +
                "left join (select sum(cena) price, kategoria from paragony_szczegoly ps " +
                "join paragony p on p.ID = ps.id_paragonu where p.data>= DATEADD(d,1,(DATEADD(m, -1, EOMONTH(GETDATE())))) " +
                "group by kategoria) as ps on ps.kategoria = k.id " +
                "where k.id = ? " +
                "order by nazwa"
    }

    private fun getCategoryList(): String {
        return "select k.id id, nazwa, isnull(ps.price,0.00) monthSummary, pr.price yearSummary from kategoria k " +
                "                join (select sum(cena) price, kategoria from paragony_szczegoly ps " +
                "                join paragony p on p.ID = ps.id_paragonu where year(p.data) = ? " +
                "                group by kategoria) as pr on pr.kategoria = k.id " +
                "                left join (select sum(cena) price, kategoria from paragony_szczegoly ps " +
                "                join paragony p on p.ID = ps.id_paragonu where year(p.data) = ? and month(p.data) = ? " +
                "                group by kategoria) as ps on ps.kategoria = k.id " +
                "                order by nazwa"
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
        return "select p.ID, DATA, NR_PARAGONU, SUMA, s.sklep, k.nazwa account FROM dbo.paragony p " +
                "join konto k on k.ID = p.konto " +
                "join sklepy s on s.ID = p.ID_sklep where year(p.data) = ? and month(p.data) = ? order by data desc"
    }

    private fun getInvoiceDetails(): String {
        return "select ps.id, cena, opis, ilosc, cena_za_jednostke, a.NAZWA, ps.rabat, a.id itemId from paragony_szczegoly ps " +
                "join ASORTYMENT a on a.id = ps.ID_ASO where id_paragonu = ?"
    }

    private fun getProductDetails():String{
        return "select s.sklep,p.data, ps.cena_za_jednostke cena, ps.ilosc, ps.rabat, p.suma, p.ID invoiceId, ps.ID invoiceItemId from paragony_szczegoly ps " +
                "join paragony p on p.ID = ps.id_paragonu " +
                "join sklepy s on s.ID = p.ID_sklep " +
                "where ps.ID_ASO=? " +
                "order by p.data desc"
    }

}