package org.axway.grapes.maven.materials.stubs;

import org.axway.grapes.commons.datamodel.Artifact;
import org.axway.grapes.commons.datamodel.DataModelFactory;

public class Artifacts
{
    public static final Artifact junit = DataModelFactory.createArtifact("junit", "junit", "4.11", null, null, "jar");
    public static final Artifact fakeArtifact = DataModelFactory.createArtifact("org.fake", "fake", "2.0.0", null, null, "jar");
    public static final Artifact axwayFakeArtifact = DataModelFactory.createArtifact("org.axway", "fake", "1.0.0", null, null, "jar");
}
