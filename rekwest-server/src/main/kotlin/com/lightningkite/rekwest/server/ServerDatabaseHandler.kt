@file:Suppress("RedundantLambdaArrow")

package com.lightningkite.rekwest.server

import com.lightningkite.mirror.archive.*
import com.lightningkite.mirror.info.*
import com.lightningkite.mirror.serialization.SerializationRegistry
import com.lightningkite.mirror.serialization.json.JsonSerializer
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.routing.*
import io.ktor.util.flattenEntries
import io.ktor.util.pipeline.ContextDsl

class ServerDatabaseHandler(
        val classInfoRegistry: SerializationRegistry,
        val tables: List<ImmutableDatabase.Table<*>>
) {
    val json = JsonSerializer(classInfoRegistry)

    @ContextDsl
    fun Route.tables() {
        for (table in tables) {
            when (table) {
                is Database.Table -> databaseTable(table)
                is Datalog.Table -> datalogTable(table)
                else -> immutableDatabaseTable(table)
            }
        }
    }

    @ContextDsl
    fun Route.databaseTable(table: Database.Table<*>) {
        @Suppress("UNCHECKED_CAST") val untypedTable = table as Database.Table<HasId>
        datalogTable(table)

        put(table.classInfo.localName + "/{id}"){ _ ->
            val id = call.parameters["id"]?.let{ Id.fromUUIDString(it) } ?: throw IllegalArgumentException("You need to supply an ID.")
            val input = call.receive(table.classInfo.type)
            assert(id == input.id)
            val result = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.update(txn, input)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = table.classInfo.typeNullable,
                    value = result
            )
        }

        patch(table.classInfo.localName + "/{id}"){ _ ->
            val id = call.parameters["id"]?.let{ Id.fromUUIDString(it) } ?: throw IllegalArgumentException("You need to supply an ID.")
            val input = call.receive(table.classInfo.type.modification.list)
            val result = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.modify(txn, id, input)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = table.classInfo.type,
                    value = result
            )
        }

        delete(table.classInfo.localName + "/{id}"){ _ ->
            val id = call.parameters["id"]?.let{ Id.fromUUIDString(it) } ?: throw IllegalArgumentException("You need to supply an ID.")
            val result = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.delete(txn, id)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = Unit::class.type,
                    value = result
            )
        }
    }

    @ContextDsl
    fun Route.datalogTable(table: Datalog.Table<*>) {
        @Suppress("UNCHECKED_CAST") val untypedTable = table as Database.Table<HasId>
        immutableDatabaseTable(table)

        post(table.classInfo.localName) { _ ->
            val input = call.receive(table.classInfo.type)
            val result = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.insert(txn, input)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = table.classInfo.type,
                    value = result
            )
        }

        post(table.classInfo.localName + "/bulk") { _ ->
            val input = call.receive(table.classInfo.type.list)
            val result = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.insertMany(txn, input)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = table.classInfo.type.list,
                    value = result.toList()
            )
        }
    }

    @ContextDsl
    fun Route.immutableDatabaseTable(table: ImmutableDatabase.Table<*>) {
        @Suppress("UNCHECKED_CAST") val untypedTable = table as Database.Table<HasId>
        val fieldMap = table.classInfo.fields.associateBy { it.name }

        //List
        get(table.classInfo.localName) { _ ->
            val count = call.request.queryParameters["count"]?.toIntOrNull() ?: 100
            val sortedBy: List<SortOnItem<HasId, *>> = call.request.queryParameters["sort"]?.let {
                it.split('+').mapNotNull {
                    val desc = it.startsWith('-')
                    val paramName = if (desc) {
                        it.substring(1)
                    } else {
                        it
                    }
                    @Suppress("UNCHECKED_CAST")
                    val result: SortOnItem<HasId, *> = SortOnItem(
                            field = fieldMap[paramName] as? FieldInfo<HasId, Comparable<Comparable<*>>>
                                    ?: return@mapNotNull null,
                            ascending = !desc,
                            nullsFirst = false
                    )
                    result
                }
            } ?: listOf()
            val continuationToken = call.request.queryParameters["continuationToken"]
            val condition:ConditionOnItem<Any> = call.request.queryParameters["condition"]?.let {
                @Suppress("UNCHECKED_CAST")
                json.read(it, table.classInfo.type.condition) as ConditionOnItem<Any>
            }
                    ?: call.request.queryParameters.flattenEntries()
                            .filter { it.first != "sort" && it.first != "count" && it.first != "token" }
                            .mapNotNull {
                                @Suppress("UNCHECKED_CAST")
                                val field = fieldMap[it.first] as? FieldInfo<Any, Any?> ?: return@mapNotNull null
                                ConditionOnItem.Equal(
                                        field = field,
                                        value = json.read(it.second, field.type)
                                )
                            }.let{
                                ConditionOnItem.And(it)
                            }

            val results = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.query(txn, condition, sortedBy, continuationToken, count)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = table.classInfo.type.queryResult,
                    value = results
            )
        }

        get(table.classInfo.localName + "/{id}"){ _ ->
            val id = call.parameters["id"]?.let{ Id.fromUUIDString(it) } ?: throw IllegalArgumentException("You need to supply an ID.")
            val result = Transaction(call.unwrappedPrincipal()).use { txn ->
                untypedTable.getSure(txn, id)
            }
            call.respond(
                    status = HttpStatusCode.OK,
                    type = table.classInfo.type,
                    value = result
            )
        }
    }
}