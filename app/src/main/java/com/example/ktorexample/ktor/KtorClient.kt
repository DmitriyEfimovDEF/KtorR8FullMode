package com.example.ktorexample.ktor

import android.util.Log
import com.example.ktorexample.ktor.FunLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun ktorClient(logAction: (String) -> Unit) = HttpClient(Android) {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(Logging) {
        logger = FunLogger { msg ->
            Log.v("Logger Ktor ->", msg)
            logAction.invoke(msg)
        }
        level = LogLevel.INFO
    }

    install(ResponseObserver) {
        onResponse { response ->
            Log.d("HTTP status:", "${response.status.value}")
        }
    }
}
