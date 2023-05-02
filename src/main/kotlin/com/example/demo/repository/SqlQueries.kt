package com.example.demo.repository


object SqlQueries {

    val DELETE_INVOICE: () -> String = { deleteInvoice() }
    val DELETE_INVOICE_DETAILS: () -> String = { deleteInvoiceDetails() }
    val GET_INVOICE_ITEMS_BY_CATEGORY_AND_DATE: () -> String = { getInvoiceItemsByCategoryAndDate() }
    val GET_ACCOUNT_OPERATIONS: () -> String = { getAccountOperations() }
    val DECREASE_ACCOUNT_MONEY: () -> String = { decreaseAccountMoney() }
    val CREATE_INVOICE_DETAILS: () -> String = { createInvoiceDetails() }
    val GET_LAST_INVOICE: () -> String = { getLastInvoice() }
    val CREATE_INVOICE: () -> String = { createInvoice() }
    val GET_SHOP_ITEM_BY_NAME: () -> String = { getShopItemByName() }
    val GET_SHOP_BY_NAME: () -> String = { getShopByName() }
    val CREATE_SHOP: () -> String = { createShop() }
    val DELETE_MEDIA_USAGE: () -> String = { deleteMediaUsage() }
    val GET_MEDIA_USAGE_BY_TYPE: () -> String = { getMediaUsageBYType() }
    val GET_OWNER_BY_ID: () -> String = { getOwnerById() }
    val ADD_NEW_OWNER: () -> String = { createNewOwner() }
    val GET_ALL_OWNERS: () -> String = { getAllOwners() }
    val ADD_NEW_MEDIA_USAGE: () -> String = { addMediaUsage() }
    val GET_MEDIA_FOR_MONTH: () -> String = { getAllMediaReadsFromMonth() }
    val FIND_ALL_MEDIA_TYPES: () -> String = { findAllMediaTypes() }
    val FIND_MEDIA_TYPE_BY_ID: () -> String = { findMediaTypeById() }
    val FIND_MEDIA_TYPE_BY_NAME: () -> String = { findMediaTypeByName() }
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
    val GET_PRODUCT_HISTORY: () -> String = { getProductHistory() }
    val GET_PRODUCT_BY_ID: () -> String = { getProductById() }
    val UPDATE_PRODUCT_CATEGORY: () -> String = { updateProductCategory() }
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
    private fun updateProductCategory(): String = "update assortment set category = ? where id = ?"


    private fun getAccountOperations() =
        """select id, date, sum as value,account, 'OUTCOME' as type from invoice where account = ?
                                                union 
                                                select id, date, value, account,'INCOME' from income where account = ?
                                                order by date desc
                                                limit ?""".trimIndent()

    private fun getAllOwners() = """select id, owner_name, description from account_owner"""
    private fun createNewOwner() = """insert into account_owner (owner_name, description) values (?,?) RETURNING id"""
    private fun getOwnerById() =
        """select id, owner_name, description from account_owner where id = ?"""

    private fun decreaseAccountMoney() = """
        update account set money = money - ? where id = ?
    """.trimIndent()

    private fun createNewMediaType() = """insert into media_type(name) values (?)""".trimIndent()
    private fun findMediaTypeByName() = """select id, name from media_type where name = ?"""
    private fun findMediaTypeById() = """select id, name from media_type where id = ?"""
    private fun findAllMediaTypes() = """select id, name from media_type where del = false"""


    private fun getMediaUsageBYType() =
        """select media_usage.id, media_type.id type_id, year, month, meter_read from media_usage
        | join media_type on media_usage.media_type=media_type.id
        |  where media_type.id = ? order by year desc, month desc""".trimMargin()

    private fun getAllMediaReadsFromMonth() =
        """select id, media_type, year, month, meter_read from media_usage where year = ? and month = ?"""

    private fun addMediaUsage() =
        """insert into media_usage(media_type, meter_read, year, month) values (?,?,?,?)"""

    private fun deleteMediaUsage() = """delete from media_usage where id = ?"""

    private fun updateAccountWithNewAmount() = """
        update account set money = money + ? where del = false and id = ? 
    """.trimIndent()

    private fun addAccountIncome() = """
        insert into income(account, value, description, date) values (?,?,?,?)
    """.trimIndent()

    private fun getIncomeTypes(): String {
        return "select id, name from salary_type"
    }

    private fun updateSingleAccount(): String {
        return "update account set money = ? where del = false and id = ?"
    }

    private fun getAccountData(): String {
        return "select id, account_name, money as amount, owner from account where del = false order by account_name"
    }

    private fun getSingleAccountData(): String {
        return "select id, account_name, money as amount, owner from account where del = false and id = ?"
    }

    private fun getAccountIncome(): String {
        return """
                select k.id, k.account_name as name, coalesce(p.value,0) income, date, p.description description
                from account k 
                    left join income p on p.account = k.ID 
                    where k.del = false and
                      k.id = ?
                      and extract(year from date) = ? and extract(month from date) = ?""".trimIndent()
    }

    private fun createShop() = """insert into shop (name) values (?)"""

    private fun getShopByName() = """select id, name from shop where name = ? order by id desc limit 1"""

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
                    left join (select sum(sum) expense, account from invoice where del = false and extract(year from date) = ?
							and extract(month from date) = ? group by account) ex on ex.account = k.ID 
                    left join (select sum(value) income, account from income where extract(year from date) = ? and extract(month from date) = ? group by account) i on i.account = k.ID 
                where k.del = false and id > 1
                order by k.account_name""".trimIndent()
    }

    private fun updatePlanedBudget(): String {
        return """update budget set planned = ? where id = ?""".trimIndent()
    }

    private fun getMonthBudgetDetails(): String {

        return """select spend outcome, 
                    planned, 
                    coalesce(income,0) income
					from
                    (select sum(used) spend, sum(planned) planned, year, month from budget b group by year, month) b
                    left join(select sum(value) income, extract(year from date) y , extract(month from date) m from income 
                        group by extract(year from date) , extract(month from date) ) as p on p.y = b.year and p.m = b.month
                where b.year = ? and b.month = ?
                """.trimIndent()
    }

    private fun getSingleBudget() = """select 
                   b.id, 
                   b.month, 
                   b.year,
                   k.id categoryId,
                   k.name category, 
                   b.planned,   
                   b.used spent, 
                   b.percentage ,
				   (select sum(planned) from budget where month=b.month and year=b.year) monthPlanned
                from budget b 
                   join category k on k.id = b.category
                where b.id = ?
                order by b.used desc""".trimIndent()

    private fun getMonthBudget(): String {
        return """select 
                   b.id, 
                   b.month,
                   b.year,
                   k.name category, 
                   b.planned,   
                   b.used spent, 
                   b.percentage 
                from budget b 
                   join category k on k.id = b.category 
                where year = ?
                   and month = ?
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
                       and s.ID = ? 
                 order by a.name""".trimIndent()
    }

    private fun getShopItemByName(): String {
        return """select 
                       a.id, 
                       a.name 
                 from shop_assortment aso_s 
                       join shop s on s.ID = aso_s.shop 
                       join assortment a on a.id = aso_s.aso 
                 where aso_s.del = false 
                       and a.del = false 
                       and s.ID = ?
                       and UPPER(a.name) = UPPER(?)
                 order by a.name""".trimIndent()
    }

    private fun getMonthSummary(): String {
        return """select 
                   k.name, 
                   sum(ps.price) sum
                 from invoice p 
                   join invoice_details ps on ps.invoice = p.ID 
                   join category k on k.id = ps.category 
                 where extract(year from p.date) = ? 
                   and extract(month from p.date) = ?
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
                                       where p.shop = ? 
                                           and p.del = false
                                           and extract(year from p.date) = ?) 
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
                                   where p.shop = ?
                                       and p.del = false
                                       and p.date between ? and ?)
                                   group by a.id, a."name"
                                   """.trimIndent()
    }

    private fun getShopListSummary(): String {
        return """select s.id, 
                         s."name" ,
                         y.yearsum, 
                         coalesce(m.monthSummary, 0.00) monthSum 
                  from shop s 
                  join (select shop, sum(sum) yearSum from invoice where extract(year from date)= ? group by shop) as y 
                        on y.shop = s.ID 
                  left join (select p.shop, sum(p.sum) monthSummary from invoice p 
                  			where extract(year from p.date) = ? and extract(month from p.date) = ? group by shop) m on m.shop = s.ID 
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
                               where extract(year from p.date) = ? 
                               group by category) as pr on pr.category = k.id 
                       left join (select 
                                       sum(price) price, 
                                       category 
                                  from invoice_details ps 
                                       join invoice p on p.ID = ps.invoice 
                                  where extract(year from p.date)=? and extract(month from p.date) = ?
                                  group by category) as ps on ps.category = k.id where k.id = ? 
                  order by name""".trimIndent()
    }

    private fun getCategoryList(): String {
        return """select k.id id, 
                    k."name", 
                    coalesce(ps.price,0.00) monthSummary, 
                    pr.price yearSummary 
                from category k 
                join (select sum(ps.price) price, category from invoice_details ps 
                            join invoice p on p.ID = ps.invoice where extract(year from p.date) = ?
                            group by category) as pr on pr.category = k.id 
                left join (select sum(ps.price) price, category from invoice_details ps 
                join invoice p on p.ID = ps.invoice where extract(year from p.date) = ? and extract(month from p.date) = ?
                group by category) as ps on ps.category = k.id 
                order by name""".trimIndent()
    }

    private fun getCategoryDetails(): String {
        return """select sum(ps.price) sum,
                    	a.name
                    from invoice_details ps 
                        join invoice p on p.ID = ps.invoice
                    	join assortment a on a.id = ps.assortment 
                    where p.date between ? and ?
                                       and ps.category = ?
                    group by a."name"
                 """.trimIndent()
    }

    private fun getSelectedInvoice(): String {
        return """select id, date, invoice_number, sum, description, del, account, shop from invoice where id = ?""".trimIndent()
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
                   join shop s on s.ID = p.shop where extract(year from p.date) = ? and extract(month from p.date) = ?  order by date desc
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
                where extract(year from p.date) = ?
						and extract(month from p.date) = ?
                    	and a.id = ?
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
                        where ps.del = false and invoice = ?
                """.trimIndent()
    }

    private fun deleteInvoice(): String {
        return """
            delete from invoice where id = ?
        """.trimIndent()
    }

    private fun deleteInvoiceDetails(): String {
        return """
            delete from invoice_details where invoice = ?
        """.trimIndent()
    }

    private fun getInvoiceItemsByCategoryAndDate(): String {
        return """
            select id.id,
                         id.price,
                         id.description,
                         id.amount,
                         id.unit_price,
                         a.name,
                         id.discount,
                         a.id itemId
                     from invoice i
                            join invoice_details id on i.id = id.invoice
                            join assortment a on id.assortment = a.id
                            where extract('YEAR' from i.date) = $1 and  extract('MONTH' from i.date) = $2
                                and id.category =$3
                            order by  a.name
        """.trimIndent()
    }

    private fun getProductHistory(): String {
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
                    where ps.assortment=?
                        order by p.date desc
                """.trimIndent()
    }

    private fun getProductById(): String {
        return """
            select a.id, a.name, c.id as categoryId, c.name as categoryName  
            from assortment a
                join category c on c.id = a.category
            where a.id = ?
        """.trimIndent()
    }

    private fun createInvoice() = """
        insert into invoice(date, invoice_number, sum, description, account, shop) values (?,?,?,?,?,?) RETURNING id
    """.trimIndent()

    private fun getLastInvoice() =
        """select id, date, invoice_number, sum, description, del, account, shop from invoice order by id desc limit 1""".trimIndent()

    private fun createInvoiceDetails() = """
        insert into invoice_details (invoice, price, amount, unit_price, discount, assortment) values (?,?,?,?,?,?);
    """.trimIndent()
}
