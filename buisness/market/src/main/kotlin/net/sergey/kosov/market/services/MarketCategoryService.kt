package net.sergey.kosov.market.services

import net.sergey.kosov.common.exceptions.NotFoundException
import net.sergey.kosov.market.api.AccountApi
import net.sergey.kosov.market.domains.entity.Account
import net.sergey.kosov.market.domains.entity.Category
import net.sergey.kosov.market.domains.entity.Characteristic
import net.sergey.kosov.market.domains.view.wrappers.CategoryViewCreation
import net.sergey.kosov.market.repository.category.CategoryRepository
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class MarketCategoryService(var categoryRepository: CategoryRepository,
                            var accountApi: AccountApi) : CategoryService {

    override fun create(categoryViewCreation: CategoryViewCreation, ownerName: String): Category {
        val account: Account = accountApi.getAccount(ownerName)
        val parentCategory = findParentCategory(categoryViewCreation.parentId)
        val category = Category(title = categoryViewCreation.title,
                description = categoryViewCreation.description,
                account = account,
                parent = parentCategory)
        return categoryRepository.insert(category)
    }

    private fun findParentCategory(parentId: String?): Category? {
        return if (parentId != null) {
            categoryRepository.findOne(parentId)
        } else {
            null
        }
    }

    override fun setCharacteristics(categoryId: String, characteristics: List<Characteristic>, name: String): Category {
        val category = findCategoryById(categoryId, name)
        category.characteristics = characteristics
        return categoryRepository.save(category)
    }

    override fun findCategoryById(categoryId: String, name: String): Category {
        val account: Account = accountApi.getAccount(name)
        val query = Query(Criteria.where("id").`is`(categoryId))
                .addCriteria(Criteria.where("account").`is`(account).orOperator(Criteria.where("account").`is`(null)))
        val categories = categoryRepository.findByQuery(query)

        if (categories.size == 1) {
            return categories[1]
        } else {
            throw NotFoundException("can not fount category by id = $categoryId")
        }
    }

    override fun getCategories(name: String): List<Category> {
        val account: Account = accountApi.getAccount(name)
        val query = Query(Criteria.where("account").`is`(account).orOperator(Criteria.where("account").`is`(null)))
        return categoryRepository.findByQuery(query)
    }
}