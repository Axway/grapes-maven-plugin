package org.axway.grapes.maven.promotion;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.utils.client.GrapesClient;
import org.axway.grapes.utils.client.GrapesCommunicationException;

public class PromotionReportItemLoader
{
    private final Log log;

    private final GrapesClient grapesClient;

    public PromotionReportItemLoader(Log log, GrapesClient grapesClient)
    {
        this.log = log;
        this.grapesClient = grapesClient;
    }

    public PromotionReportItem load(Dependency dependency)
    {
        Artifact artifact = getGrapesArtifactQuietly(dependency);
        if (artifact == null)
        {
            return null;
        }
        else
        {
            return new PromotionReportItem(artifact);
        }
    }

    private Artifact getGrapesArtifactQuietly(Dependency dependency)
    {
        String gavc = dependencyGavc(dependency);
        try
        {
            return grapesClient.getArtifact(gavc);
        }
        catch (GrapesCommunicationException e)
        {
            log.error(String.format("Error while getting artifact %s from Grapes server.", gavc));
            log.debug(e);
            return null;
        }
    }

    private static String dependencyGavc(Dependency dependency)
    {
        final StringBuilder sb = new StringBuilder();

        sb.append(nullToEmpty(dependency.getGroupId()));
        sb.append(":");
        sb.append(nullToEmpty(dependency.getArtifactId()));
        sb.append(":");
        sb.append(nullToEmpty(dependency.getVersion()));
        sb.append(":");
        sb.append(nullToEmpty(dependency.getClassifier()));
        sb.append(":");
        sb.append(nullToEmpty(dependency.getType()));

        return sb.toString();
    }

    private static String nullToEmpty(String s)
    {
        return s == null ? "" : s;
    }
}
