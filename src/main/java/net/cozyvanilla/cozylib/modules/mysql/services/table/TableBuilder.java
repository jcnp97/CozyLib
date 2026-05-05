package net.cozyvanilla.cozylib.modules.mysql.services.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TableBuilder {
    private final String tableName;
    private final List<ColumnBuilder> columns = new ArrayList<>();
    private final List<String> primaryKeys = new ArrayList<>();
    private final List<String> indexes = new ArrayList<>();
    private final List<String> foreignKeys = new ArrayList<>();

    public TableBuilder(String tableName) {
        this.tableName = tableName;
    }

    public ColumnBuilder column(String name, DataType type) {
        ColumnBuilder column = new ColumnBuilder(this, name, type);
        columns.add(column);
        return column;
    }

    public TableBuilder primaryKey(String... columns) {
        primaryKeys.addAll(Arrays.asList(columns));
        return this;
    }

    public TableBuilder index(String indexName, String... columns) {
        indexes.add(
                "INDEX `" + indexName + "` (" +
                        String.join(", ", Arrays.stream(columns)
                                .map(c -> "`" + c + "`")
                                .toList()) +
                        ")"
        );
        return this;
    }

    public TableBuilder foreignKey(
            String column,
            String referencedTable,
            String referencedColumn
    ) {
        return foreignKey(column, referencedTable, referencedColumn, null, null);
    }

    public TableBuilder foreignKey(
            String column,
            String referencedTable,
            String referencedColumn,
            String onDelete,
            String onUpdate
    ) {
        StringBuilder fk = new StringBuilder(
                "FOREIGN KEY (`" + column + "`) REFERENCES `" +
                        referencedTable + "`(`" + referencedColumn + "`)"
        );

        if (onDelete != null) {
            fk.append(" ON DELETE ").append(onDelete);
        }

        if (onUpdate != null) {
            fk.append(" ON UPDATE ").append(onUpdate);
        }

        foreignKeys.add(fk.toString());
        return this;
    }

    public String build() {
        return toSql();
    }

    private String toSql() {
        List<String> definitions = new ArrayList<>();

        for (ColumnBuilder column : columns) {
            definitions.add(column.toSql());
        }

        if (!primaryKeys.isEmpty()) {
            definitions.add(
                    "PRIMARY KEY (" +
                            String.join(", ", primaryKeys.stream()
                                    .map(c -> "`" + c + "`")
                                    .toList()) +
                            ")"
            );
        }

        definitions.addAll(indexes);
        definitions.addAll(foreignKeys);

        return "CREATE TABLE IF NOT EXISTS `" + tableName + "` (\n  " +
                String.join(",\n  ", definitions) +
                "\n);";
    }
}