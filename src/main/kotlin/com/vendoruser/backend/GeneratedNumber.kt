package com.vendoruser.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class GeneratedNumber(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val number: Int,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val gameId: String
)
