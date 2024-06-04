package com.example.mongo.locking.optimistic

import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository : MongoRepository<Account, String>