package com.statelesscoder

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import org.apache.commons.lang3.StringEscapeUtils
import java.io.FileInputStream

val desiredModel = "Basic (and reversed card)"


fun main() {
//    val metroNews = MetroNewsSource()
//    val articles = metroNews.getCurrentDescriptions()
//        .map { cleanBody(it) }
//    println(articles.first())
    val translate = TranslateOptions
        .newBuilder()
        .setCredentials(ServiceAccountCredentials.fromStream(FileInputStream("C:\\Users\\john\\projects\\articletoanki\\key.json")))
        .setTargetLanguage("en")
        .build()
        .service

    val result = translate.translate("Hij doet 't wel.", Translate.TranslateOption.sourceLanguage("nl"))
    val decoded = StringEscapeUtils.unescapeHtml4(result.translatedText)
    println(decoded)

}

fun cleanBody(description: String): List<String> {
    return description.split('.', '!', '?')
        .map { it.trim() }
        .filter { it != "" }
}


