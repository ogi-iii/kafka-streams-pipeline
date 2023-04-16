package kafka.streams.pipeline;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class StreamsAppTest {
    @Test
    void testMain() {
        var waitTimeSeconds = 5;

        new Thread(){
            @Override
            public void run() {
                StreamsApp.main(null);
            }
        }.start();

        try {
            Thread.sleep(waitTimeSeconds * 1000);

        } catch (InterruptedException e) {
            fail(e);
        }
    }
}
