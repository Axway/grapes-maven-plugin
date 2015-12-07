package org.axway.grapes.maven.promotion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.axway.grapes.maven.materials.stubs.Artifacts.axwayFakeArtifact;
import static org.axway.grapes.maven.materials.stubs.Artifacts.fakeArtifact;
import static org.axway.grapes.maven.materials.stubs.Artifacts.junit;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.axway.grapes.commons.datamodel.Artifact;
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
    private PromotionParallelReporter reporter;

    @Mock
    private Log log;

    @Mock
    private GrapesClient grapesClient;

    @Before
    public void setUp() throws GrapesCommunicationException
    {
        reporter = new PromotionParallelReporter(log, grapesClient);
        when(grapesClient.getArtifact(junit.getGavc())).thenReturn(junit);
        when(grapesClient.getArtifact(fakeArtifact.getGavc())).thenReturn(fakeArtifact);
        when(grapesClient.getArtifact(axwayFakeArtifact.getGavc())).thenReturn(axwayFakeArtifact);
    }

    @Test
    public void testSimpleReport()
    {
        MavenProject mavenProject = new MultiModuleProjectStub();

        SortedSet<Artifact> result = reporter.report(mavenProject, Collections.<String>emptyList());

        assertThat(result).containsExactly(junit, axwayFakeArtifact, fakeArtifact);
    }

    @Test
    public void testReportWithFilter()
    {
        MavenProject mavenProject = new MultiModuleProjectStub();

        Set<Artifact> result = reporter.report(mavenProject, Collections.singletonList("org.axway"));

        assertThat(result).containsExactly(axwayFakeArtifact);
    }
}