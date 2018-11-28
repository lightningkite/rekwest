package com.lightningkite.rekwest.server.security

import com.lightningkite.mirror.archive.HasId
import com.lightningkite.mirror.archive.secure.PropertySecureTable
import com.lightningkite.mirror.info.FieldInfo
import de.mkammerer.argon2.Argon2Factory
import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.resources.ConfigurationBuilder
import me.gosimple.nbvcxz.resources.DictionaryBuilder
import java.lang.IllegalArgumentException

class HashedFieldRules<T : HasId>(
        override val variable: FieldInfo<T, String>,
        val getIdentifiers: (T)->List<String>,
        val atLeastEntropy: (T?)->Int = { 30 /*Takes roughly a billion guesses*/ }
) : PropertySecureTable.PropertyRules<T, String> {

    val hasher = Argon2Factory.create()

    companion object {
        const val HASHED_PREFIX = "hashed: "
        const val HASHED_PLACEHOLDER = "<password>"
    }

    override suspend fun query(untypedUser: Any?) { throw IllegalAccessException() }

    override suspend fun read(untypedUser: Any?, justInserted: Boolean, currentState: T): String {
        return HASHED_PLACEHOLDER
    }

    override suspend fun write(untypedUser: Any?, currentState: T?, newState: String): String {
        if(newState.startsWith(HASHED_PREFIX)) return newState
        if(newState.equals(HASHED_PLACEHOLDER)) return currentState?.let { variable.get(it) } ?: throw IllegalArgumentException()
        judgePassword(currentState, newState)
        return HASHED_PREFIX + hasher.hash(10, 65536, 1, newState)
    }

    private fun judgePassword(item: T?, password: String) {
        //Check security of password.
        // Create a map of excluded words on a per-untypedUser basis using a hypothetical "User" object that contains this info
        val dictionaryList = ConfigurationBuilder.getDefaultDictionaries()
        dictionaryList.add(DictionaryBuilder()
                .setDictionaryName("exclude")
                .setExclusion(true)
                .let {
                    item?.let(getIdentifiers)?.fold(it) { current, id -> current.addWord(id, 0) } ?: it
                }
                .createDictionary())

        // Create our configuration object and set our custom minimum
        // entropy, and custom dictionary list
        val configuration = ConfigurationBuilder()
                .setMinimumEntropy(atLeastEntropy.invoke(item).toDouble())
                .setDictionaries(dictionaryList)
                .createConfiguration()

        // Create our Nbvcxz object with the configuration we built
        val nbvcxz = Nbvcxz(configuration)

        if (!nbvcxz.estimate(password).isMinimumEntropyMet) {
            throw IllegalArgumentException("This password is not secure enough.")
        }
    }

    fun check(rawItem: T, plaintext: String): Boolean {
        return hasher.verify(variable.get(rawItem).removePrefix(HASHED_PREFIX), plaintext)
    }
}