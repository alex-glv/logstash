package org.apache.mesos.logstash.systemtest;

import org.apache.log4j.Logger;
import org.apache.mesos.mini.MesosCluster;
import org.apache.mesos.mini.mesos.MesosClusterConfig;

/**
 * Main app to run Mesos Logstash with Mini Mesos.
 */
public class Main {


    public static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        MesosCluster cluster = new MesosCluster(
                MesosClusterConfig.builder()
                .numberOfSlaves(1)
                .slaveResources(new String[]{"ports(*):[9200-9200,9300-9300]"})
                .build()
        );

        cluster.start();

        LOGGER.info("Starting scheduler");
        LogstashSchedulerContainer scheduler = new LogstashSchedulerContainer(cluster.getConfig().dockerClient, cluster.getMesosContainer().getIpAddress());
        scheduler.start();
        LOGGER.info("Scheduler started at http://" + scheduler.getIpAddress() + ":9092");

        LOGGER.info("Type CTRL-C to quit");
        while (true) {
            Thread.sleep(1000);
        }
    }

}
