package de.maggiwuerze.xdccwebloader.service.search.base

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration

@Component
class SearchClient {

    private val objectMapper = jacksonObjectMapper()

    private val httpClient: HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build()

    /**
     * Calls an endpoint like:
     * https://skullxdcc.com/ws.php?sterm=BEAST-X-CHEDDAR&limit_results=25&page=1
     *
     * @param baseUrlTemplate e.g. "https://skullxdcc.com/ws.php?sterm=%s&"
     */
    fun search(
        baseUrlTemplate: String,
        searchTerm: String,
        limitResults: Int = 25,
        page: Int = 1
    ): JsonNode {
        val encodedTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8)
        val base = if (baseUrlTemplate.contains("%s")) {
            baseUrlTemplate.format(encodedTerm)
        } else {
            val joiner = if (baseUrlTemplate.contains("?")) "&" else "?"
            "${baseUrlTemplate}${joiner}sterm=$encodedTerm&"
        }

        val url =
            "${base}limit_results=${URLEncoder.encode(limitResults.toString(), StandardCharsets.UTF_8)}" +
                    "&page=${URLEncoder.encode(page.toString(), StandardCharsets.UTF_8)}"

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(20))
            .header("Accept", "application/json")
            .GET()
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            throw IllegalStateException(
                "Search request failed: HTTP ${response.statusCode()} (url=$url, body=${response.body().take(500)})"
            )
        }

        return objectMapper.readTree(response.body())
    }
}