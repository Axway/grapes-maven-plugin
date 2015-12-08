package org.axway.grapes.maven.materials.stubs;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.axway.grapes.commons.datamodel.Scope;

public class MultiModuleProjectStub extends AbstractProjectStub {

    @Override
    public String getProjectPath() {
        return "multi-module-project";
    }

    @Override
    public List<Artifact> getAttachedArtifacts() {
        final Artifact attachedArtifact = new DefaultArtifact(
                getGroupId(),
                getArtifactId(),
                getVersion(),
                Scope.COMPILE.toString(),
                "zip",
                "materials",
                new DefaultArtifactHandler()
        );
        return Collections.singletonList(attachedArtifact);
    }

    @Override
    public List<String> getModules() {
        final List<String> modules = new ArrayList<String>();
        modules.add("subModule1");
        modules.add("subModule2");

        return modules;
    }

    @Override
    public List<MavenProject> getCollectedProjects() {
        final List<MavenProject> modules = new ArrayList<MavenProject>();
        modules.add(this);
        modules.add(new SubModule1ProjectStub());
        modules.add(new SubModule2ProjectStub());

        return modules;
    }

    @Override
    public List<Dependency> getDependencies() {
        Dependency junit = new Dependency();
        junit.setGroupId(Artifacts.junit.getGroupId());
        junit.setArtifactId(Artifacts.junit.getArtifactId());
        junit.setVersion(Artifacts.junit.getVersion());
        return Collections.singletonList(junit);
    }
}
