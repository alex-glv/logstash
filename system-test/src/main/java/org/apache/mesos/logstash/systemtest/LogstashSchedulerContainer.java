package org.apache.mesos.logstash.systemtest;

/**
 * Created by aleks on 17/09/15.
 */

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import org.apache.mesos.mini.container.AbstractContainer;

import java.security.SecureRandom;

/**
 * Container for the Elasticsearch scheduler
 */
public class LogstashSchedulerContainer extends AbstractContainer {

    public static final String SCHEDULER_IMAGE = "mesos/logstash-scheduler";

    public static final String SCHEDULER_NAME = "logstash-scheduler";

    private String mesosIp;

    private String frameworkRole;

    private String zookeeperFrameworkUrl;

    private String dataDirectory;

    protected LogstashSchedulerContainer(DockerClient dockerClient, String mesosIp) {
        super(dockerClient);
        this.mesosIp = mesosIp;
        this.frameworkRole = "*"; // The default
    }

    protected LogstashSchedulerContainer(DockerClient dockerClient, String mesosIp, String frameworkRole) {
        super(dockerClient);
        this.mesosIp = mesosIp;
        this.frameworkRole = frameworkRole;
    }

    @Override
    protected void pullImage() {
        this.pullImage(SCHEDULER_IMAGE, "latest");
    }

    @Override
    protected CreateContainerCmd dockerCommand() {
        return dockerClient
                .createContainerCmd(SCHEDULER_IMAGE)
                .withName(SCHEDULER_NAME + "_" + new SecureRandom().nextInt())
                .withExposedPorts(new ExposedPort(9092))
                .withEnv("JAVA_OPTS=-Xms128m -Xmx256m -Dmesos.zk=" + this.getZookeeperMesosUrl());
    }

    public String getZookeeperMesosUrl() {
        return "zk://" + mesosIp + ":2181/mesos";
    }

    public void setZookeeperFrameworkUrl(String zookeeperFrameworkUrl) {
        this.zookeeperFrameworkUrl = zookeeperFrameworkUrl;
    }
}
