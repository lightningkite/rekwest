//package com.lightningkite.kotlinx.server.base.security
//
//import com.lightningkite.kotlinx.reflection.KxClass
//import com.lightningkite.kotlinx.server.ConditionOnItem
//import com.lightningkite.kotlinx.server.ModificationOnItem
//import com.lightningkite.kotlinx.server.SortOnItem
//import com.lightningkite.kotlinx.persistence.Transaction
//import com.lightningkite.kotlinx.server.base.orm.Database
//import com.lightningkite.kotlinx.server.base.orm.Model
//
//class SecureDatabase<T: Model<ID>, ID>(val type:KxClass<T>, val wraps: SecureDatabase<T, ID>) : Database<T, ID> {
//    override suspend fun get(transaction: Transaction, id: ID): T = wraps.get(transaction, id).apply {
//        type.readFilter(transaction.untypedUser, this)
//    }
//
//    override suspend fun insert(transaction: Transaction, model: T): T {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun update(transaction: Transaction, model: T): T {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun modify(transaction: Transaction, id: ID, modifications: List<ModificationOnItem<T, *>>): T {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun query(transaction: Transaction, condition: ConditionOnItem<T>, sortedBy: List<SortOnItem<T, *>>, after: T?, count: Int): List<T> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun delete(transaction: Transaction, id: ID) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
