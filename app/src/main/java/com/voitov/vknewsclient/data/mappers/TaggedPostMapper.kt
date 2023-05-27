package com.voitov.vknewsclient.data.mappers

import android.util.Log
import com.voitov.vknewsclient.data.database.models.TaggedPostItemDbModel
import com.voitov.vknewsclient.domain.entities.TaggedPostItem
import javax.inject.Inject

class TaggedPostMapper @Inject constructor() {
    fun mapEntityToDbModel(entity: TaggedPostItem): TaggedPostItemDbModel {
        Log.d("TEST_MAPPER", entity.tag.name)
        return TaggedPostItemDbModel(
            tag = entity.tag.name,
            communityId = entity.postItem.communityId,
            authorName = entity.postItem.authorName,
            date = entity.postItem.date,
            contentText = entity.postItem.contentText
        )
    }
}