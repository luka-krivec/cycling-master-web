package utils;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

public class MongoDBHelper {

    private static final String DB_HOST = "127.2.246.4";
    private static final int DB_PORT = 27017;
    private static String userName = "admin";
    private static String password = "Xye4Uz_rPsU2";
    private static String database = "cyclingmaster";

    private MongoClient client;
    private MongoDatabase db;

    public MongoDBHelper() {
        //MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
        MongoClientURI uri = new MongoClientURI("mongodb://" + userName + ":" + password + "@" +
                DB_HOST + ":" + DB_PORT + "/?authSource=" + database);
        client = new MongoClient(uri);
        db = client.getDatabase("tracker");
    }

    public MongoCollection getCollection(String name) {
        return db.getCollection(name);
    }

    public void destroy() {
        client.close();
    }

}
