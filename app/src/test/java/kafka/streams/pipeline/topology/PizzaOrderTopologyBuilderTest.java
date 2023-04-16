package kafka.streams.pipeline.topology;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.Test;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import kafka.streams.pipeline.properties.PropertyUtils;

public class PizzaOrderTopologyBuilderTest {
    private final PizzaOrderTopologyBuilder pizzaOrderTopologyBuilder = new PizzaOrderTopologyBuilder();
    @Test
    void testGetTopologyConfig() {
        var topologyConfig = pizzaOrderTopologyBuilder.getTopologyConfig();
        assertEquals(
            PropertyUtils.getProperty("kafka.streams.application.id"),
            topologyConfig.getProperty(StreamsConfig.APPLICATION_ID_CONFIG));
        assertEquals(
            PropertyUtils.getProperty("kafka.streams.bootstrap.servers"),
            topologyConfig.getProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(
            Serdes.String().getClass().getName(),
            topologyConfig.getProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG));
        assertEquals(
            GenericAvroSerde.class.getName(),
            topologyConfig.getProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG));
        assertEquals(
            PropertyUtils.getProperty("kafka.streams.schema.registry"),
            topologyConfig.getProperty(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG));
    }

    @Test
    void testCreateTopology() {
        var topology = pizzaOrderTopologyBuilder.createTopology();
        assertEquals("""
                Topologies:
                Sub-topology: 0
                Source: KSTREAM-SOURCE-0000000000 (topics: [pizzaOrders])
                --> KSTREAM-FLATMAPVALUES-0000000001
                Processor: KSTREAM-FLATMAPVALUES-0000000001 (stores: [])
                --> KSTREAM-FLATMAPVALUES-0000000002
                <-- KSTREAM-SOURCE-0000000000
                Processor: KSTREAM-FLATMAPVALUES-0000000002 (stores: [])
                --> KSTREAM-FLATMAPVALUES-0000000003
                <-- KSTREAM-FLATMAPVALUES-0000000001
                Processor: KSTREAM-FLATMAPVALUES-0000000003 (stores: [])
                --> none
                <-- KSTREAM-FLATMAPVALUES-0000000002
            """.replace("\n", "")
                .replace(" ", ""),
            topology.describe()
                    .toString()
                    .replace("\n", "")
                    .replace(" ", ""));
    }
}
