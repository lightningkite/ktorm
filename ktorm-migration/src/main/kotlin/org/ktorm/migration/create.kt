package org.ktorm.migration

import org.ktorm.dsl.QueryRowSet
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.ReferenceBinding

public fun BaseTable<*>.createTable(): CreateTableExpression {
    val tableConstraints = HashMap<String, TableConstraintExpression>()
    tableConstraints["pk"] = PrimaryKeyTableConstraintExpression(
        across = this.primaryKeys.map { it.asExpression() }
    )
    return CreateTableExpression(
        name = this.asReferenceExpression(),
        columns = this.columns.map {
            (it.binding as? ReferenceBinding)?.let { binding ->
                handleReferenceBinding(tableConstraints, it, binding)
            }
            for (extra in it.extraBindings) {
                if (extra is ReferenceBinding) {
                    handleReferenceBinding(tableConstraints, it, extra)
                }
            }
            for (constraint in it.constraints) {
                when (constraint) {
                    UniqueConstraint -> tableConstraints["unique_${it.name}"] = UniqueTableConstraintExpression(
                        across = listOf(it.asExpression())
                    )
                    is ForeignKeyConstraint -> {
                        tableConstraints["FK_" + it.name] = (ForeignKeyTableConstraintExpression(
                            otherTable = constraint.to.asReferenceExpression(),
                            correspondence = mapOf(
                                it.asExpression() to constraint.on.asExpression()
                            )
                        ))
                    }
                }
            }
            ColumnDeclarationExpression(
                name = it.name,
                sqlType = it.sqlType,
                notNull = it.notNull,
                default = it.default as? ScalarExpression<*>,
                autoIncrement = it.autoIncrement
            )
        },
        constraints = tableConstraints
    )
}

private fun handleReferenceBinding(
    tableConstraints: HashMap<String, TableConstraintExpression>,
    it: Column<*>,
    binding: ReferenceBinding
) {
    tableConstraints["FK_" + it.name] = (ForeignKeyTableConstraintExpression(
        otherTable = binding.referenceTable.asReferenceExpression(),
        correspondence = mapOf(
            it.asExpression() to (binding.referenceTable.primaryKeys.singleOrNull()
                ?: throw IllegalArgumentException("Foreign key cannot be defined this way if there are multiple primary keys on the other")).asExpression()
        )
    ))
}

public fun CreateTableExpression.reverse(): BaseTable<MidMigrationModel> {
    return object : BaseTable<MidMigrationModel>(
        tableName = this.name.name,
        alias = null,
        catalog = this.name.catalog,
        schema = this.name.schema
    ) {
        override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): MidMigrationModel {
            return MidMigrationModel(columns.associateWith { row[it] })
        }
    }
}

public data class MidMigrationModel(val map: Map<Column<*>, Any?>) {
    @Suppress("UNCHECKED_CAST")
    public operator fun <T: Any> get(key: Column<T>): T? = map[key] as? T
}

public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableAddExpression) {
    registerColumn(alter.column.name, alter.column.sqlType)
    if(alter.)
}
public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableDropColumnExpression) {}
public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableModifyColumnExpression) {}
public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableSetDefaultExpression<*>) {}
public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableDropDefaultExpression) {}
public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableAddConstraintExpression) {}
public fun BaseTable<MidMigrationModel>.apply(alter: AlterTableDropConstraintExpression) {}