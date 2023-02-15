package com.example.ktorexample

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktorexample.ktor.ktorClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.takeFrom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val parameter = MutableStateFlow("")
    private val total = MutableStateFlow("")
    private val client = ktorClient { all -> total.tryEmit(all) }

    val state = combine(
        parameter,
        total
    ) { param, body ->
        RequestState(body, param)
    }

    fun onParamChanged(p: String) {
        parameter.tryEmit(p)
    }

    fun sendRequest() {
        viewModelScope.launch {
            val request: HttpRequestBuilder.() -> Unit = {
                method = HttpMethod.Get
                url {
                    takeFrom(RequestUrl)
                    parameter("q", parameter.value)
                }
            }
            client.request(request)
        }
    }
}

@Immutable
data class RequestState(
    val requestFact: String = "",
    val requestParam: String = "",
)

const val RequestUrl: String = "https://www.google.com/search"