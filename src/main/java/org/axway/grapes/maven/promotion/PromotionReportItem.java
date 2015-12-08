package org.axway.grapes.maven.promotion;

import org.axway.grapes.commons.datamodel.Artifact;

public class PromotionReportItem
{
    private Artifact artifact;

    private PromotionStatus promotionStatus;

    private String lastRelease;

    private String lastPromotion;

    public PromotionReportItem(Artifact artifact)
    {
        this.artifact = artifact;
        this.promotionStatus = artifact.isPromoted() ? PromotionStatus.PROMOTED : PromotionStatus.NOT_PROMOTABLE;
    }

    public PromotionStatus getPromotionStatus()
    {
        return promotionStatus;
    }

    public String getGavc()
    {
        return artifact.getGavc();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof PromotionReportItem)
        {
            return this.artifact.equals(((PromotionReportItem)obj).artifact);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return artifact.hashCode();
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", artifact.getGavc(), artifact.isPromoted() ? "PROMOTED" : "NOT PROMOTED");
    }
}
