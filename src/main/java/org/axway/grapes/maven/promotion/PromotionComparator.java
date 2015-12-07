package org.axway.grapes.maven.promotion;

import org.axway.grapes.commons.datamodel.Artifact;

class PromotionComparator implements java.util.Comparator<Artifact>
{
    @Override
    public int compare(final Artifact artifact1, final Artifact artifact2)
    {
        if (artifact1.isPromoted() == artifact2.isPromoted())
            return artifact1.getGavc().compareTo(artifact2.getGavc());
        else
            return artifact1.isPromoted()?1:-1;
    }
}
