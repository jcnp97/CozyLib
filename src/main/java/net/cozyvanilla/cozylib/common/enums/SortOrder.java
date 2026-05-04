package net.cozyvanilla.cozylib.common.enums;

import github.scarsz.discordsrv.dependencies.jackson.annotation.JsonTypeInfo;

public enum SortOrder {
    ASCENDING,
    DESCENDING;

    public boolean isAscending() {
        return this == ASCENDING;
    }

    public boolean isDescending() {
        return this == DESCENDING;
    }
}