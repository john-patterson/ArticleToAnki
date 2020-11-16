package com.statelesscoder

import com.apptastic.rssreader.RssReader
import org.jsoup.Jsoup
import java.util.stream.Collectors

class MetroNewsSource {
    private val url = "https://www.metronieuws.nl/in-het-nieuws/feed"
    private val reader = RssReader()

    fun getCurrentDescriptions(): List<String> {
        val feed = reader.read(url)
        return feed
            .map { it.description }
            .filter { it.isPresent }
            .map { Jsoup.parse(it.get()).text() }
            .collect(Collectors.toList())
    }
}