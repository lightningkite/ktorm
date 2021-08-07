package org.ktorm.migration

import org.ktorm.schema.Column

public val <C : Any> Column<C>.autoIncrement: Boolean
    get() {
        @Suppress("UNCHECKED_CAST")
        return this.extraProperties.getOrDefault("autoIncrement", false) as Boolean
    }

public fun <C : Any> Column<C>.autoIncrement(autoIncrement: Boolean = true): Column<C> {
    return this.copy(extraProperties = extraProperties + ("autoIncrement" to autoIncrement))
}