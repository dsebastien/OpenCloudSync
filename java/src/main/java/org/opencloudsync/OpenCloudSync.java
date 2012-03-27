package org.opencloudsync;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.net.UnknownHostException;
import java.security.Security;

/**
 * Date: 24/01/12
 * Time: 08:58
 */
public class OpenCloudSync {
    public static void main(String... args){
        //todo pom.xml: downloadsources etc!


        // configure BouncyCastle as security provider
        Security.addProvider(new BouncyCastleProvider());

        // todo extract
        // connect to mongodb
        Mongo m = null;
        try {
            m = new Mongo("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // TODO remove later on
        // ensure the db is dropped
        m.dropDatabase("tests");
        // TODO rename later on
        DB db = m.getDB( "tests");

        // load/initialize repositories
        RepositoryManager repositoryManager = new RepositoryManager("./sample-data/.repository");
        IndexManager indexManager = new IndexManager(repositoryManager);

        File folderToWatch = new File("./sample-data");

        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(folderToWatch, indexManager);

        fileSystemWatcher.refresh();
    }
}
