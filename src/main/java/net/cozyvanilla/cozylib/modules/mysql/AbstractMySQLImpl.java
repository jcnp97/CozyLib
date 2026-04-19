package net.cozyvanilla.cozylib.modules.mysql;

public abstract class AbstractMySQLImpl {

    protected AbstractMySQLImpl() {
        createTable();
    }

    protected abstract void createTable();
    protected abstract String getTableStmt();
}