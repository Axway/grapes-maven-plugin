package org.axway.grapes.maven.promotion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.axway.grapes.utils.client.GrapesClient;

public class PromotionReporterTask implements Callable<Set<PromotionReportItem>>
{
    private final GrapesClient grapesClient;

    private final Log log;

    private final MavenProject project;

    private final List<String> groupIdPrefixes;

    private final PromotionReportItemLoader loader;

    public PromotionReporterTask(Log log, GrapesClient grapesClient, MavenProject project, List<String> groupIdPrefixes)
    {
        this.log = log;
        this.grapesClient = grapesClient;
        this.project = project;
        this.groupIdPrefixes = groupIdPrefixes;
        this.loader = new PromotionReportItemLoader(log, grapesClient);
    }

    @Override
    public Set<PromotionReportItem> call() throws Exception
    {
        log.debug(String.format("reporting dependencies for project %s", project.getName()));

        Set<PromotionReportItem> report = new HashSet<PromotionReportItem>();
        if (project.getDependencies() != null)
        {
            for (Dependency dependency : project.getDependencies())
            {
                log.debug(String.format("checking dependency %s", dependency.getArtifactId()));
                if (match(dependency))
                {
                    log.debug(String.format("%s matches filter", dependency.getArtifactId()));
                    PromotionReportItem item = loader.load(dependency);
                    if (item == null)
                    {
                        log.debug(String.format("%s is not found in Grapes server", dependency.getArtifactId()));
                        continue;
                    }
                    log.debug(String.format("%s found in Grapes server", dependency.getArtifactId()));
                    report.add(item);
                }
                else
                {
                    log.debug(String.format("%s doesn't match filter", dependency.getArtifactId()));
                }
            }
        }
        return report;
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

}