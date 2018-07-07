package net.sergey.kosov.account.services

import net.sergey.kosov.account.api.AuthUserClient
import net.sergey.kosov.account.domains.User
import net.sergey.kosov.account.repositories.UserRepository
import net.sergey.kosov.common.exceptions.NotFoundException
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class AccountUserService(var userRepository: UserRepository,
                         var authUserClient: AuthUserClient) : UserService {
    override fun createUser(user: User): User {
        val authUser = User.User(username = user.email, password = user.password)
        authUserClient.createUser(authUser)
        user.password = ""
        return userRepository.insert(user)
    }

    override fun getUser(username: String): User {
        val findByQuery = userRepository.findByQuery(Query(Criteria.where("name").`is`(username)))
        if (findByQuery.size != 1) {
            throw NotFoundException("can not found user by name: $username")
        }
        return findByQuery.first()
    }
}

