package kafka.streams.pipeline.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PropertyUtilsTest {
    @Test
    void testGetProperty_001() {
        assertEquals(
            "value",
            PropertyUtils.getProperty("test.property"));
    }

    @Test
    void testGetProperty_002() {
        assertEquals(
            "",
            PropertyUtils.getProperty("blank.property"));
    }

    @Test
    void testGetProperty_003() {
        assertEquals(
            "",
            PropertyUtils.getProperty("unknown.property"));
    }

    @Test
    void testGetProperty_004() {
        assertEquals(
            "default",
            PropertyUtils.getProperty("unknown.property", "default"));
    }
}
