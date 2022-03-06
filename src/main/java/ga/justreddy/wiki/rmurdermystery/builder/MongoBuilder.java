package ga.justreddy.wiki.rmurdermystery.builder;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoBuilder {

    private final MongoDatabase database;
    public MongoBuilder(String URI){
        MongoClientURI connectionString = new MongoClientURI(URI);
        MongoClient mongoClient = new MongoClient(connectionString);
        database = mongoClient.getDatabase("rmurdermystery");
    }

    public MongoCollection<Document> getDatabase(String collectionName) {
        return database.getCollection(collectionName);
    }

}
