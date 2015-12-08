package org.axway.grapes.maven.promotion;

import static org.junit.Assert.*;

import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.commons.datamodel.DataModelFactory;
import org.junit.Test;

public class PromotionComparatorTest
{
    private PromotionReportItemComparator comparator = new PromotionReportItemComparator();

    @Test
    public void testCompareBasedOnArtifactId() throws Exception
    {
        PromotionReportItem item1 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway", "a1", "1.0.0-1", null, null, null));
        PromotionReportItem item2 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway", "a2", "1.0.0-1", null, null, null));

        int result = comparator.compare(item1, item2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareBasedOnGroupId() throws Exception
    {
        PromotionReportItem item1 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null));
        PromotionReportItem item2 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway.a2", "a", "1.0.0-1", null, null, null));

        int result = comparator.compare(item1, item2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareBasedOnVersion() throws Exception
    {
        PromotionReportItem item1 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null));
        PromotionReportItem item2 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-2", null, null, null));

        int result = comparator.compare(item1, item2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareBasedOnPromotion() throws Exception
    {
        final Artifact a1 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null);
        a1.setPromoted(false);
        final Artifact a2 = DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-2", null, null, null);
        a2.setPromoted(true);

        PromotionReportItem item1 = new PromotionReportItem(a1);
        PromotionReportItem item2 = new PromotionReportItem(a2);

        int result = comparator.compare(item1, item2);

        assertTrue(result < 0);
    }

    @Test
    public void testEquality() throws Exception
    {
        PromotionReportItem item1 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null));
        PromotionReportItem item2 = new PromotionReportItem(DataModelFactory.createArtifact("com.axway.a1", "a", "1.0.0-1", null, null, null));

        int result = comparator.compare(item1, item2);

        assertTrue(result == 0);
    }
}