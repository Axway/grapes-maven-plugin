package org.axway.grapes.maven;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.axway.grapes.commons.datamodel.Module;
import org.axway.grapes.utils.client.GrapesClient;

/**
 * Goal which gathers and send dependencies information to Grapes.
 *
 * @goal notify
 * @phase install
 */
public class NotifyMojo extends AbstractGrapesMojo {

    /**
     * Host of the targeted Grapes server
     * @parameter property="grapes.host"
     * @required
     */
    private String host;

    /**
     * Port of the targeted Grapes server
     * @parameter property="grapes.port"
     */
    private String port;

    /**
     * Grapes user to use during the notification
     * @parameter property="grapes.user"
     */
    private String user;

    /**
     * Password of the Grapes user
     * @parameter property="grapes.password"
     */
    private String password;

    public void execute() throws MojoExecutionException {
        // Execute only one time
        if(project.equals(reactorProjects.get(0))){
            try {
                final File workingFolder = GrapesMavenPlugin.getGrapesPluginWorkingFolder(reactorProjects.get(0));
                final Module rootModule = GrapesMavenPlugin.getModule(workingFolder, GrapesMavenPlugin.MODULE_JSON_FILE_NAME);
                getLog().info("Sending " + rootModule.getName() + "...");

                getLog().info("Connection to Grapes");
                getLog().info("Host: " + host);
                getLog().info("Port: " + port);
                getLog().info("User: " + user);
                final GrapesClient client = new GrapesClient(host, port);

                if(!client.isServerAvailable()){
                    throw new MojoExecutionException("Grapes is unreachable");
                }

                client.postModule(rootModule, user, password);

                getLog().info("Information successfully sent");

            } catch (Exception e) {
                if(failOnError){
                    throw new MojoExecutionException("An error occurred during Grapes server Notification." , e);
                }
                else{
                    getLog().debug("An error occurred during Grapes server Notification.", e);
                    getLog().info("Failed to send information to Grapes");
                }
            }
        }
    }
}