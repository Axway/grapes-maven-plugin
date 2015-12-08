package org.axway.grapes.maven.promotion;

public enum PromotionStatus
{
    NOT_PROMOTABLE("NOT PROMOTABLE"),
    PROMOTABLE("PROMOTABLE"),
    PROMOTED("PROMOTED");

    private final String label;

    PromotionStatus(final String label)
    {
        this.label = label;
    }
}
