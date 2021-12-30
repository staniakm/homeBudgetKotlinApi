package com.example.demo.repository


object SqlQueries {

    val FIND_ALL_MEDIA_TYPES: () -> String = {findAllMediaTypes()}
    val FIND_MEDIA_TYPE_BY_ID: () -> String = { findMediaTypeById() }
    val FIND_MEDIA_TYPE_BY_NAME: () -> String = {findMediaTypeByName()}
    val ADD_NEW_MEDIA_TYPE: () -> String = { createNewMediaType() }
    val GET_INVOICE: () -> String = { getInvoices() }
    val GET_INVOICE_DATA: () -> String = { getSelectedInvoice() }
    val GET_ACCOUNT_INVOICES: () -> String = { getAccountInvoices() }
    val GET_INVOICE_DETAILS: () -> String = { getInvoiceDetails() }
    val GET_CATEGORY_SUMMARY_LIST: () -> String = { getCategoryList() }
    val GET_CATEGORY_DETAILS: () -> String = { getCategoryDetails() }
    val GET_SHOP_LIST_SUMMARY: () -> String = { getShopListSummary() }
    val GET_SHOP_MONTH_ITEMS: () -> String = { getShopMonthShopping() }
    val GET_SHOP_YEAR_ITEMS: () -> String = { getShopYearShopping() }
    val GET_MONTH_SUMMARY_CHART_DATA: () -> String = { getMonthSummary() }
    val GET_SHOP_ITEMS: () -> String = { getShopItems() }
    val GET_MONTH_BUDGET: () -> String = { getMonthBudget() }
    val GET_CATEGORY_BY_ID: () -> String = { getCategoryById() }
    val GET_MONTH_BUDGE_DETAILS: () -> String = { getMonthBudgetDetails() }
    val UPDATE_MONTH_BUDGE_DETAILS: () -> String = { updatePlanedBudget() }
    val GET_SINGLE_BUDGET: () -> String = { getSingleBudget() }
    val GET_PRODUCT_DETAILS: () -> String = { getProductDetails() }
    val GET_MONTH_BUDGET_FOR_CATEGORY: () -> String = { getMonthBudgetForCategory() }
    val GET_ACCOUNTS_SUMMARY_FOR_MONTH: () -> String = { getAccountsSummaryForMonth() }
    val GET_SHOP_LIST: () -> String = { getShopList() }
    val GET_ACCOUNT_DATA: () -> String = { getAccountData() }
    val GET_SINGLE_ACCOUNT_DATA: () -> String = { getSingleAccountData() }
    val UPDATE_SINGLE_ACCOUNT_DATA: () -> String = { updateSingleAccount() }
    val GET_ACCOUNT_INCOME: () -> String = { getAccountIncome() }
    val GET_INCOME_TYPES: () -> String = { getIncomeTypes() }
    val ADD_ACCOUNT_INCOME: () -> String = { addAccountIncome() }
    val UPDATE_ACCOUNT_WITH_NEW_AMOUNT = { updateAccountWithNewAmount() }

    private fun createNewMediaType() = """insert into media_type(name) values ($1)""".trimIndent()
    private fun findMediaTypeByName() = """select id, name from media_type where name = $1"""
    private fun findMediaTypeById() = """select id, name from media_type where id = $1"""
    private fun findAllMediaTypes() = """select id, name from media_type where del = false"""

    private fun updateAccountWithNewAmount() = """
        update account set money = money + $1 where del = false and id = $2 
    """.trimIndent()

    private fun addAccountIncome() = """
        insert into income(account, value, description, date) values ($1, $2, $3, $4)
    """.trimIndent()

    private fun getIncomeTypes(): String {
        return "select id, name from salary_type"
    }

    private fun updateSingleAccount(): String {
        return "update account set money = $1 where del = false and id = $2"
    }

    private fun getAccountData(): String {
        return "select id, account_name, money as amount, owner from account where del = false order by account_name"
    }

    private fun getSingleAccountData(): String {
        return "select id, account_name, money as amount, owner from account where del = false and id = $1"
    }

    private fun getAccountIncome(): String {
        return """
                select k.id, k.account_name as name, coalesce(p.value,0) income, date, p.description description
                from account k 
                    left join income p on p.account = k.ID 
                    where k.del = false and
                      k.id = $3
                      and extract(year from date) = $1 and extract(month from date) = $2""".trimIndent()
    }

    private fun getShopList(): String {
        return "select id, name from shop order by name"
    }

    private fun getAccountsSummaryForMonth(): String {
        return """select k.id, 
                    k.account_name, 
                    coalesce(k.money,0) money, 
                    coalesce(ex.expense,0) expense,  
                    coalesce(i.income,0) income 
                from account k 
                    left join (select sum(sum) expense, account from invoice where del = false and extract(year from date) = $1
							and extract(month from date) = $2 group by account) ex on ex.account = k.ID 
                    left join (select sum(value) income, account from income where extract(year from date) = $1 and extract(month from date) = $2 group by account) i on i.account = k.ID 
                where k.del = false and id > 1
                order by k.account_name""".trimIndent()
    }

    private fun updatePlanedBudget(): String {
        return """update budget set planned = $1 where id = $2""".trimIndent()
    }

    private fun getMonthBudgetDetails(): String {

        return """select spend outcome, 
                    planned, 
                    coalesce(income,0) income
					from
                    (select sum(used) spend, sum(planned) planned, year, month from budget b group by year, month) b
                    left join(select sum(value) income, extract(year from date) y , extract(month from date) m from income 
                        group by extract(year from date) , extract(month from date) ) as p on p.y = b.year and p.m = b.month
                where b.year = $1 and b.month = $2
                """.trimIndent()
    }

    private fun getSingleBudget() = """select 
                   b.id, 
                   b.month, 
                   k.name category, 
                   b.planned,   
                   b.used spent, 
                   b.percentage ,
				   (select sum(planned) from budget where month=b.month and year=b.year) monthPlanned
                from budget b 
                   join category k on k.id = b.category
                where b.id = $1
                order by b.used desc""".trimIndent()

    private fun getMonthBudget(): String {
        return """select 
                   b.id, 
                   b.month, 
                   k.name category, 
                   b.planned,   
                   b.used spent, 
                   b.percentage 
                from budget b 
                   join category k on k.id = b.category 
                where year = $1
                   and month = $2
                order by b.used desc""".trimIndent()
    }

    private fun getMonthBudgetForCategory(): String {
        return """select 
                       b.id, 
                       b.month, 
                       k.name category, 
                       b.planned planned, 
                      b.used spent, 
                      b.percentage 
                  from budget b 
                      join category k ON k.id = b.category
                  where year = $1 
                       and month = $2 
                       and k.name = $3
                   order by spent desc""".trimIndent()
    }

    private fun getShopItems(): String {
        return """select 
                       a.id, 
                       a.name 
                 from shop_assortment aso_s 
                       join shop s on s.ID = aso_s.shop 
                       join assortment a on a.id = aso_s.aso 
                 where aso_s.del = false 
                       and a.del = false 
                       and s.ID = $1 
                 order by a.name""".trimIndent()
    }

    private fun getMonthSummary(): String {
        return """select 
                   k.name, 
                   sum(ps.price) sum
                 from invoice p 
                   join invoice_details ps on ps.invoice = p.ID 
                   join category k on k.id = ps.category 
                 where extract(year from p.date) = $1 
                   and extract(month from p.date) = $2
                 group by k.name 
                 order by sum""".trimIndent()
    }

    private fun getShopYearShopping(): String {
        return """select 
                   a.id, 
                   a.name, 
                   sum(amount) quantity, 
                   min(unit_price) min_price_for_unit, 
                   max(unit_price) max_price_for_unit, 
                   sum(discount) discount_sum, 
                   sum(price) total_spend 
                 from invoice_details ps 
                   join assortment a on a.id = ps.assortment and a.del = false 
                 where ps.del=false and ps.invoice in 
                                   (select id 
                                       from invoice p 
                                       where p.shop = $1 
                                           and p.del = false
                                           and extract(year from p.date) = $2) 
                                        group by a.id, a.name""".trimIndent()
    }

    private fun getShopMonthShopping(): String {
        return """select 
                   a.id, 
                   a.name,
                    sum(amount) quantity, 
                    min(unit_price) min_price_for_unit, 
                    max(unit_price) max_price_for_unit, 
                    sum(discount) discount_sum, 
                    sum(price) total_spend 
                 from invoice_details ps 
                   join assortment a on a.id = ps.assortment and a.del = false
                 where ps.del = false 
				 and ps.invoice in 
                               (select id 
                                   from invoice p 
                                   where p.shop = $1
                                       and p.del = false
                                       and p.date between $2 and $3)
                                   group by a.id, a."name"
                                   """.trimIndent()
    }

    private fun getShopListSummary(): String {
        return """select s.id, 
                         s."name" ,
                         y.yearsum, 
                         coalesce(m.monthSummary, 0.00) monthSum 
                  from shop s 
                  join (select shop, sum(sum) yearSum from invoice where extract(year from date)= $1 group by shop) as y 
                        on y.shop = s.ID 
                  left join (select p.shop, sum(p.sum) monthSummary from invoice p 
                  			where extract(year from p.date) = $1 and extract(month from p.date) = $2 group by shop) m on m.shop = s.ID 
                  order by s.id""".trimIndent()

    }

    private fun getCategoryById(): String {
        return """select 
                      k.id id, 
                      name, 
                      coalesce(ps.price,0.00) monthSummary, 
                      pr.price yearSummary 
                  from category k 
                       join (select sum(price) price, category 
                               from invoice_details ps 
                                   join invoice p on p.ID = ps.invoice 
                               where extract(year from p.date) = $1 
                               group by category) as pr on pr.category = k.id 
                       left join (select 
                                       sum(price) price, 
                                       category 
                                  from invoice_details ps 
                                       join invoice p on p.ID = ps.invoice 
                                  where extract(year from p.date)=$1 and extract(month from p.date) = $2
                                  group by category) as ps on ps.category = k.id where k.id = $3 
                  order by name""".trimIndent()
    }

    private fun getCategoryList(): String {
        return """select k.id id, 
                    k."name", 
                    coalesce(ps.price,0.00) monthSummary, 
                    pr.price yearSummary 
                from category k 
                join (select sum(ps.price) price, category from invoice_details ps 
                            join invoice p on p.ID = ps.invoice where extract(year from p.date) = $1
                            group by category) as pr on pr.category = k.id 
                left join (select sum(ps.price) price, category from invoice_details ps 
                join invoice p on p.ID = ps.invoice where extract(year from p.date) = $1 and extract(month from p.date) = $2
                group by category) as ps on ps.category = k.id 
                order by name""".trimIndent()
    }

    private fun getCategoryDetails(): String {
        return """select sum(ps.price) sum,
                    	a.name
                    from invoice_details ps 
                        join invoice p on p.ID = ps.invoice
                    	join assortment a on a.id = ps.assortment 
                    where p.date between $1 and $2
                                       and ps.category = $3
                    group by a."name"
                 """.trimIndent()
    }

    private fun getSelectedInvoice(): String {
        return """select id, date, invoice_number, sum, description, del, account, shop from invoice where id = $1""".trimIndent()
    }

    private fun getInvoices(): String {
        return """
                select p.ID,
                   date,
                   invoice_number,
                   sum,
                   s."name" shopName,
                   k.account_name
                FROM invoice p
                   join account k on k.ID = p.account
                   join shop s on s.ID = p.shop where extract(year from p.date) = $1 and extract(month from p.date) = $2  order by date desc
                """.trimIndent()
    }

    private fun getAccountInvoices(): String {
        return """
                select p.ID,
                   date,
                   invoice_number,
                   sum,
                   s.name shopName,
                   a.account_name
                FROM invoice p
                   join account a ON a.id = p.account
                   join shop s ON s.id = p.shop
                where extract(year from p.date) = $1
						and extract(month from p.date) = $2
                    	and a.id = $3
                   order by date desc
                """.trimIndent()
    }

    private fun getInvoiceDetails(): String {
        return """select ps.id,
                         price,
                         description,
                         amount,
                         unit_price,
                         a.name,
                         ps.discount,
                         a.id itemId
                    from invoice_details ps
                        join assortment a on a.id = ps.assortment
                        where ps.del = false and invoice = $1
                """.trimIndent()
    }

    private fun getProductDetails(): String {
        return """select s.name shopName,
                        p.date, 
                        ps.unit_price unitPrice, 
                        ps.amount,
						ps.discount,
						p.sum, 
                        p.id invoiceId, 
                        ps.id invoiceItemId ,
						a.name assortmentName
                    from invoice_details ps
                        join invoice p ON p.id = ps.invoice
                        join shop s on s.ID = p.shop
						join assortment a on a.id = ps.assortment
                    where ps.assortment=$1
                        order by p.date desc
                """.trimIndent()
    }
}
