package kafka.streams.pipeline.topology.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PipelineWrapperTest {
    @Test
    void testRun() {
        var prefix = "Hello, ";
        var sourceValue = "pipelineWrapper!";

        var processing = PipelineWrapper.run(
            "testPipeline",
            val -> prefix + val);

        var results = processing.apply(sourceValue);

        for (String result : results) {
            assertEquals(
                prefix + sourceValue,
                result);
        }
    }
}
