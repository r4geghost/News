package com.dyusov.news.data.mapper

import com.dyusov.news.data.local.ArticleDbModel
import com.dyusov.news.data.remote.NewsResponseDto
import com.dyusov.news.domain.entity.Article
import java.text.SimpleDateFormat
import java.util.Locale

fun NewsResponseDto.toDbModels(topic: String): List<ArticleDbModel> {
    return articles.map {
        ArticleDbModel(
            title = it.title,
            description = it.description,
            url = it.url,
            imageUrl = it.urlToImage,
            source = it.source.name,
            topic = topic,
            publishedAt = it.publishedAt.toTimestamp()
        )
    }
}

fun List<ArticleDbModel>.toEntities(): List<Article> {
    return map {
        Article(
            title = it.title,
            description = it.description,
            imageUrl = it.imageUrl,
            source = it.source,
            publishedAt = it.publishedAt,
            url = it.url
        )
    }.distinct() // delete duplicates
}

private fun String.toTimestamp(): Long {
    val dateFormatter = SimpleDateFormat(
        /* pattern = */ "yyyy-MM-dd'T'HH:mm:ss'Z'",
        /* locale = */ Locale.getDefault()
    )
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}