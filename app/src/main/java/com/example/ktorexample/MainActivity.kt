package com.example.ktorexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktorexample.ui.theme.KtorExampleTheme
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val vm by viewModels<ExampleViewModel>()
                    val state = vm.state.collectAsState(RequestState())
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(modifier = Modifier.weight(1F)) {
                            Text(text = state.value.requestFact)
                        }

                        TextField(
                            value = state.value.requestParam,
                            onValueChange = vm::onParamChanged,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                vm.sendRequest()
                            }),
                        )

                        Button(onClick = vm::sendRequest) {
                            Text(text = "Send")
                        }
                    }
                }
            }
        }
    }
}

class ExampleViewModel : ViewModel() {


    private val parameter = MutableStateFlow("")
    private val total = MutableStateFlow("")
    private val client = ktorClient { all ->
        total.tryEmit(all)
    }

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
            client.get(RequestUrl) {
                parameter("page", 1)
                parameter("species__name", parameter.value)
            }
        }
    }
}

@Immutable
data class RequestState(
    val requestFact: String = "",
    val requestParam: String = "",
)

const val RequestUrl = "http://158.160.56.133/app/pet"