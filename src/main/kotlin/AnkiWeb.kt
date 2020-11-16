package com.statelesscoder

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*

data class AnkiRequest<T>(@SerializedName("action") val action: String,
                       @SerializedName("params") val params: Map<String, T> = emptyMap()) {
    @SerializedName("version") val version = 6
}


data class AnkiWebResult<T>(
    @SerializedName("result") val result: T,
    @SerializedName("error") val error: String?)
data class Deck(val name: String, val id: Double)
data class Model(val name: String, val id: Double)

data class NoteFields(
    @SerializedName("Front") val front: String,
    @SerializedName("Back") val back: String
)
data class Note(val deck: Deck,
                val model: Model,
                @SerializedName("fields") val fields: NoteFields) {
    @SerializedName("deckName") val deckName = deck.name
    @SerializedName("modelName") val modelName = model.name
}




class AnkiApi(private val scheme: String, private val hostName: String, private val port: Int) {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    private suspend fun <T, K> get(requestBody: AnkiRequest<K>): AnkiWebResult<T> {
        val result = this.client.get<String>(this.scheme, this.hostName, this.port) {
            contentType(ContentType.Application.Json)
            body = requestBody
        }

        return Gson().fromJson<AnkiWebResult<T>>(result, AnkiWebResult::class.java)
    }

    private suspend fun <T, K> post(requestBody: AnkiRequest<K>): AnkiWebResult<T> {
        val result = this.client.post<String>(this.scheme, this.hostName, this.port) {
            contentType(ContentType.Application.Json)
            body = requestBody
        }

        return Gson().fromJson<AnkiWebResult<T>>(result, AnkiWebResult::class.java)
    }

    suspend fun addNote(note: Note): Double {
        val response: AnkiWebResult<Double> = post(AnkiRequest("addNote", mapOf(Pair("note", note))))
        return response.result
    }

    suspend fun modelNamesAndIds(): List<Model> {
        val response: AnkiWebResult<Map<String, Double>> = get(AnkiRequest<String>("modelNamesAndIds"))
        return response.result.entries
            .map { Model(it.key, it.value) }
    }

    suspend fun createDeck(name: String): Deck {
        val response: AnkiWebResult<Double> = post(AnkiRequest("createDeck", mapOf(Pair("deck", name))))
        return Deck(name, response.result)
    }

    suspend fun deckNamesAndIds(): List<Deck>  {
        val response: AnkiWebResult<Map<String, Double>> = get(AnkiRequest<String>("deckNamesAndIds"))
        return response.result.entries
            .map { Deck(it.key, it.value) }
    }
}