package org.axway.grapes.maven.server;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.commons.utils.JsonUtils;
import org.axway.grapes.utils.client.RequestUtils;

import com.github.tomakehurst.wiremock.WireMockServer;

public class GrapesServerMock
{
    private WireMockServer wireMockServer;

    public GrapesServerMock()
    {
        wireMockServer = new WireMockServer(); //No-args constructor will start on port 8080, no HTTPS
        wireMockServer.stubFor(
                get(urlEqualTo("/"))
                .willReturn(
                        aResponse().withStatus(200)));
    }

    public void start()
    {
        wireMockServer.start();

    }

    public void stop()
    {
        wireMockServer.stop();
    }

    public int getPort()
    {
        return wireMockServer.port();
    }

    public void addArtifact(Artifact artifact)
    {
        try
        {
            wireMockServer.stubFor(
                    get(urlEqualTo("/"+RequestUtils.getArtifactPath(artifact.getGavc())))
                    .willReturn(
                            aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                                    .withBody(JsonUtils.serialize(artifact))
                    )
            );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
