package ru.ifmo.boger.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * "Least recently used" cache, which holds exactly up to N values.
 * If the number of keys exceeds the capacity, evicts least recently used value.
 *
 * @author Daniil Boger
 */
public class LRUCache<K, V> implements Cache<K, V> {
    @NotNull
    private final Map<K, Node> data;
    @Nullable
    private Node head;
    @Nullable
    private Node tail;
    private int freeSpace;

    /**
     * Initialize LRU cache with given capacity
     *
     * @param capacity capacity
     * @throws IllegalArgumentException if given capacity is lesser than 1
     */
    public LRUCache(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("LRU Cache capacity must be greater than zero");
        }
        this.freeSpace = capacity;
        this.data = new HashMap<>(capacity);
    }

    @Override
    @Nullable
    public V get(@NotNull K key) {
        final Node tmp = data.get(key);
        if (tmp == null) {
            return null;
        }
        moveToEnd(tmp);
        return tmp.value;
    }

    @Override
    public void put(@NotNull K key, @NotNull V value) {
        if (data.containsKey(key)) {
            Node tmp = data.get(key);
            tmp.value = value;  // overwrite
            moveToEnd(tmp);
        } else {
            if (freeSpace > 0) {
                freeSpace--;
                Node node = new Node(key, value);
                pushBack(node);
                data.put(key, node);
            } else {
                // no free space - evict least recently used
                final Node toDelete = head;
                assert toDelete != null;
                data.remove(toDelete.key);
                deleteNode(toDelete);
                Node toInsert = new Node(key, value);
                pushBack(toInsert);
                data.put(key, toInsert);
                assert get(key) == value;
            }
        }
    }

    private void deleteNode(@NotNull final Node node) {
        if (node.left != null) {
            node.left.right = node.right;
        } else {
            head = node.right;
        }
        if (node.right != null) {
            node.right.left = node.left;
        } else {
            tail = node.left;
        }
        checkForLoops(node);
    }

    private void pushBack(@NotNull final Node node) {
        if (head == null) {
            head = node;
        }
        if (tail != null) {
            tail.right = node;
        }
        node.left = tail;
        node.right = null;
        tail = node;
        checkForLoops(node);
    }

    private void checkForLoops(@NotNull final Node node) {
        assert node.left != node && node.right != node;
    }

    private void moveToEnd(@NotNull final Node node) {
        deleteNode(node);
        pushBack(node);
        assert tail == node;
    }

    private class Node {
        @NotNull K key;
        @NotNull V value;
        @Nullable Node left;
        @Nullable Node right;

        Node(@NotNull K key, @NotNull V value) {
            this.key = key;
            this.value = value;
        }

    }
}
