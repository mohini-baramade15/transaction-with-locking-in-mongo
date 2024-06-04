package com.example.mongo.locking.optimistic

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "accounts")
data class Account(
    @Id
    val id: String? = null,

    var balance: Double,

    @Version
    val version: Long? = null
)