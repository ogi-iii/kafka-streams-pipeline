package kafka.streams.pipeline.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import kafka.streams.pipeline.properties.PropertyUtils;

/**
 * mongodb client driver
 */
public class MongoDriver implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(MongoDriver.class.getName());

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    /**
     * constructor
     *
     * @param databaseName database name of mongodb
     */
    public MongoDriver(final String databaseName) {
        var addr = PropertyUtils.getProperty("mongodb.host");
        var port = PropertyUtils.getProperty("mongodb.port");
        var user = PropertyUtils.getProperty("mongodb.user");
        var passwd = PropertyUtils.getProperty("mongodb.passwd");

        var uri = new MongoClientURI(
            String.format(
                "mongodb://%s:%s@%s:%s",
                user, passwd, addr, port));
        this.mongoClient = new MongoClient(uri);

        // create the database if not exists
        this.mongoDatabase = mongoClient.getDatabase(databaseName);
        logger.info(
            String.format(
                "The mongodb client was connected to the database '%s'.",
                this.mongoDatabase.getName()));
    }

    /**
     * bulk insert the documents to the target collection in mongodb
     *
     * @param collectionName collection name in the database of mongodb
     * @param documents the list of source bson documents
     * @return the number of inserted documents
     */
    public int insertDocuments(final String collectionName, final List<Document> documents) {
        try {
            // create the collection if not exists
            if (!this.mongoDatabase.listCollectionNames().into(new ArrayList<String>()).contains(collectionName)) {
                this.mongoDatabase.createCollection(collectionName);
                logger.info(String.format("The collection '%s' was created in mongodb.", collectionName));
            }
            var mongoCollection = this.mongoDatabase.getCollection(collectionName);

            mongoCollection.insertMany(documents);
            logger.info(
                String.format(
                    "Total %s documents were inserted to the collection '%s' in mongodb.",
                    documents.size(), collectionName));
            return documents.size();

        } catch (Exception e) {
            logger.severe("failed to insert the documents to mongodb");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * close mongodb client connection
     */
    public void close() {
        this.mongoClient.close();
        logger.info(String.format("The mongodb client was closed."));
    }
}
