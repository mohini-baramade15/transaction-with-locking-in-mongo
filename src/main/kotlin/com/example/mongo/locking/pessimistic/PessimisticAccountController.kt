package com.example.mongo.locking.pessimistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pessimistic/accounts")
class PessimisticAccountController(@Autowired val pessimisticAccountService: PessimisticAccountService) {
    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: String): ResponseEntity<PessimisticAccount> {
        val account = pessimisticAccountService.getAccountById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(account)
    }

    @PostMapping
    fun addAccount(@RequestBody pessimisticAccount: PessimisticAccount): ResponseEntity<PessimisticAccount> {
        return ResponseEntity.ok(pessimisticAccountService.addAccount(pessimisticAccount))
    }

    @PostMapping("/{id}/deposit")
    fun deposit(@PathVariable id: String, @RequestBody transaction: TransactionRequest): ResponseEntity<PessimisticAccount> {
        return try {
            val updatedAccount = pessimisticAccountService.deposit(id, transaction.amount)
            ResponseEntity.ok(updatedAccount)
        } catch (e: RuntimeException) {
            ResponseEntity.status(409).body(null)
        }
    }

    @PostMapping("/{id}/withdraw")
    fun withdraw(@PathVariable id: String, @RequestBody transaction: TransactionRequest): ResponseEntity<PessimisticAccount> {
        return try {
            val updatedAccount = pessimisticAccountService.withdraw(id, transaction.amount)
            ResponseEntity.ok(updatedAccount)
        } catch (e: RuntimeException) {
            ResponseEntity.status(409).body(null)
        }
    }
}

data class TransactionRequest(val amount: Double)