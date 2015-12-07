package org.axway.grapes.maven.promotion;

import static org.junit.Assert.*;

import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.commons.datamodel.DataModelFactory;
import org.junit.Test;

public class PromotionComparatorTest
{
    private PromotionComparator comparator = new PromotionComparator();

    @Test
    public void testCompareBasedOnArtifactId() throws Exception
    {
        Artifact a1 = DataModelFactory.createArtifact("com.axway", "a1", "1.0.0-1", null, null, null);
        Artifact a2 = DataModelFactory.createArtifact("com.axway", "a2", "1.0.0-1", null, null, null);

        int result = comparator.compare(a1, a2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareBasedOnGroupId() throws Exception
    {
        Artifact a1 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null);
        Artifact a2 = DataModelFactory.createArtifact("com.axway.a2", "a", "1.0.0-1", null, null, null);

        int result = comparator.compare(a1, a2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareBasedOnVersion() throws Exception
    {
        Artifact a1 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null);
        Artifact a2 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-2", null, null, null);

        int result = comparator.compare(a1, a2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareBasedOnPromotion() throws Exception
    {
        Artifact a1 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null);
        Artifact a2 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-2", null, null, null);
        a1.setPromoted(false);
        a2.setPromoted(true);

        int result = comparator.compare(a1, a2);

        assertTrue(result < 0);
    }

    @Test
    public void testEquality() throws Exception
    {
        Artifact a1 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null);
        Artifact a2 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null);

        int result = comparator.compare(a1, a2);

        assertTrue(result == 0);
    }
}