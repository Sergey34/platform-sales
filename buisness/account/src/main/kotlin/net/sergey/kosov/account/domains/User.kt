package net.sergey.kosov.account.domains

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import net.sergey.kosov.common.annotations.NoArgs
import net.sergey.kosov.common.serializers.ObjectIdSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@NoArgs
@Document(collection = "users")
data class User(@Id @JsonSerialize(using = ObjectIdSerializer::class) var id: ObjectId = ObjectId(),
                var fullName: String,
                var firstName: String,
                var lastName: String,
                @Indexed(unique = true, name = "email_index")
                var email: String,
                var password: String,
                var birthDay: LocalDate,
                var country: String,
                var gender: Gender,
                var account: Account
) {
    data class User(var username: String,
                    var password: String) {

    }
}

