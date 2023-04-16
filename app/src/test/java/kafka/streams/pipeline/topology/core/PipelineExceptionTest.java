package kafka.streams.pipeline.topology.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class PipelineExceptionTest {
    @Test
    void testConstructor_001() {
        var message = "testing";
        var pipelineException = new PipelineException(message);
        assertEquals(
            message,
            pipelineException.getMessage());
    }

    @Test
    void testConstructor_002() {
        var message = "testing";
        var cause = new IOException(message);
        var pipelineException = new PipelineException(cause.getMessage(), cause);
        assertEquals(
            message,
            pipelineException.getMessage());
        assertEquals(
            cause,
            pipelineException.getCause());
        }
}
