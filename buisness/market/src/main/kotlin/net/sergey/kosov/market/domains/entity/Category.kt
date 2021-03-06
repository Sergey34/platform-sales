package net.sergey.kosov.market.domains.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import net.sergey.kosov.common.annotations.NoArgs
import net.sergey.kosov.common.serializers.ObjectIdSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@NoArgs
@Document(collection = "Categories")
data class Category(@Id @JsonSerialize(using = ObjectIdSerializer::class) var id: ObjectId = ObjectId(),
                    @Indexed(name = "category_title")
                    var title: String,
                    var description: String = "",
                    @Indexed(name = "category_parentId")
                    var parent: Category? = null,
                    var account: Account? = null,
                    var characteristics: List<Characteristic> = ArrayList()) : Iterable<Category> {

    override fun iterator(): Iterator<Category> {
        return CategoryIterator(this)

    }

    class CategoryIterator(var category: Category?) : Iterator<Category> {
        override fun hasNext(): Boolean {
            return category != null
        }

        override fun next(): Category {
            if (!hasNext()) throw NoSuchElementException()
            val returned = category!! // checked in previous line. hasNext()
            category = returned.parent
            return returned
        }

    }

    object _Category {
        const val TITLE = "title"
        const val ID = "id"
        const val DESCRIPTION = "description"
        const val PARENT = "parent"
        const val ACCOUNT = "account"
        const val CHARACTERISTICS = "characteristics"
    }
}
