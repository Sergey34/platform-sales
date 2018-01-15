package net.sergey.kosov.market.services

import net.sergey.kosov.market.domains.Goods
import net.sergey.kosov.market.repository.GoodsRepository
import org.bson.types.ObjectId
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service

@Service
class MarketGoodService(var goodsRepository: GoodsRepository) : GoodService {

    override fun createGoods(description: String, title: String): Goods {
        val goods = Goods(title = title, description = description)
        return goodsRepository.save(goods) ?: throw IllegalStateException("не смог сохранить")
    }

    override fun findGoodsById(id: ObjectId): Goods {
        return goodsRepository.findOne(id) ?: throw ChangeSetPersister.NotFoundException()
    }

    override fun getGoods4Chart(): List<Goods> {
        //        запросить у сервиса статистики n первых в рейтинге id продуктов, и достать их.
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGoods4ChartForUser(userId: String): List<Goods> {
        //        запросить у сервиса статистики n первых в рейтинге id продуктов для юзера, и достать их.
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disabledGoods(goodsId: ObjectId): Goods {
        return disabledGoods(findGoodsById(goodsId))
    }

    override fun enabledGoods(goodsId: ObjectId): Goods {
        return enabledGoods(findGoodsById(goodsId))
    }

    override fun disabledGoods(goods: Goods): Goods {
        return goodsRepository.save(goods.apply { goods.enabled = false })
    }

    override fun enabledGoods(goods: Goods): Goods {
        return goodsRepository.save(goods.apply { goods.enabled = true })
    }
}
