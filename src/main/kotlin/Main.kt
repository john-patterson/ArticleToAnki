package com.statelesscoder

import kotlinx.coroutines.runBlocking

val desiredModel = "Basic (and reversed card)"

fun main() {
    val translator = Translator(System.getProperty("google.api.key"),
        TranslatorLanguages.Nederlands,
        TranslatorLanguages.English)
    val feed = MetroNewsSource()
    val ankiApi = AnkiApi("http", "localhost", 8765)

    val sentences = feed.getCurrentDescriptions()
        .flatMap { cleanBody(it) }
        .map { Pair(it, translator.translate(it)) }

    runBlocking {
        val newDeck = ankiApi.createDeck("Metro Nieuws Deck")
        val model = ankiApi.modelNamesAndIds().first { it.name == desiredModel }

        sentences
            .map { NoteFields(it.first, it.second) }
            .map { Note(newDeck, model, it) }
            .forEach { ankiApi.addNote(it)}
    }
}

fun cleanBody(description: String): List<String> {
    return description.split('.', '!', '?')
        .map { it.trim() }
        .filter { it != "" }
}


