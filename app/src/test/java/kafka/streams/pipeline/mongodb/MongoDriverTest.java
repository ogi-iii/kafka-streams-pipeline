package kafka.streams.pipeline.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import kafka.streams.pipeline.jackson.PizzaOrderEntity;
import kafka.streams.pipeline.properties.PropertyUtils;

@TestInstance(Lifecycle.PER_CLASS)
public class MongoDriverTest {
    @Test
    void testInsertDocuments() {
        // an order report with two order lines
        var jsonString = """
                {
                    \"store_id\":8,
                    \"store_order_id\":1000,
                    \"coupon_code\":1650,
                    \"date\":18433,
                    \"status\":\"accepted\",
                    \"order_lines\":[
                        {
                            \"product_id\":65,
                            \"category\":\"salad\",
                            \"quantity\":5,
                            \"unit_price\":16.61,
                            \"net_price\":83.05
                        },
                        {
                            \"product_id\":66,
                            \"category\":\"salad\",
                            \"quantity\":4,
                            \"unit_price\":16.61,
                            \"net_price\":66.44
                        }
                    ]
                }
            """;

        try (var mongoDriver = new MongoDriver(PropertyUtils.getProperty("mongodb.database"))) {
            var mapper = new ObjectMapper();
            var pizzaOrderEntity = mapper.readValue(jsonString, PizzaOrderEntity.class);

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

            var insertedDocuments = mongoDriver.insertDocuments(
                PropertyUtils.getProperty("mongodb.collection"),
                documents);

            // check if the order lines were inserted as each document
            assertEquals(2, insertedDocuments);

        } catch (IOException e) {
            fail(e);
        }
    }

    @AfterAll
    void cleanup() {
        var addr = PropertyUtils.getProperty("mongodb.host");
        var port = PropertyUtils.getProperty("mongodb.port");
        var user = PropertyUtils.getProperty("mongodb.user");
        var passwd = PropertyUtils.getProperty("mongodb.passwd");

        var uri = new MongoClientURI(
            String.format(
                "mongodb://%s:%s@%s:%s",
                user, passwd, addr, port));

        try (var mongoClient = new MongoClient(uri)) {
            // remove database used for unit tests
            mongoClient.getDatabase(
                PropertyUtils.getProperty("mongodb.database"))
            .drop();

        } catch (Exception e) {
            fail(e);
        }
    }
}
