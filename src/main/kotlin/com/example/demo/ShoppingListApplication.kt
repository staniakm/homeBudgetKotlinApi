package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class ShoppingListApplication

fun main(args: Array<String>) {
    runApplication<ShoppingListApplication>(*args)
}
