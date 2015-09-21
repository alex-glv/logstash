package org.apache.mesos.logstash.executor;

import org.apache.mesos.MesosExecutorDriver;

import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getCanonicalName());
//    private static final long MAX_LOG_SIZE = 5_000_000;

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {

//        LOGGER.info("Starting executor...");
//        DockerClient dockerClient = new DockerClient();
//        LOGGER.info("Started dockerClient.");
//        FileLogSteamWriter writer = new FileLogSteamWriter(MAX_LOG_SIZE);
//        LOGGER.info("Started writer");
//        DockerStreamer streamer = new DockerStreamer(writer, dockerClient);
//        LOGGER.info("Started streamer");
//        DockerLogStreamManager streamManager = new DockerLogStreamManager(streamer);
//        LOGGER.info("Started streamManager");
//        LogstashService logstashService = new LogstashService(dockerClient);
//        logstashService.start();
//        LOGGER.info("Started logstashService");
//        LOGGER.info("Started executor");
//        LOGGER.info("Wrapped driver");
        new MesosExecutorDriver(new LogstashExecutor()).run();

//        logstashService.stop();
//        dockerClient.stop();

//        if (status.equals(Protos.Status.DRIVER_STOPPED)) {
//            System.exit(0);
//        } else {
//            System.exit(1);
//        }
    }
}
