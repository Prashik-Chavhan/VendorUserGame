package com.vendoruser.backend

import org.springframework.data.jpa.repository.JpaRepository

interface GeneratedNumberRepository : JpaRepository<GeneratedNumber, Long> {
    fun findByGameId(gameId: String): List<GeneratedNumber>
}