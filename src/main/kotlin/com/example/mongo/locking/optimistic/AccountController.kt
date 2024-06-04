package com.example.mongo.locking.optimistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(@Autowired val accountService: AccountService) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: String): ResponseEntity<Account> {
        val account = accountService.getAccountById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(account)
    }

    @PostMapping
    fun addAccount(@RequestBody account: Account): ResponseEntity<Account> {
        return ResponseEntity.ok(accountService.addAccount(account))
    }

    @PostMapping("/{id}/deposit")
    fun deposit(
        @PathVariable id: String,
        @RequestBody transactionRequest: TransactionRequest
    ): ResponseEntity<Account> {
        val account =
            accountService.deposit(id, transactionRequest.amount) ?: return ResponseEntity.status(HttpStatus.CONFLICT)
                .build()
        return ResponseEntity.ok(account)
    }

    @PostMapping("/{id}/withdraw")
    fun withdraw(
        @PathVariable id: String,
        @RequestBody transactionRequest: TransactionRequest
    ): ResponseEntity<Account> {
        val account =
            accountService.withdraw(id, transactionRequest.amount) ?: return ResponseEntity.status(HttpStatus.CONFLICT)
                .build()
        return ResponseEntity.ok(account)
    }
}

data class TransactionRequest(val amount: Double)