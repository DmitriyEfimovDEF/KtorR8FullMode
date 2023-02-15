package com.example.ktorexample

import io.ktor.client.plugins.logging.Logger

fun interface FunLogger : Logger {
    override fun log(message: String)
}