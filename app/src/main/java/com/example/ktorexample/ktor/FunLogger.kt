package com.example.ktorexample.ktor

import io.ktor.client.plugins.logging.Logger

fun interface FunLogger : Logger {
    override fun log(message: String)
}