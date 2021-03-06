package ru.otus.gromov;

public interface MyCash<K, V> {

    void put(K key, V value);

    V get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();
}
