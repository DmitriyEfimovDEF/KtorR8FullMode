package com.example.ktorexample.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.takeFrom

suspend inline fun <reified T> HttpClient.get(
    path: String,
    parameters: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): T {
    val request = createGetRequest(path, parameters, headers)
    return execute(request)
}

fun createGetRequest(
    path: String,
    parameters: Map<String, Any> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): HttpRequestBuilder.() -> Unit {
    return {
        method = HttpMethod.Get
        url {
            takeFrom(path)
            headers.forEach { header(it.key, it.value) }
            parameters.forEach { parameter(it.key, it.value) }
        }
    }
}

suspend inline fun <reified T> HttpClient.execute(block: HttpRequestBuilder.() -> Unit) =
    request(block).body() as T
