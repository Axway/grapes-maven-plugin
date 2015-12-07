package org.axway.grapes.maven.materials.stubs;


import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;

public class SubModule1ProjectStub extends AbstractProjectStub {

    @Override
    public String getProjectPath() {
        return "multi-module-project/subModule1";
    }

    @Override
    public List<Artifact> getAttachedArtifacts() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getModules() {
        return Collections.emptyList();
    }

    @Override
    public List<Dependency> getDependencies() {
        Dependency dependency = new Dependency();
        dependency.setGroupId("org.axway");
        dependency.setArtifactId("fake");
        dependency.setVersion("1.0.0");
        return Collections.singletonList(dependency);
    }
}
