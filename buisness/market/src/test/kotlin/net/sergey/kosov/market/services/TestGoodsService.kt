package net.sergey.kosov.market.services

import com.sun.security.auth.UserPrincipal
import net.sergey.kosov.market.api.StatisticApi
import net.sergey.kosov.market.configuration.ConfigurationFeign
import net.sergey.kosov.market.domains.Goods
import org.bson.types.ObjectId
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextHierarchy(ContextConfiguration(classes = [GoodsServiceConfig::class, ConfigurationFeign::class]))
class TestGoodsService {
    @Autowired
    private lateinit var goodsService: GoodService
    @MockBean
    private lateinit var statisticService: StatisticApi
    private var goodsChartIds: List<ObjectId>? = null
    @Before
    fun before() {
        goodsChartIds = (1..10).mapTo(ArrayList()) { goodsService.createGoods(title = "name$it", description = "description$it").id }
        Mockito.doReturn(goodsChartIds).`when`(statisticService).getChart("name", 100)
    }

    @Test
    fun findGoodsById() {
        val goodsCreated = goodsService.createGoods(title = "name", description = "description")
        val goods: Goods = goodsService.findGoodsById(id = goodsCreated.id)
        Assert.assertEquals(goodsCreated.id, goods.id)
    }

    @Test
    fun getGoods4Chart() {
        var goodsList: List<Goods> = goodsService.getGoods4Chart(UserPrincipal("name"))
        Assert.assertTrue(goodsList.all { goodsChartIds!!.contains(it.id) })
    }

    @Test
    fun disabledGoods() {
        val goodsCreated = goodsService.createGoods(title = "name2", description = "description2")
        val disabledGoods = goodsService.disabledGoods(goodsCreated)
        Assert.assertEquals(goodsCreated.id, disabledGoods.id)
        Assert.assertEquals(false, disabledGoods.enabled)
    }

    @Test
    fun enabledGoods() {
        val goodsCreated = goodsService.createGoods(title = "name3", description = "description3")
        val enabledGoods = goodsService.enabledGoods(goodsCreated)
        Assert.assertEquals(goodsCreated.id, enabledGoods.id)
        Assert.assertEquals(true, enabledGoods.enabled)
    }

}

