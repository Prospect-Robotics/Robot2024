package com.team2813.lib2813.util;

import java.util.HashSet;
import java.util.Set;

import static com.team2813.lib2813.util.InputValidation.checkCanId;

/**
 * Type-safe wrapper for a can port ID.
 */
public final class Port {
    private static final Set<Integer> _GLOBAL_REGISTRY = new HashSet<>();
    private final int id;

    public Port(int id) {
        this(id, _GLOBAL_REGISTRY);
    }

    /** Visible for testing. */
    Port(int id, Set<Integer> canIdRegistry) {
        this.id = checkCanId(id);
        if (!canIdRegistry.add(id)) {
            throw new InvalidCanIdException(
                    id,  String.format("Can ID %d already registered", id));
        }
    }

    /** The ID (device number) for this port. */
    public int getCanId() {
        return id;
    }
}
