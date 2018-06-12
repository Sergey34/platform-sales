package net.sergey.kosov.market.repository.product

import net.sergey.kosov.market.domains.entity.Product
import net.sergey.kosov.market.repository.RepositoryQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : MongoRepository<Product, String>, RepositoryQuery<Product>