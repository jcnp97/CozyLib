package net.cozyvanilla.cozylib.integrations;

public interface Integration {
    String getName();
    void enable();
    void disable();
    void load();
}