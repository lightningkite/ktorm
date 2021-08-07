package org.ktorm.migration

import org.ktorm.schema.Column

public val <C : Any> Column<C>.notNull: Boolean
    get() {
        @Suppress("UNCHECKED_CAST")
        return this.extraProperties.getOrDefault("notNull", false) as Boolean
    }

public fun <C : Any> Column<C>.notNull(notNull: Boolean = true): Column<C> {
    return this.copy(extraProperties = extraProperties + ("notNull" to notNull))
}