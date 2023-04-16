package kafka.streams.pipeline.topology.core;

/**
 * pipeline processing handler of kafka streams
 */
@FunctionalInterface
public interface PipelineHandler<S, R, E extends Exception> {
    /**
     * apply the lambda function as pipeline processing
     *
     * @param s source data from the previous pipeline processing
     * @return result data which will be passed to the next pipeline processing
     * @throws E general exception
     */
    R apply(S s) throws E;
}
