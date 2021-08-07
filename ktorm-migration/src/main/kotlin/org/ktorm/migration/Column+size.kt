package org.ktorm.migration

import org.ktorm.schema.Column

public val <C : Any> Column<C>.size: Int?
    get() {
        @Suppress("UNCHECKED_CAST")
        return this.extraProperties["size"] as Int?
    }

public fun <C : Any> Column<C>.size(size: Int): Column<C> {
    return this.copy(extraProperties = extraProperties + ("size" to size))
}