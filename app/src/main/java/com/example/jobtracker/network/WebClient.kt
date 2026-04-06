package com.example.jobtracker.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.jsoup.Jsoup
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText

object WebClient {
    // 1. Reusable Ktor client instance (instantiated only once)
    private val client = HttpClient(Android)

    // 2. Pure Network Function: Only handles the HTTP GET request
    // Can be reused for any future features requiring raw HTML
    suspend fun getHtmlString(urlString: String): String? {
        return try {
            client.get(urlString) {
                // Injecting a User-Agent to bypass basic bot-protection firewalls
                header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
            }.bodyAsText()
        } catch (e: Exception) {
            null
        }
    }

    // 3. Pure Extraction Function: Only handles DOM parsing
    // Completely separated from the network layer
    fun extractLogo(htmlBody: String, baseUrl: String): String? {
        return try {
            // Injecting the baseUrl allows Jsoup's abs:href to automatically resolve relative asset paths into absolute URLs
            val document = Jsoup.parse(htmlBody, baseUrl)
            document.select("link[rel=icon], link[rel=shortcut icon], link[rel=apple-touch-icon]").first()?.attr("abs:href")
        } catch (e: Exception) {
            null
        }
    }
}