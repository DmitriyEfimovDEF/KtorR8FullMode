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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ktorexample.ui.theme.KtorExampleTheme

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
                    val vm by viewModels<MainViewModel>()
                    val state = vm.state.collectAsState(RequestState.Initial)

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(1F)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Text(
                                text = state.value.requestInfo,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomStart)
                            )
                        }

                        TextField(
                            value = state.value.requestParam,
                            label = {
                                Text(text = "input parameter string or number")
                            },
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
