package ru.ifmo.boger.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface representing Cache data structure, which stores pairs of keys and values.
 *
 * @param <K> key type
 * @param <V> value type
 * @author Daniil Boger
 */
public interface Cache<K, V> {
    /**
     * Receive value which is assigned to given key. Returns null if cache does not contain key
     *
     * @param key key
     * @return value associated with key if it exists, null otherwise
     */
    @Nullable
    V get(@NotNull K key);

    /**
     * Put a new value into cache. If key already exists, overwrites the value.
     *
     * @param key   key
     * @param value value
     */
    void put(@NotNull K key, @NotNull V value);
}
