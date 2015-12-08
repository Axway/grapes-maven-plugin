package org.axway.grapes.maven.promotion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.axway.grapes.maven.materials.stubs.Artifacts;
import org.axway.grapes.maven.materials.stubs.MultiModuleProjectStub;
import org.axway.grapes.utils.client.GrapesClient;
import org.axway.grapes.utils.client.GrapesCommunicationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PromotionParallelReporterTest
{
    private static final PromotionReportItem junit = new PromotionReportItem(Artifacts.junit);
    private static final PromotionReportItem fakeArtifact = new PromotionReportItem(Artifacts.fakeArtifact);
    private static final PromotionReportItem axwayFakeArtifact = new PromotionReportItem(Artifacts.axwayFakeArtifact);

    private PromotionParallelReporter reporter;

    @Mock
    private Log log;

    @Mock
    private GrapesClient grapesClient;

    @Before
    public void setUp() throws GrapesCommunicationException
    {
        reporter = new PromotionParallelReporter(log, grapesClient);
        when(grapesClient.getArtifact(junit.getGavc())).thenReturn(Artifacts.junit);
        when(grapesClient.getArtifact(fakeArtifact.getGavc())).thenReturn(Artifacts.fakeArtifact);
        when(grapesClient.getArtifact(axwayFakeArtifact.getGavc())).thenReturn(Artifacts.axwayFakeArtifact);
    }

    @Test
    public void testSimpleReport()
    {
        MavenProject mavenProject = new MultiModuleProjectStub();

        PromotionReport result = reporter.report(mavenProject, Collections.<String>emptyList());

        assertThat(result.getItems()).containsExactly(junit, axwayFakeArtifact, fakeArtifact);
    }

    @Test
    public void testReportWithFilter()
    {
        MavenProject mavenProject = new MultiModuleProjectStub();

        PromotionReport result = reporter.report(mavenProject, Collections.singletonList("org.axway"));

        assertThat(result.getItems()).containsExactly(axwayFakeArtifact);
    }
}