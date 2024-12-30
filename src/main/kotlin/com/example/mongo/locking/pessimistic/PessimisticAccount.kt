package com.example.mongo.locking.pessimistic

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "pessimistic_accounts")
data class PessimisticAccount(
    @Id
    val id: String? = null,
    var balance: Double,
    var locked: Boolean = false
)