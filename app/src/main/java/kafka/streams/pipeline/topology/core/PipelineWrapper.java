package kafka.streams.pipeline.topology.core;

import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.kafka.streams.kstream.ValueMapper;

/**
 * pipeline processing wrapper of kafka streams
 */
public class PipelineWrapper {
    private static final Logger logger = Logger.getLogger(PipelineWrapper.class.getName());

    /**
     * run the lambda function along with error handling
     *
     * @param <V> value data from the previous pipeline
     * @param <VR> value results data which will be passed to the next pipeline
     * @param <E> general exception
     * @param pipelineName the name of pipeline processing
     * @param handler lambda function which will be executed as pipeline processing
     * @return the list of value results data
     */
    public static <V, VR, E extends Exception>
        ValueMapper<V, Iterable<VR>> run(
            final String pipelineName,
            final PipelineHandler<V, VR, E> handler) {

        return new ValueMapper<V,Iterable<VR>>() {
            public Iterable<VR> apply(V v) {
                try {
                    var valueResults = handler.apply(v);
                    logger.info(String.format(
                        "Pipeline processing was executed: %s",
                        pipelineName));
                    return Arrays.asList(valueResults);

                } catch (Exception exception) {
                    new PipelineException(
                        String.format(
                            "failed to apply pipeline processing: %s",
                            pipelineName),
                        exception)
                        .printStackTrace();
                    return Arrays.asList();
                }
            }
        };
    }
}
