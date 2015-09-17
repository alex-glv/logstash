package org.apache.mesos.logstash.systemtest;

import org.apache.mesos.mini.MesosCluster;
import org.apache.mesos.mini.mesos.MesosClusterConfig;

public class LocalCluster {
    public final MesosClusterConfig clusterConfig = MesosClusterConfig.builder()
        .numberOfSlaves(1)
        .slaveResources(new String[]{"ports(*):[9299-9299,9300-9300]"})
        .build();

    private LocalMesosCluster cluster = new LocalMesosCluster(clusterConfig);

    public static void main(String[] args) throws Exception {
        new LocalCluster().run();
    }

    private void run() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(cluster::stop));
        cluster.start();

        DummyFrameworkContainer dummyFrameworkContainer = new DummyFrameworkContainer(
            cluster.getInnerDockerClient(), "dummy-framework_" + Math.random());
        dummyFrameworkContainer.start();

        String zkAddress = cluster.getMesosContainer().getIpAddress() + ":2181";

        System.setProperty("mesos.zk", "zk://" + zkAddress + "/mesos");
        System.setProperty("mesos.logstash.logstash.heap.size", "128");
        System.setProperty("mesos.logstash.executor.heap.size", "64");

        LogstashSchedulerContainer scheduler = new LogstashSchedulerContainer(cluster.getInnerDockerClient(), cluster.getMesosContainer().getIpAddress());
        scheduler.start();


        System.out.println("");
        System.out.println("Cluster Started.");
        System.out.println("MASTER URL: " + cluster.getMesosContainer().getMesosMasterURL());
        System.out.println("Press CTRL-C to exit.");

        while (!Thread.currentThread().isInterrupted()) {
            Thread.sleep(5000);
        }
    }

    class LocalMesosCluster extends MesosCluster {

        public LocalMesosCluster(MesosClusterConfig config) {
            super(config);
        }

        public void stop() {
            this.after();
        }
    }

}
