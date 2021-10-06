package com.example.demo.repository

interface QueryProvider {
    val GET_INVOICE: () -> String
    val GET_ACCOUNT_INVOICES: () -> String
    val GET_INVOICE_DETAILS: () -> String
    val GET_CATEGORY_SUMMARY_LIST: () -> String
    val GET_CATEGORY_DETAILS: () -> String
    val GET_SHOP_LIST_SUMMARY: () -> String
    val GET_SHOP_MONTH_ITEMS: () -> String
    val GET_SHOP_YEAR_ITEMS: () -> String
    val GET_MONTH_SUMMARY_CHART_DATA: () -> String
    val GET_SHOP_ITEMS: () -> String
    val GET_MONTH_BUDGET: () -> String
    val GET_CATEGORY_BY_ID: () -> String
    val GET_MONTH_BUDGE_DETAILS: () -> String
    val UPDATE_MONTH_BUDGE_DETAILS: () -> String
    val GET_PRODUCT_DETAILS: () -> String
    val GET_MONTH_BUDGET_FOR_CATEGORY: () -> String
    val GET_ACCOUNTS_SUMMARY_FOR_MONTH: () -> String
    val GET_SHOP_LIST: () -> String
    val GET_ACCOUNT_DATA: () -> String
    val GET_SINGLE_ACCOUNT_DATA: () -> String
    val UPDATE_SINGLE_ACCOUNT_DATA: () -> String

}
