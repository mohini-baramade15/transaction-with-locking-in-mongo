package com.example.mongo.locking.pessimistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class PessimisticAccountService(
    @Autowired val pessimisticAccountRepository: PessimisticAccountRepository,
    @Autowired val mongoTemplate: MongoTemplate
) {
    fun getAccountById(id: String): PessimisticAccount? {
        return pessimisticAccountRepository.findById(id).orElse(null)
    }

    fun addAccount(account: PessimisticAccount): PessimisticAccount {
        return pessimisticAccountRepository.save(account)
    }

    fun deposit(id: String, amount: Double): PessimisticAccount? {
        val account = lockAccount(id) ?: throw RuntimeException("Account not found or already locked")

        account.balance += amount

        val updatedAccount = pessimisticAccountRepository.save(account)

        unlockAccount(account)

        return updatedAccount
    }

    fun withdraw(id: String, amount: Double): PessimisticAccount? {
        val account = lockAccount(id) ?: throw RuntimeException("Account not found or already locked")

        if (account.balance < amount) {
            unlockAccount(account)
            throw RuntimeException("Insufficient funds")
        }

        account.balance -= amount

        val updatedAccount = pessimisticAccountRepository.save(account)

        unlockAccount(account)

        return updatedAccount
    }

    private fun lockAccount(id: String): PessimisticAccount? {
        val query = Query(Criteria.where("id").`is`(id).and("locked").`is`(false))
        val update = Update().set("locked", true)

        return mongoTemplate.findAndModify(query, update, PessimisticAccount::class.java)
    }

    private fun unlockAccount(account: PessimisticAccount) {
        account.locked = false
        pessimisticAccountRepository.save(account)
    }
}