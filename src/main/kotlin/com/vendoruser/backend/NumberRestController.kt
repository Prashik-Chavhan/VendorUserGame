package com.vendoruser.backend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class NumberRestController(private val repository: GeneratedNumberRepository) {

    @GetMapping("/history/{gameId}")
    fun getHistory(@PathVariable gameId: String): List<GeneratedNumber> {
        return repository.findByGameId(gameId)
    }

    @PostMapping("/generate-number")
    fun generateNumber(@RequestParam gameId: String): GeneratedNumber {
        val number = (0..99).random()
        val record = GeneratedNumber(number = number, gameId = gameId)
        repository.save(record)
        return record
    }
}