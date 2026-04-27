package net.cozyvanilla.cozylib.modules;

import javax.annotation.Nullable;

public interface Module<T> {
    String getName();
    String getPrefix();
    @Nullable
    T getCommands();
    void getConfig();
    void enable();
    void disable();
}