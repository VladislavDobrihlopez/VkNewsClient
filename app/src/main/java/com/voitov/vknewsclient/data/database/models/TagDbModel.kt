package com.voitov.vknewsclient.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_tags")
data class TagDbModel(
    @PrimaryKey
    val name: String
)