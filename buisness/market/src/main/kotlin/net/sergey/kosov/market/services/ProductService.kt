package net.sergey.kosov.market.services

import net.sergey.kosov.market.domains.entity.Characteristic
import net.sergey.kosov.market.domains.entity.Product
import net.sergey.kosov.market.domains.view.wrappers.ProductFilter
import net.sergey.kosov.market.domains.view.wrappers.ProductViewCreation

interface ProductService {
    fun findProductById(id: String): Product
    fun disabledProduct(id: String): Product
    fun enabledProduct(id: String): Product
    fun createProduct(productViewCreation: ProductViewCreation, userName: String): Product
    fun findProducts(filter: ProductFilter): List<Product>
    fun setCharacteristic(characteristics: List<Characteristic>, id: String): Product
    fun findProducts(): List<Product>
    fun getProducts4Market(marketName: String): List<Product>
}