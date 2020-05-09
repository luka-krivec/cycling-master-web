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

    private static final String DB_HOST = "ds045622.mlab.com";
    private static final int DB_PORT = 45622;
    private static String userName = "backend";
    private static String password = "backend4850";
    private static String database = "heroku_wv4vtr2f";

    private MongoClient client;
    private DB db;

    public MongoDBHelper() throws UnknownHostException {
        MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
        client = new MongoClient(new ServerAddress(DB_HOST, DB_PORT), Arrays.asList(credential));
        db = client.getDB(database);
    }

    public DBCollection getCollection(String name) {
        return db.getCollection(name);
    }

    public void destroy() {
        client.close();
    }

}
