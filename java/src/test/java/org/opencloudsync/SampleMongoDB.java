package org.opencloudsync;

import com.mongodb.*;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Set;

public class SampleMongoDB {
    public static void main(String ... args){
        // configure BouncyCastle as security provider
        Security.addProvider(new BouncyCastleProvider());

        // mongodb
        Mongo m = null;
        try {
            m = new Mongo("localhost", 27017);
            System.out.println(m);
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        DB db = m.getDB( "mydb" );
        System.out.println(db.getName());


        DBObject dbObject = com.mongodb.BasicDBObjectBuilder.start().add("1","bla").get();
        //DBCollection collection = db.createCollection("indexCollection", dbObject);

        DBCollection indexCol = db.getCollection("indexCollection");
        //indexCol.insert(BasicDBObjectBuilder.start().add("1","first").add("2","second").get());

        String string = "Hello world!";
        byte[] stringBytes = null;
        try {
            stringBytes = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        // digest generation
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("SHA-1", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchProviderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        hash.update(stringBytes);
        byte[] digest = hash.digest();

        System.out.println("Input: " + new String(Hex.encode(stringBytes)));
        System.out.println("SHA-1: " + new String(Hex.encode(digest)));

        Set<String> colls = db.getCollectionNames();

        for (String s : colls) {
            System.out.println(s);
            DBCollection currentCol = db.getCollection(s);
            DBCursor cursor = currentCol.find();
            while(cursor.hasNext()){
                DBObject curObj = cursor.next();
                for(String key: curObj.keySet()){
                    System.out.println("Property: "+key+" - Value: "+curObj.get(key));
                }
            }
        }

    }
}
