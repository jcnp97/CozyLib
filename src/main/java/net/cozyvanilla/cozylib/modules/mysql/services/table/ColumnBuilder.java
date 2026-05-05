package net.cozyvanilla.cozylib.modules.mysql.services.table;

public final class ColumnBuilder {
    private final TableBuilder table;
    private final String name;
    private final DataType type;

    private boolean nullable = false;
    private Object defaultValue;
    private boolean hasDefault = false;
    private boolean unique = false;
    private boolean autoIncrement = false;

    public ColumnBuilder(TableBuilder table, String name, DataType type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    public ColumnBuilder column(String name, DataType type) {
        return table.column(name, type);
    }

    public TableBuilder primaryKey(String... columns) {
        return table.primaryKey(columns);
    }

    public TableBuilder index(String name, String... columns) {
        return table.index(name, columns);
    }

    public TableBuilder foreignKey(String column, String refTable, String refColumn) {
        return table.foreignKey(column, refTable, refColumn);
    }

    public ColumnBuilder nullable() {
        this.nullable = true;
        return this;
    }

    public ColumnBuilder defaultValue(Object value) {
        this.defaultValue = value;
        this.hasDefault = true;

        if (value == null) {
            this.nullable = true;
        }

        return this;
    }

    public ColumnBuilder unique() {
        this.unique = true;
        return this;
    }

    public ColumnBuilder autoIncrement() {
        this.autoIncrement = true;
        return this;
    }

    String toSql() {
        StringBuilder sql = new StringBuilder();

        sql.append("`").append(name).append("` ");
        sql.append(type.toSql());

        if (autoIncrement && !isNumericType()) {
            throw new IllegalStateException("AUTO_INCREMENT is only allowed on numeric types");
        }

        sql.append(nullable ? " NULL" : " NOT NULL");

        if (autoIncrement) {
            sql.append(" AUTO_INCREMENT");
        }

        if (hasDefault) {
            sql.append(" DEFAULT ").append(formatDefault(defaultValue));
        }

        if (unique) {
            sql.append(" UNIQUE");
        }

        return sql.toString();
    }

    private boolean isNumericType() {
        String sqlType = type.toSql();
        return sqlType.contains("INT");
    }

    private String formatDefault(Object value) {
        return switch (value) {
            case null -> "NULL";
            case Number n -> n.toString();
            case Boolean b -> b ? "TRUE" : "FALSE";
            default -> "'" + value.toString().replace("'", "''") + "'";
        };
    }
}