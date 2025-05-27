package com.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val login: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String,
    val role: String
    )

@Serializable
data class RegisterResponseRemote(
    val token: String
)