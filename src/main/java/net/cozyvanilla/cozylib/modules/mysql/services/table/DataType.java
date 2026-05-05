package net.cozyvanilla.cozylib.modules.mysql.services.table;

public final class DataType {
    private final String sql;

    private DataType(String sql) {
        this.sql = sql;
    }

    public String toSql() {
        return sql;
    }

    public static DataType intType() {
        return new DataType("INT");
    }

    public static DataType bigint() {
        return new DataType("BIGINT");
    }

    public static DataType varchar(int length) {
        return new DataType("VARCHAR(" + length + ")");
    }

    public static DataType charType(int length) {
        return new DataType("CHAR(" + length + ")");
    }

    public static DataType varbinary(int length) {
        return new DataType("VARBINARY(" + length + ")");
    }

    public static DataType binary(int length) {
        return new DataType("BINARY(" + length + ")");
    }

    public static DataType decimal(int precision, int scale) {
        return new DataType("DECIMAL(" + precision + "," + scale + ")");
    }

    public static DataType decimal() {
        return new DataType("DECIMAL(12,2)");
    }

    public static DataType text() {
        return new DataType("TEXT");
    }

    public static DataType timestamp() {
        return new DataType("TIMESTAMP");
    }

    public static DataType bool() {
        return new DataType("BOOLEAN");
    }

    public static DataType json() {
        return new DataType("JSON");
    }

    public static DataType uuid() {
        return binary(16);
    }

    public static DataType inet() {
        return varbinary(16);
    }
}