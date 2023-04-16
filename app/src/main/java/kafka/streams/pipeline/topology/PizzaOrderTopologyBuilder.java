package kafka.streams.pipeline.topology;

import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import kafka.streams.pipeline.jackson.PizzaOrderEntity;
import kafka.streams.pipeline.mongodb.MongoDriver;
import kafka.streams.pipeline.properties.PropertyUtils;
import kafka.streams.pipeline.topology.core.PipelineWrapper;
import lombok.Data;

/**
 * kafka streams topology builder which handles the messages of the pizza orders
 */
@Data
public class PizzaOrderTopologyBuilder {
    private static final Logger logger = Logger.getLogger(PizzaOrderTopologyBuilder.class.getName());

    private final Properties topologyConfig = new Properties();

    /**
     * constructor
     */
    public PizzaOrderTopologyBuilder() {
        // required settings
        this.topologyConfig.put(
            StreamsConfig.APPLICATION_ID_CONFIG,
            PropertyUtils.getProperty("kafka.streams.application.id"));
        this.topologyConfig.put(
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
            PropertyUtils.getProperty("kafka.streams.bootstrap.servers"));
        // serdes settings
        this.topologyConfig.put(
            StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
            Serdes.String().getClass().getName());
        this.topologyConfig.put(
            StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
            GenericAvroSerde.class.getName());
        this.topologyConfig.put(
            AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
            PropertyUtils.getProperty("kafka.streams.schema.registry"));
        logger.info("The configs of topology were loaded.");
    }

    /**
     * create the topology composed of some pipeline processing
     *
     * @return the topology of kafka streams
     */
    public Topology createTopology() {
        var builder = new StreamsBuilder();
        var mapper = new ObjectMapper();

        builder.stream(
                PropertyUtils.getProperty("kafka.streams.topic"))
            .flatMapValues(
                PipelineWrapper.run(
                    "JsonStringConverter",
                    record -> record.toString()))
            .flatMapValues(
                PipelineWrapper.run(
                    "PizzaOrderEntityLoader",
                    jsonString -> mapper.readValue(
                        jsonString,
                        PizzaOrderEntity.class)))
            .flatMapValues(
                PipelineWrapper.run(
                    "MongoDocumentInserter",
                    pizzaOrderEntity -> insertMongoDocuments(pizzaOrderEntity)));

        logger.info("The topology was created.");
        return builder.build();
    }

    /**
     * insert the entities as documents into the collection in mongodb
     *
     * @param pizzaOrderEntity json entity for the pizza order
     * @return the number of inserted documents
     */
    private int insertMongoDocuments(final PizzaOrderEntity pizzaOrderEntity) {
        try (var mongoDriver = new MongoDriver(PropertyUtils.getProperty("mongodb.database"))) {
            var documents = pizzaOrderEntity.getOrder_lines()
                .stream()
                .parallel()
                .map(orderLineEntity -> {
                    var document = new Document("_id", new ObjectId());
                    document.append("store_id", pizzaOrderEntity.getStore_id())
                            .append("store_order_id", pizzaOrderEntity.getStore_order_id())
                            .append("product_id", orderLineEntity.getProduct_id())
                            .append("category", orderLineEntity.getCategory())
                            .append("quantity", orderLineEntity.getQuantity())
                            .append("total_amount", orderLineEntity.getNet_price())
                            .append("date", pizzaOrderEntity.getDate());
                    return document;
                    })
                .toList();
            return mongoDriver.insertDocuments(
                PropertyUtils.getProperty("mongodb.collection"),
                documents);

        } catch (Exception e) {
            throw e;
        }
    }
}
