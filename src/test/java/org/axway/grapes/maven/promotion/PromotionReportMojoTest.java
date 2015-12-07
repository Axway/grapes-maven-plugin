package org.axway.grapes.maven.promotion;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.axway.grapes.maven.TestableLog;
import org.axway.grapes.maven.materials.stubs.Artifacts;
import org.axway.grapes.maven.server.GrapesServerMock;
import org.junit.Test;


public class PromotionReportMojoTest extends AbstractMojoTestCase
{
    private GrapesServerMock grapesServer;

    protected void setUp() throws Exception
    {
        // required for mojo lookups to work
        super.setUp();

        grapesServer = new GrapesServerMock();
        mockGrapesServer();
        grapesServer.start();
    }

    private void mockGrapesServer()
    {
        grapesServer.addArtifact(Artifacts.junit);
        grapesServer.addArtifact(Artifacts.axwayFakeArtifact);
        grapesServer.addArtifact(Artifacts.fakeArtifact);
    }

    protected void tearDown()
    {
        grapesServer.stop();
    }

    @Test
    public void testMultiModuleProject() throws Exception
    {
        File pom = getTestFile("src/test/resources/materials/multi-module-project/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        PromotionReportMojo promotionReportMojo = (PromotionReportMojo) lookupMojo("promotion-report", pom);
        setVariableValueToObject(promotionReportMojo, "host", "localhost");
        setVariableValueToObject(promotionReportMojo, "port", String.valueOf(grapesServer.getPort()));

        TestableLog log = new TestableLog();
        setVariableValueToObject(promotionReportMojo, "log", log);

        // Runs Mojo on the project
        promotionReportMojo.execute();

        // Verifying logs
        assertTrue(log.getWarns().contains("junit:junit:4.11::jar NOT PROMOTED"));
        assertTrue(log.getWarns().contains("org.axway:fake:1.0.0::jar NOT PROMOTED"));
        assertTrue(log.getWarns().contains("org.fake:fake:2.0.0::jar NOT PROMOTED"));
    }
}
