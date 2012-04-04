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
import java.security.Security;

/**
 * OpenCloudSync's entry point.
 */
public final class OpenCloudSync {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCloudSync.class);

    private final Configuration configuration;
    private IndexManager indexManager;
    private FileSystemWatcher fileSystemWatcher;
    
    public OpenCloudSync(final Configuration configuration){
        this.configuration = configuration;
        LOGGER.info("OpenCloudSync v"+configuration.getVersion()+". Loading...");
    }

    @Required
    public void setIndexManager(final IndexManager indexManager){
        this.indexManager = indexManager;
    }

    @Required
    public void setFileSystemWatcher(final FileSystemWatcher fileSystemWatcher){
        this.fileSystemWatcher = fileSystemWatcher;
    }

    public void start(){
        File folderToWatch = new File(configuration.getFolderToWatch());

        try {
            fileSystemWatcher.start();
        } catch (Exception e) {
            LOGGER.warn("Failed to start the file system watcher", e);
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