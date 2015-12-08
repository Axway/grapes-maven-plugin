package org.axway.grapes.maven.promotion;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.maven.plugin.logging.Log;
import org.axway.grapes.commons.datamodel.Artifact;

public class PromotionReport
{
    private SortedSet<PromotionReportItem> items = new TreeSet<PromotionReportItem>(new PromotionReportItemComparator());

    public void addAll(final Set<PromotionReportItem> items)
    {
        this.items.addAll(items);
    }

    public Collection<PromotionReportItem> getItems()
    {
        return items;
    }

    public void display(Log log)
    {
        for (PromotionReportItem item : items)
        {
            display(log, item);
        }
    }

    private void display(Log log, PromotionReportItem item)
    {
        switch (item.getPromotionStatus())
        {
            case NOT_PROMOTABLE:
                log.error(item.toString());
                break;
            case PROMOTABLE:
                log.warn(item.toString());
                break;
            case PROMOTED:
            default:
                log.info(item.toString());
                break;
        }
    }

    public void remove(final Artifact artifact)
    {
        items.remove(new PromotionReportItem(artifact));
    }
}
