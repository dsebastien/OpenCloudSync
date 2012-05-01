/**
 * OpenCloudSync: Open source cloud synchronization solution; an extensible software that allows you to synchronize your data with different storage systems.
 *
 *     Copyright (C) 2012 Sebastien Dubois <seb__at__dsebastien.net>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudsync;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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