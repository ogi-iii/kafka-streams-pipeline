package kafka.streams.pipeline;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;

import kafka.streams.pipeline.properties.PropertyUtils;
import kafka.streams.pipeline.topology.PizzaOrderTopologyBuilder;


public class StreamsApp {
    private static final Logger logger = Logger.getLogger(StreamsApp.class.getName());

    public static void main(String[] args) {
        try {
            // prepare the topology of kafka streams pipeline
            var topologyBuilder = new PizzaOrderTopologyBuilder();
            var pipelineTopology = topologyBuilder.createTopology();
            logger.info(
                pipelineTopology.describe()
                                .toString());

            // create kafka streams
            var streamsPipeline = new KafkaStreams(
                pipelineTopology,
                topologyBuilder.getTopologyConfig());

            // define the number of kafka streams
            var latch = new CountDownLatch(1);

            // prepare to handle uncaught exceptions in kafka streams
            streamsPipeline.setUncaughtExceptionHandler(exception -> {
                logger.severe(
                    String.format(
                        "uncaught exception was handled: %s",
                        exception));
                // release latch to exit pipeline app
                latch.countDown();
                return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
            });

            // attach shutdown hook to catch SIGINT (ctrl+C)
            Runtime.getRuntime()
                .addShutdownHook(
                    new Thread(
                        PropertyUtils.getProperty("kafka.streams.application.id")) {
                    @Override
                    public void run() {
                        logger.severe("Streams pipeline was terminated.");
                        streamsPipeline.close();
                        latch.countDown();
                    }
                });

            // execute the kafka streams pipeline
            logger.info("Streams pipeline was started.");
            streamsPipeline.start();
            latch.await();

        } catch (Exception e) {
            logger.severe("Streams pipeline was aborted.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
