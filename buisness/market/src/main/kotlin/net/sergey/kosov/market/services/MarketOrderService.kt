package net.sergey.kosov.market.services

import net.sergey.kosov.common.exceptions.NotFoundException
import net.sergey.kosov.market.api.AccountApi
import net.sergey.kosov.market.domains.entity.Order
import net.sergey.kosov.market.domains.entity.Order._Order
import net.sergey.kosov.market.domains.entity.Product
import net.sergey.kosov.market.domains.entity.Status
import net.sergey.kosov.market.domains.entity.Status.*
import net.sergey.kosov.market.domains.view.wrappers.OrderFilter
import net.sergey.kosov.market.domains.view.wrappers.OrderViewCreation
import net.sergey.kosov.market.repository.order.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MarketOrderService @Autowired constructor(var orderRepository: OrderRepository,
                                                val productService: ProductService,
                                                val accountApi: AccountApi) : OrderService {
    private val cancelableStatuses = listOf(PROCESSING, CREATED)
    private val completeStatuses = listOf(PROCESSING)
    private val processStatuses = listOf(CREATED)

    override fun create(orderViewCreation: OrderViewCreation, customerName: String): Order {
        val customer = accountApi.getUser(username = customerName)
        val product = productService.findProductById(orderViewCreation.productId)
        return orderRepository.insert(Order(product = product, count = orderViewCreation.count, customer = customer))
    }

    override fun getOrders(customerName: String): List<Order> {
        val customer = accountApi.getUser(customerName)
        return orderRepository.findByQuery(getQueryOrder().addCriteria(Criteria.where(_Order.CUSTOMER).`is`(customer)))
    }

    override fun findOrder(orderId: String, name: String): Order {
        val customer = accountApi.getUser(name)
        val account = accountApi.getAccount(name)
        val findByQuery = orderRepository.findByQuery(getQueryOrder()
                .addCriteria(Criteria.where(_Order.ID).`is`(orderId))
                .addCriteria(Criteria().orOperator(
                        Criteria.where(_Order.CUSTOMER).`is`(customer),
                        Criteria.where("${_Order.product}.${Product._Product.ACCOUNT}").`is`(account))))
        if (findByQuery.size != 1) {
            throw NotFoundException("Can Not Found Order By id = $orderId")
        }
        return findByQuery.first()
    }

    override fun processOrder(orderId: String, name: String): Order {
        val order = findOrder(orderId, name)
        return orderRepository.save(changeStatus(order, PROCESSING))
    }

    override fun completeOrder(orderId: String, name: String): Order {
        val order = findOrder(orderId, name)
        return orderRepository.save(changeStatus(order, COMPLETED))
    }

    override fun cancelOrder(orderId: String, name: String): Order {
        val order = findOrder(orderId, name)
        return orderRepository.save(changeStatus(order, CANCELED))
    }

    override fun findOrders(filter: OrderFilter): List<Order> {
        val customer = accountApi.getUser(filter.customerName)
        val query = getQueryOrder().addCriteria(Criteria.where(_Order.CUSTOMER).`is`(customer))
        if (filter.status != null) {
            query.addCriteria(Criteria.where(_Order.STATUS).`is`(filter.status))
        }
        return orderRepository.findByQuery(query)
    }

    private fun changeStatus(order: Order, status: Status): Order {
        if (order.statusHistory.any { it.first == COMPLETED }) {
            throw IllegalStateException("Нельзя сетить статус после $COMPLETED-- ордер звершен")
        }

        when (status) {
            CREATED -> throw IllegalStateException("Нельзя сетить статус $CREATED, он заполняется только при создании ордера")
            COMPLETED -> {
                if (order.status !in completeStatuses) {
                    throw IllegalArgumentException("Комплитить можно только ордера в статусе $completeStatuses")
                }
                order.completedTime = LocalDateTime.now()
            }
            PROCESSING -> {
                if (order.status !in processStatuses) {
                    throw IllegalArgumentException("Процессить можно только ордера в статусе $processStatuses")
                }
            }
            CANCELED -> {
                if (order.status !in cancelableStatuses) {
                    throw IllegalArgumentException("Отменять можно только ордера в статусе $cancelableStatuses")
                }
            }
        }

        return order.apply {
            this.status = status
            this.statusHistory.add(Pair(status, LocalDateTime.now()))
        }
    }

    private fun getQueryOrder(): Query {
        return Query.query(Criteria.where(_Order.STATUS).ne(Status.IN_A_CART))
    }
}