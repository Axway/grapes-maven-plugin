package org.axway.grapes.maven.promotion;

class PromotionReportItemComparator implements java.util.Comparator<PromotionReportItem>
{
    @Override
    public int compare(final PromotionReportItem item1, final PromotionReportItem item2)
    {
        if (item1.getPromotionStatus() == item2.getPromotionStatus())
        {
            return item1.getGavc().compareTo(item2.getGavc());
        }
        else
        {
            return item1.getPromotionStatus().compareTo(item2.getPromotionStatus());
        }
    }
}
