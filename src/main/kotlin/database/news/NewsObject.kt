package com.database.news

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object NewsObject : Table("news") {
    val title = varchar("title", 45)
    val description = varchar("description",500)

    fun insert(newsDTO: NewsDTO) {
        transaction {
            insert {
                it[title] = newsDTO.title
                it[description] = newsDTO.description
            }
        }
    }
    fun fetchNews(title: String, description: String): NewsDTO? {
        return try {
            val result: NewsDTO? = transaction {
                NewsObject
                    .select { (NewsObject.title eq title) and
                            (NewsObject.description eq description) }
                    .singleOrNull()
                    ?.let { row ->
                        NewsDTO(
                            title = row[NewsObject.title],
                            description = row[NewsObject.description]
                        )
                    }
            }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}