package com.statelesscoder

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import com.google.*
import kotlin.reflect.typeOf

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
    }
}

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


suspend fun addNote(note: Note): Double {
    val result = client.post<String>(scheme = "http", "localhost", port = 8765) {
        contentType(ContentType.Application.Json)
        body = AnkiRequest("addNote", mapOf(Pair("note", note)))
    }

    val parsed = Gson().fromJson<AnkiWebResult<Double>>(result, AnkiWebResult::class.java)
    return parsed.result

}

suspend fun modelNamesAndIds(): List<Model> {
    val result = client.get<String>(scheme = "http", host = "localhost", port = 8765) {
        contentType(ContentType.Application.Json)
        body = AnkiRequest<String>("modelNamesAndIds")
    }

    val parsed = Gson().fromJson<AnkiWebResult<Map<String, Double>>>(result, AnkiWebResult::class.java)
    return parsed.result.entries
        .map { Model(it.key, it.value) }
}

suspend fun createDeck(name: String): Deck {
    val result = client.post<String>(scheme = "http", "localhost", port = 8765) {
        contentType(ContentType.Application.Json)
        body = AnkiRequest("createDeck", mapOf(Pair("deck", name)))
    }

    val parsed = Gson().fromJson<AnkiWebResult<Double>>(result, AnkiWebResult::class.java)
    return Deck(name, parsed.result)
}

suspend fun deckNamesAndIds(): List<Deck>  {
    val result = client.get<String>(scheme = "http", host = "localhost", port = 8765) {
        contentType(ContentType.Application.Json)
        body = AnkiRequest<String>("deckNamesAndIds")
    }

    val parsed = Gson().fromJson<AnkiWebResult<Map<String, Double>>>(result, AnkiWebResult::class.java)
    return parsed.result.entries
        .map { Deck(it.key, it.value) }
}