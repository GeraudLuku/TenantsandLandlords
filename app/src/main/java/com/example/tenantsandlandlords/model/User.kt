package com.example.tenantsandlandlords.model

data class User(
    var uid: String,
    var accountType: Int // 0 tenant 1 landlord
)