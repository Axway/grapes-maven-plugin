package org.axway.grapes.maven;

import java.util.List;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

public abstract class AbstractGrapesMojo extends AbstractMojo {

    /**
     * @parameter default-value="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

	/**
     * Indicates whether the build will continue even if there are clean errors.
     * If true, an exception will stop the maven execution on error
     * If false, the error will be logged the maven life cycle will continue.
     * @parameter property="grapes.failOnError"
     */
    protected boolean failOnError = true;
    
    /**
     * @parameter property="project"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * The projects in the reactor.
     *
     * @parameter property="reactorProjects"
     * @readonly
     */
    protected List<MavenProject> reactorProjects;
    
    /**
     * Flag used to suppress execution.
     *
     * @parameter property="grapes.skip"
     */
    protected boolean skip;
    
    public boolean isExecuteSkipped() {
    	if (skip) {
    		getLog().info("Skipping Grapes execution.");
    	}
    	return skip;
    }

}
