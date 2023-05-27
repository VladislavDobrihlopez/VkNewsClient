package com.voitov.vknewsclient.data.mappers

import com.voitov.vknewsclient.data.database.models.TagDbModel
import com.voitov.vknewsclient.domain.entities.ItemTag
import javax.inject.Inject

class TagsMapper @Inject constructor() {
    fun mapEntityToDbModel(item: ItemTag): TagDbModel {
        return TagDbModel(name = item.name)
    }

    fun mapDbModelToEntity(dbModel: TagDbModel): ItemTag {
        return ItemTag(name = dbModel.name)
    }
}