package ru.ifmo.boger.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LRUCacheTest {
    private Cache<Integer, Integer> cache;

    @Before
    public void init() {
        cache = new LRUCache<>(3);
    }

    @Test
    public void test01_empty() {
        assertNull(cache.get(5));
    }

    @Test
    public void test02_incorrectCapacity() {
        Assert.assertThrows(
                "throw IllegalArgumentException on incorrect capacity",
                IllegalArgumentException.class,
                () -> new LRUCache<Integer, Integer>(-1)
        );
    }

    @Test
    public void test03_insertions() {
        cache.put(1, 1);
        cache.put(2, 4);
        cache.put(3, 9);
        assertEquals(1, getNotNullValue(1));
        assertEquals(4, getNotNullValue(2));
        assertEquals(9, getNotNullValue(3));
        assertNull(cache.get(5));
    }

    @Test
    public void test04_oneElement() {
        cache = new LRUCache<>(1);
        cache.put(0, 0);
        for (int i = 1; i < 10; i++) {
            cache.put(i, i);
            assertNotNull(cache.get(i));
            assertNull(cache.get(i - 1));
        }
    }

    @Test
    public void test05_singleOverflow() {
        cache.put(1, 1);
        cache.put(2, 4);
        cache.put(3, 9);
        cache.get(1);
        cache.put(4, 16);
        assertEquals(1, getNotNullValue(1));
        assertEquals(9, getNotNullValue(3));
        assertEquals(16, getNotNullValue(4));
        assertNull(cache.get(2));
    }

    @Test
    public void test06_multipleReplacements() {
        cache.put(1, 1);
        cache.put(2, 4);
        cache.put(3, 9);
        cache.put(1, 0);
        cache.get(2);
        cache.get(2);
        cache.put(4, 16);
        assertEquals(0, getNotNullValue(1));
        assertEquals(4, getNotNullValue(2));
        assertEquals(16, getNotNullValue(4));
        assertNull(cache.get(3));
        cache.put(5, 25);
        assertNull(cache.get(1));
        cache.put(6, 0);
        cache.put(7, 0);
        cache.put(8, 0);
        for (int i = 0; i <= 5; i++) {
            assertNull(cache.get(i));
        }
    }

    private int getNotNullValue(int key) {
        Integer value = cache.get(key);
        assertNotNull(value);
        return value;
    }

}
