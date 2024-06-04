package com.example.mongo.locking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionWithLockingApplication

fun main(args: Array<String>) {
	runApplication<TransactionWithLockingApplication>(*args)
}
