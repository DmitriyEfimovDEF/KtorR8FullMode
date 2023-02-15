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
    private val requestInfo = MutableStateFlow(RequestUrl)
    private val client = ktorClient { all -> requestInfo.tryEmit(all) }

    val state = combine(
        parameter,
        requestInfo
    ) { param, request ->
        RequestState(request, param)
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
    val requestInfo: String = "",
    val requestParam: String = "",
) {
    companion object {
        val Initial = RequestState(
            requestInfo = "Request info",
            requestParam = ""
        )
    }
}

const val RequestUrl: String = "https://www.google.com/search"