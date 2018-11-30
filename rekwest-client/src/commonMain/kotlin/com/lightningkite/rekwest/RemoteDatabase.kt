package com.lightningkite.rekwest

import com.lightningkite.kommunicate.*
import com.lightningkite.mirror.archive.*
import com.lightningkite.mirror.info.*
import com.lightningkite.mirror.serialization.SerializationRegistry
import com.lightningkite.mirror.serialization.StringSerializer
import com.lightningkite.mirror.serialization.json.JsonSerializer
import kotlin.reflect.KClass

class RemoteDatabase(
        override val registry: SerializationRegistry,
        val serializer: StringSerializer,
        val url: String,
        val headers: Map<String, List<String>>
) : Database {

    val json = JsonSerializer(registry)

    override fun <T : HasId> table(type: KClass<T>, name: String): Database.Table<T> = object : Database.Table<T> {
        override val classInfo: ClassInfo<T> = registry.classInfoRegistry[type]!!
        val url = this@RemoteDatabase.url + "/" + classInfo.localName

        override suspend fun delete(transaction: Transaction, id: Id) {
            HttpClient.callString(
                    url = url + "/" + id.toUUIDString(),
                    method = HttpMethod.DELETE,
                    headers = headers
            )
        }

        override suspend fun get(transaction: Transaction, id: Id): T? {
            return try {
                HttpClient.callString(
                        url = url + "/" + id.toUUIDString(),
                        method = HttpMethod.GET,
                        headers = headers
                ).let {
                    serializer.read(it, classInfo.type)
                }
            } catch (e: HttpException) {
                if(e.code == 404) null else throw e
            }
        }

        override suspend fun getSure(transaction: Transaction, id: Id): T {
            return HttpClient.callString(
                    url = url + "/" + id.toUUIDString(),
                    method = HttpMethod.GET,
                    headers = headers
            ).let {
                serializer.read(it, classInfo.type)
            }
        }

        override suspend fun insert(transaction: Transaction, model: T): T {
            return HttpClient.callString(
                    url = url,
                    method = HttpMethod.POST,
                    body = HttpBody.string(serializer.contentType, serializer.write(model, classInfo.type)),
                    headers = headers
            ).let {
                serializer.read(it, classInfo.type)
            }
        }

        override suspend fun insertMany(transaction: Transaction, models: Collection<T>): Collection<T> {
            return HttpClient.callString(
                    url = "$url/bulk",
                    method = HttpMethod.POST,
                    body = HttpBody.string(serializer.contentType, serializer.write(models.toList(), classInfo.type.list)),
                    headers = headers
            ).let {
                serializer.read(it, classInfo.type.list)
            }
        }

        override suspend fun modify(transaction: Transaction, id: Id, modifications: List<ModificationOnItem<T, *>>): T {
            return HttpClient.callString(
                    url = url + "/" + id.toUUIDString(),
                    method = HttpMethod.PATCH,
                    body = HttpBody.string(serializer.contentType, serializer.write(modifications, classInfo.type.modification.list)),
                    headers = headers
            ).let {
                serializer.read(it, classInfo.type)
            }
        }

        override suspend fun query(
                transaction: Transaction,
                condition: ConditionOnItem<T>,
                sortedBy: List<SortOnItem<T, *>>,
                continuationToken: String?,
                count: Int
        ): QueryResult<T> {
            val params = mapOf(
                    "condition" to json.write(condition, classInfo.type.condition),
                    "sortedBy" to sortedBy.joinToString(" "){ (if(it.ascending) "" else "-") + it.field.name },
                    "continuationToken" to continuationToken,
                    "count" to count.toString()
            )
            return HttpClient.callString(
                    url = url + "?" + params.entries
                            .mapNotNull { it.key to (it.value ?: return@mapNotNull null) }
                            .joinToString("&"){ it.first + "=" + it.second.urlEncode() },
                    method = HttpMethod.GET,
                    headers = headers
            ).let {
                serializer.read(it, classInfo.type.queryResult)
            }
        }

        override suspend fun update(transaction: Transaction, model: T): T {
            return HttpClient.callString(
                    url = url + "/" + model.id.toUUIDString(),
                    method = HttpMethod.PUT,
                    body = HttpBody.string(serializer.contentType, serializer.write(model, classInfo.type)),
                    headers = headers
            ).let {
                serializer.read(it, classInfo.type)
            }
        }
    }
}