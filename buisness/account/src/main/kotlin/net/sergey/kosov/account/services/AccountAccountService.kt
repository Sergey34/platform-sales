package net.sergey.kosov.account.services

import net.sergey.kosov.account.domains.Account
import net.sergey.kosov.account.domains.User
import net.sergey.kosov.account.domains.ViewCreationAccount
import net.sergey.kosov.account.domains.ViewUpdateAccount
import net.sergey.kosov.account.repositories.AccountRepository
import net.sergey.kosov.common.exceptions.NotFoundException
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountAccountService(var accountRepository: AccountRepository,
                            var userService: UserService) : AccountService {
    @Transactional
    override fun createAccount(viewCreationAccount: ViewCreationAccount): Account {
        val account = Account(marketName = viewCreationAccount.marketName, images = listOf())
        val user = User(fullName = viewCreationAccount.fullName,
                firstName = viewCreationAccount.firstName,
                lastName = viewCreationAccount.lastName,
                email = viewCreationAccount.email,
                birthDay = viewCreationAccount.birthDay,
                country = viewCreationAccount.country,
                gender = viewCreationAccount.gender,
                account = account)
        val createdUser = userService.createUser(user, viewCreationAccount.password)
        return accountRepository.insert(account)
    }

    override fun getAccountByUser(name: String): Account {
        return userService.getUser(name).account
    }

    override fun getAccount(marketName: String): Account {
        val findByQuery = accountRepository.findByQuery(Query(Criteria.where("marketName").`is`(marketName)))
        if (findByQuery.size != 1) {
            throw NotFoundException("can not found account by marketName: $marketName")
        }
        return findByQuery.first()
    }

    override fun updateAccount(name: String, viewUpdateAccount: ViewUpdateAccount): Account {
        val account = accountRepository.findOne(viewUpdateAccount.marketId)
        if (account == userService.getUser(name).account || account == accountRepository.findByQuery(Query(Criteria.where("users").`is`(name))).firstOrNull()) {
            account.description = viewUpdateAccount.marketDescription
            account.marketName = viewUpdateAccount.marketName
            account.images = viewUpdateAccount.marketImages
            return accountRepository.save(account)
        }
        throw NotFoundException("Can not found market")
    }

    override fun addUser(name: String, marketName: String, uuid: String): Account {
        val account = getAccount(marketName)
        if (account.linkToAddedUsers == uuid) {
            val currentUser = userService.getUser(name)
            account.users += currentUser.email
            return accountRepository.save(account)
        }
        throw NotFoundException("Can not found market")
    }
}