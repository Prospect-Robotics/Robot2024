package com.team2813;

import com.team2813.lib2813.feature.FeatureIdentifier;

/** Enumeration of all features for this robot. */
public enum Feature implements FeatureIdentifier {
    EXAMPLE(FeatureBehavior.INITIALLY_DISABLED);

    private final FeatureBehavior behavior;

    Feature(FeatureBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public FeatureBehavior behavior() {
        return behavior;
    }
}
