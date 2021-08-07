package org.ktorm.migration

import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.Column

public val <C : Any> Column<C>.default: ScalarExpression<C>?
    get() {
        @Suppress("UNCHECKED_CAST")
        return this.extraProperties["default"] as ScalarExpression<C>?
    }

public fun <C : Any> Column<C>.default(defaultExpression: ScalarExpression<C>?): Column<C> {
    if (defaultExpression != null)
        return this.copy(extraProperties = extraProperties + ("default" to defaultExpression))
    else
        return this.copy(extraProperties = extraProperties - "default")
}

public fun <C : Any> Column<C>.default(value: C): Column<C> {
    return this.default(ArgumentExpression(value, this.sqlType))
}