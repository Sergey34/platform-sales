package net.sergey.kosov.market.domains

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Categories")
data class Category(@Id var id: ObjectId = ObjectId(),
                    var title: String,
                    var description: String = "",
                    var parentId: ObjectId? = null,
                    var characteristics: List<Characteristic> = ArrayList())