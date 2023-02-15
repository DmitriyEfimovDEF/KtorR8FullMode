# KtorR8FullMode

Repository to reproduce bug with environment: 
kotlin version 1.8.10,
ktor 2.2.3,
android.enableR8.fullMode=true in gragle.properties

In this case, after set android.enableR8.fullMode to true, ktor httpClient instance send request with empty parameters.
It happens because method encodeURLParameter() in package */ktor-http-jvm-2.2.2-sources.jar!/commonMain/io/ktor/http/Codecs.kt:116 returns empty string.
