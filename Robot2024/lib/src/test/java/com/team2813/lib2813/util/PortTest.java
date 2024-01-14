package com.team2813.lib2813.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.team2813.lib2813.theories.Between;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashSet;
import java.util.Set;

import com.team2813.lib2813.util.Port;

@RunWith(Theories.class)
public class PortTest {
    private final Set<Integer> canRegistry = new HashSet<>();

    @Test
    public void getCanIdReturnsCanId() {
        Port port = new Port(3, canRegistry);
        assertEquals(3, port.getCanId());
    }

    @Test
    public void multipleCanIds() {
        new Port(1, canRegistry);
        assertEquals(1, canRegistry.size());
        new Port(2, canRegistry);;
        assertEquals(2, canRegistry.size());
    }

    @Test
    public void duplicateCanIdRaises() {
        new Port(1, canRegistry);
        assertThrows(IllegalArgumentException.class, () -> new Port(1, canRegistry));
        assertEquals(1, canRegistry.size());
    }

    @Test
    public void invalidCanIdRaises() {
        assertThrows(InvalidCanIdException.class, () -> new Port(-1, canRegistry));
        assertThrows(InvalidCanIdException.class, () -> new Port(63, canRegistry));
        assertTrue(canRegistry.isEmpty());

        new Port(0, canRegistry);
        assertEquals(1, canRegistry.size());
        new Port(62, canRegistry);
        assertEquals(2, canRegistry.size());
    }

    @Theory
    public void validCanID(@Between(first = 0, last = 62) int canID) {
        new Port(canID, canRegistry);
    }
}
