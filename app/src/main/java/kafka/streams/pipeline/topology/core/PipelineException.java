package kafka.streams.pipeline.topology.core;

/**
 * exception for kafka streams pipeline
 */
public class PipelineException extends Exception {
    /**
     * constructor
     *
     * @param message error message
     * @param cause source exception
     */
    public PipelineException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * constructor
     *
     * @param message error message
     */
    public PipelineException(final String message) {
        super(message);
    }
}
