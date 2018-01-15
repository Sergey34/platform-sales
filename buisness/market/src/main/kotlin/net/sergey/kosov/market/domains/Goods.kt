package net.sergey.kosov.market.domains

import org.bson.types.ObjectId

data class Goods(var id: ObjectId = ObjectId(), var description: String = "", var title: String = "")