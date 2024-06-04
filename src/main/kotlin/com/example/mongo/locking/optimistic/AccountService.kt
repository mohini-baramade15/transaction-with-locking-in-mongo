package com.example.mongo.locking.optimistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(@Autowired val accountRepository: AccountRepository) {

    fun getAccountById(id: String): Account? {
        return accountRepository.findById(id).orElse(null)
    }

    fun addAccount(account: Account): Account {
        return accountRepository.save(account)
    }

    fun deposit(id: String, amount: Double): Account? {
        val account = accountRepository.findById(id).orElseThrow { RuntimeException("Account not found") }
        account.balance += amount

        return try {
            accountRepository.save(account)
        } catch (exception: OptimisticLockingFailureException) {
            println("Optimistic lock exception: ${exception.message}")
            null
        }
    }

    fun withdraw(id: String, amount: Double): Account? {
        val account = accountRepository.findById(id).orElseThrow { RuntimeException("Account not found") }
        account.balance -= amount

        return try {
            accountRepository.save(account)
        } catch (exception: OptimisticLockingFailureException) {
            println("Optimistic lock exception: ${exception.message}")
            null
        }
    }
}