package cz.kudladev.tamziikmp.core.remote

import io.ktor.client.HttpClient

expect class HttpClientFactory() {
    fun create(): HttpClient
}
