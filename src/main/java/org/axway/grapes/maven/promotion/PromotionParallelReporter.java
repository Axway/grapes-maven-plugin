package org.axway.grapes.maven.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.axway.grapes.maven.report.GrapesTranslator;
import org.axway.grapes.utils.client.GrapesClient;

class PromotionParallelReporter
{
    private final GrapesClient grapesClient;

    private final Log log;

    private final ExecutorService executor;

    private final long timeout;

    public PromotionParallelReporter(Log log, GrapesClient grapesClient)
    {
        this(log, grapesClient, 10, 1);
    }

    public PromotionParallelReporter(Log log, GrapesClient grapesClient, int maxThreads, long timeout)
    {
        this.log = log;
        this.grapesClient = grapesClient;
        this.executor = Executors.newFixedThreadPool(maxThreads, new PromotionParallelReporterThreadFactory());
        this.timeout = timeout;
    }

    public PromotionReport report(MavenProject project, List<String> groupIdFilters)
    {
        return getDependencies(project, groupIdFilters);
    }

    private PromotionReport getDependencies(MavenProject project, List<String> groupIdFilters)
    {
        PromotionReport report = new PromotionReport();
        try
        {
            // get dependencies in parallel
            Collection<PromotionReporterTask> tasks = new ArrayList<PromotionReporterTask>();
            for (MavenProject collectedProject : project.getCollectedProjects())
            {
                tasks.add(new PromotionReporterTask(log, grapesClient, collectedProject, groupIdFilters));
            }
            List<Future<Set<PromotionReportItem>>> results = executor.invokeAll(tasks, timeout, TimeUnit.MINUTES);

            // aggregate results
            for (Future<Set<PromotionReportItem>> result : results)
            {
                report.addAll(result.get());
            }

            // remove inner projects from dependencies
            removeProjectArtifacts(project, report);
        }
        catch (InterruptedException e)
        {
            fatalError(e, "Timeout reached while processing Grapes report.");
        }
        catch (ExecutionException e)
        {
            fatalError(e, "An error occured while processing Grapes report.");
        }
        return report;
    }

    private void fatalError(final Exception e, final String errorMessage)
    {
        log.error(errorMessage);
        log.debug(e);
        throw new RuntimeException(errorMessage, e);
    }

    private void removeProjectArtifacts(MavenProject project, PromotionReport report)
    {
        for (MavenProject collectedProject : project.getCollectedProjects())
        {
            report.remove(GrapesTranslator.getGrapesArtifact(collectedProject.getArtifact()));
        }
    }

    private static class PromotionParallelReporterThreadFactory implements ThreadFactory
    {
        private int count = 0;
        @Override
        public Thread newThread(final Runnable r)
        {
            return new Thread(r, String.format("grapes-promotion-report-%d", count++));
        }
    }
}
