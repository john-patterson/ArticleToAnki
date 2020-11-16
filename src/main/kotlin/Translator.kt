package com.statelesscoder

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import org.apache.commons.lang3.StringEscapeUtils
import java.io.FileInputStream


enum class TranslatorLanguages {
    English,
    Nederlands,
}
class Translator(apiKeyPath: String, source: TranslatorLanguages, target: TranslatorLanguages) {
    private val credentials = ServiceAccountCredentials.fromStream(FileInputStream(apiKeyPath))
    private val translator = TranslateOptions.newBuilder()
        .setCredentials(credentials)
        .setTargetLanguage(languageToGoogleCode(target))
        .build()
        .service
    private val sourceLanguage = Translate.TranslateOption.sourceLanguage(languageToGoogleCode(source))

    fun translate(textToTranslate: String): String {
        val translation = translator.translate(textToTranslate, sourceLanguage)
        return StringEscapeUtils.unescapeHtml4(translation.translatedText)
    }

    private fun languageToGoogleCode(lang: TranslatorLanguages): String {
        return when (lang) {
            TranslatorLanguages.English -> "en"
            TranslatorLanguages.Nederlands -> "nl"
        }
    }

}