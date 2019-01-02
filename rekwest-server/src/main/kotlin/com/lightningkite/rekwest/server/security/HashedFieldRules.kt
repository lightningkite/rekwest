package com.lightningkite.rekwest.server.security

import com.lightningkite.mirror.archive.database.secure.SecureSuspendMap
import com.lightningkite.mirror.archive.model.Operation
import com.lightningkite.mirror.info.FieldInfo
import com.lightningkite.rekwest.server.security.HasPassword.Companion.HASHED_PLACEHOLDER
import de.mkammerer.argon2.Argon2Factory
import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.resources.ConfigurationBuilder
import me.gosimple.nbvcxz.resources.DictionaryBuilder
import kotlin.IllegalArgumentException

interface HasPassword {
    var password: String

    companion object {
        const val HASHED_PREFIX = "hashed: "
        const val HASHED_PLACEHOLDER = "<password>"
        val argon2 = Argon2Factory.create()
        fun <K, T : HasPassword, USER> rules(): SecureSuspendMap.Rules<K, T, USER> {
            return SecureSuspendMap.Rules(
                    mask = { _, it -> it.apply { password = HASHED_PLACEHOLDER } },
                    validate = { _, value ->
                        if (value.password == HASHED_PLACEHOLDER) {
                            throw IllegalArgumentException("You can't use a placeholder as a password.")
                        } else if (value.password.startsWith(HASHED_PREFIX)) {
                            value.password = HASHED_PREFIX + argon2.hash(10, 976562, 4, value.password)
                        }
                        value
                    },
                    operation = { _, op ->
                        if (op is Operation.Fields) {
                            op.copy(changes = op.changes.mapValues {
                                val k = it.key
                                if (k.name == "password") {
                                    val v = it.value
                                    if (v is Operation.Set) {
                                        Operation.Set(HASHED_PREFIX + argon2.hash(10, 976562, 4, v.value as String))
                                    } else {
                                        throw IllegalArgumentException()
                                    }
                                } else it.value
                            })
                        } else op
                    }
            )
        }
    }
}

//class HashedFieldRules<K, V : Any, USER>(
//        override val variable: FieldInfo<V, String>,
//        val getIdentifiers: (V) -> List<String>,
//        val atLeastEntropy: (V?) -> Int = { 30 /*Takes roughly a billion guesses*/ }
//) : SecureSuspendMap.Rules<K, V, USER>() {
//
//    val hasher = Argon2Factory.create()
//
//    companion object {
//    }
//
//    override suspend fun query(user: USER?) {
//        throw IllegalAccessException()
//    }
//
//    override suspend fun read(user: USER?, justInserted: Boolean, currentState: T): String {
//        return HASHED_PLACEHOLDER
//    }
//
//    override suspend fun write(user: USER?, currentState: T?, newState: String): String {
//        if (newState.startsWith(HASHED_PREFIX)) return newState
//        if (newState.equals(HASHED_PLACEHOLDER)) return currentState?.let { variable.get(it) }
//                ?: throw IllegalArgumentException()
//        judgePassword(currentState, newState)
//        return HASHED_PREFIX + hasher.hash(10, 65536, 1, newState)
//    }
//
//    private fun judgePassword(item: T?, password: String) {
//        //Check security of password.
//        // Create a map of excluded words on a per-untypedUser basis using a hypothetical "User" object that contains this info
//        val dictionaryList = ConfigurationBuilder.getDefaultDictionaries()
//        dictionaryList.add(DictionaryBuilder()
//                .setDictionaryName("exclude")
//                .setExclusion(true)
//                .let {
//                    item?.let(getIdentifiers)?.fold(it) { current, id -> current.addWord(id, 0) } ?: it
//                }
//                .createDictionary())
//
//        // Create our configuration object and set our custom minimum
//        // entropy, and custom dictionary list
//        val configuration = ConfigurationBuilder()
//                .setMinimumEntropy(atLeastEntropy.invoke(item).toDouble())
//                .setDictionaries(dictionaryList)
//                .createConfiguration()
//
//        // Create our Nbvcxz object with the configuration we built
//        val nbvcxz = Nbvcxz(configuration)
//
//        if (!nbvcxz.estimate(password).isMinimumEntropyMet) {
//            throw IllegalArgumentException("This password is not secure enough.")
//        }
//    }
//
//    fun check(rawItem: T, plaintext: String): Boolean {
//        return hasher.verify(variable.get(rawItem).removePrefix(HASHED_PREFIX), plaintext)
//    }
//}