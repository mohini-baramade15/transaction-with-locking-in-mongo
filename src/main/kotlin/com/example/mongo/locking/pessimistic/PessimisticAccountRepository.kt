package com.example.mongo.locking.pessimistic

import org.springframework.data.mongodb.repository.MongoRepository

interface PessimisticAccountRepository : MongoRepository<PessimisticAccount, String>