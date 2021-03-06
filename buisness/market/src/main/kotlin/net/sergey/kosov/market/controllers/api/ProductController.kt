package net.sergey.kosov.market.controllers.api

import net.sergey.kosov.market.domains.entity.Product
import net.sergey.kosov.market.domains.view.wrappers.ProductFilter
import net.sergey.kosov.market.domains.view.wrappers.ProductViewCreation
import net.sergey.kosov.market.services.ProductService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class ProductController(val productService: ProductService) {
    @GetMapping("/product/{id}")
    fun findProductById(@PathVariable("id") id: String, principal: Principal?): Product {
        return productService.findProductById(id)
    }

    @GetMapping("/products")
    fun getProducts(): List<Product> {
        return productService.findProducts()
    }

    @GetMapping("/products/market/{market_name}")
    fun getProducts4Market(@PathVariable("market_name") marketName: String): List<Product> {
        return productService.getProducts4Market(marketName)
    }

/*    @PostMapping("/product/{id}/disabled")
    fun disabledProduct(@PathVariable("id") id: String): Product { //todo   валидация того что пользователь принадлежит аккаунту продукта
        return productService.disabledProduct(id)
    }

    @PostMapping("/product/{id}/enabled")
    fun enabledProduct(@PathVariable("id") id: String): Product { //todo   валидация того что пользователь принадлежит аккаунту продукта
        return productService.enabledProduct(id)
    }*/

    @PutMapping("/product")
    fun createProduct(@RequestBody product: ProductViewCreation, principal: Principal): Product {
        return productService.createProduct(product, principal.name)
    }

    @PostMapping("/products")
    fun findProducts(@RequestBody filter: ProductFilter): List<Product> {
        return productService.findProducts(filter)
    }
}