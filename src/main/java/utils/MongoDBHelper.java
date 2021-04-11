package utils;


import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;

public class MongoDBHelper {

    //private static final String DB_HOST = "127.0.0.1";
    /*private static final String DB_HOST = "127.2.246.4";
    private static final int DB_PORT = 27017;
    private static String userName = "backend";
    private static String password = "backend4850";
    private static String database = "cyclingmaster";*/

    /*private static final String DB_HOST = "ds045622.mlab.com";
    private static final int DB_PORT = 45622;
    private static String userName = "backend";
    private static String password = "backend4850";
    private static String database = "heroku_wv4vtr2f";*/

    // cloud.mongodb.com
    private static final String DB_HOST = "cluster0.leml1.mongodb.net";
    private static String userName = "android";
    private static String password = "android4850";
    private static String databaseName = "tracker";

    private MongoClient client;
    private DB database;

    public MongoDBHelper() throws UnknownHostException {

        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://" + userName + ":" + password + "@" + DB_HOST + "/" + databaseName + "?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        database = client.getDB(databaseName);
    }

    public DBCollection getCollection(String name) {
        return database.getCollection(name);
    }

    public void destroy() {
        client.close();
    }

}
