package org.opencloudsync;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.security.Security;

/**
 * OpenCloudSync's entry point.
 */
public final class OpenCloudSync {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCloudSync.class);

    private final Configuration configuration;
    private IndexManager indexManager;
    
    public OpenCloudSync(final Configuration configuration){
        this.configuration = configuration;
    }

    @Required
    public void setIndexManager(final IndexManager indexManager){
        this.indexManager = indexManager;
    }

    public void start(){
        File folderToWatch = new File(configuration.getFolderToWatch());

        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(folderToWatch, indexManager);
        fileSystemWatcher.refresh();

        //http://commons.apache.org/io/apidocs/org/apache/commons/io/monitor/FileAlterationObserver.html
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(folderToWatch);
        fileAlterationObserver.addListener(indexManager); // the index manager will be notified of changes
        FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(100, fileAlterationObserver);

        fileAlterationMonitor.run();
        try {
            fileAlterationMonitor.start();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Entry point
     * @param args
     */
    public static void main(final String... args){
        LOGGER.trace("Initializing Spring");
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ctx.registerShutdownHook();

        LOGGER.trace("Configuring Bouncy Castle as security provider");
        Security.addProvider(new BouncyCastleProvider());

        OpenCloudSync openCloudSync = ctx.getBean(OpenCloudSync.class);
        openCloudSync.start();
    }
}