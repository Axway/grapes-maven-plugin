package org.axway.grapes.maven.promotion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.utils.client.GrapesClient;
import org.axway.grapes.utils.client.GrapesCommunicationException;

public class PromotionReporterTask implements Callable<Set<Artifact>>
{
    private final GrapesClient grapesClient;

    private final Log log;

    private final MavenProject project;

    private final List<String> groupIdPrefixes;

    public PromotionReporterTask(Log log, GrapesClient grapesClient, MavenProject project, List<String> groupIdPrefixes)
    {
        this.log = log;
        this.grapesClient = grapesClient;
        this.project = project;
        this.groupIdPrefixes = groupIdPrefixes;
    }

    @Override
    public Set<Artifact> call() throws Exception
    {
        log.debug(String.format("reporting dependencies for project %s", project.getName()));

        Set<Artifact> dependencies = new HashSet<Artifact>();
        if (project.getDependencies() != null)
        {
            for (Dependency dependency : project.getDependencies())
            {
                log.debug(String.format("checking dependency %s", dependency.getArtifactId()));
                if (match(dependency))
                {
                    log.debug(String.format("%s matches filter", dependency.getArtifactId()));
                    Artifact remoteArtifact = getGrapesArtifactQuietly(dependency);
                    if (remoteArtifact == null)
                    {
                        log.debug(String.format("%s is not found in Grapes server", dependency.getArtifactId()));
                        continue;
                    }
                    log.debug(String.format("%s found in Grapes server", dependency.getArtifactId()));
                    dependencies.add(remoteArtifact);
                }
                else
                {
                    log.debug(String.format("%s doesn't match filter", dependency.getArtifactId()));
                }
            }
        }
        return dependencies;
    }

    private boolean match(Dependency dependency)
    {
        if (groupIdPrefixes.isEmpty())
        {
            return true;
        }

        for (String groupIdPrefixe : groupIdPrefixes)
        {
            if (dependency.getGroupId().startsWith(groupIdPrefixe))
            {
                return true;
            }
        }
        return false;
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
        return s == null? "":s;
    }
}