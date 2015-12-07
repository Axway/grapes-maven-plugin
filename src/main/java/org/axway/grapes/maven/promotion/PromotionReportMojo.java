package org.axway.grapes.maven.promotion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.maven.AbstractGrapesMojo;
import org.axway.grapes.utils.client.GrapesClient;

/**
 * Goal which gathers and send dependencies information to Grapes.
 *
 * @goal promotion-report
 * @aggregator
 */
public class PromotionReportMojo extends AbstractGrapesMojo
{

    /**
     * Grapes server host
     *
     * @parameter property=grapes.host
     * @required
     */
    private String host = "";

    /**
     * Grapes server port
     *
     * @parameter property=grapes.port
     * @required
     */
    private String port = "";

    /**
     * Comma separated list of groupId prefixes.
     *
     * @parameter property=grapes.groupIds
     */
    private String groupIdPrefixes = "";

    /**
     * Timeout for the report processing in minutes.
     * Default is 1 minute.
     * @parameter property=grapes.timeout default-value=1
     */
    private long timeout = 1;

    /**
     * Maximum number of threads to involve during the report processing.
     * Default is 10.
     * @parameter property=grapes.threads default-value=10
     */
    private int threads = 10;

    public void execute() throws MojoExecutionException
    {
        if (isExecuteSkipped())
        {
            return;
        }

        PromotionParallelReporter reporter = new PromotionParallelReporter(getLog(), createGrapesClient(), threads, timeout);
        Set<Artifact> report = reporter.report(project, splitGroupIdPrefixes());
        display(report);
    }

    private GrapesClient createGrapesClient()
    {
        // check if Grapes server is available
        GrapesClient grapesClient = new GrapesClient(host, port);
        if (!grapesClient.isServerAvailable())
        {
            throw new RuntimeException(String.format("Grapes server (%s:%s) is not available.", host, port));
        }
        return grapesClient;
    }

    private List<String> splitGroupIdPrefixes()
    {
        if (!groupIdPrefixes.isEmpty())
        {
            String[] prefixes = groupIdPrefixes.split(",");
            return Arrays.asList(prefixes);
        }
        return Collections.emptyList();
    }

    private void display(Set<Artifact> report)
    {
        for (Artifact artifact : report)
        {
            display(artifact);
        }
    }

    private void display(Artifact artifact)
    {
        if (artifact.isPromoted())
        {
            getLog().info(artifactToString(artifact));
        }
        else
        {
            getLog().warn(artifactToString(artifact));
        }
    }

    private String artifactToString(final Artifact artifact)
    {
        return String.format("%s %s", artifact.getGavc(), artifact.isPromoted()?"PROMOTED":"NOT PROMOTED");
    }
}