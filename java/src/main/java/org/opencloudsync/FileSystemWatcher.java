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

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.opencloudsync.tree.FolderReference;
import org.opencloudsync.tree.Node;
import org.opencloudsync.tree.TreeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FileSystemWatcher implements FileAlterationListener{
    //todo handle multiple folders?
    //todo store folders to watch in a flat file?
    private File folderToWatch;
    private IndexManager indexManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemWatcher.class);
    private TreeReference currentTree;

    private FileReader fileReader;
    
    private FileAlterationObserver fileAlterationObserver;
    private FileAlterationMonitor fileAlterationMonitor;
    
    //todo add ignore list (text file in the root folder? e.g., .openCloudSyncIgnoreList)

    public FileSystemWatcher(){
    }

    @Required
    public void setFolderToWatch(final File folderToWatch){ // todo handle multiple folders; the treereference & db are ready
        //todo check arg (folder and not file, exists, ...)
        this.folderToWatch = folderToWatch;

        // create dummy tree for starters
        FolderReference rootNode = new FolderReference(folderToWatch.getName(), new ArrayList());
        this.currentTree = new TreeReference(rootNode);
    }

    @Required
    public void setIndexManager(final IndexManager indexManager){
        //todo check arg
        this.indexManager = indexManager;
    }

    @Required
    public void setFileReader(final FileReader fileReader){
        //todo check arg
        this.fileReader = fileReader;
    }

    public void start() throws Exception{
        //todo implement file filter (also for the file alteration observer)
        refresh();

        // update the index with the current situation on disk
        indexManager.update(currentTree);

        //todo refresh a second time and invoke save again

        // start observing the file system for changes
        // http://commons.apache.org/io/apidocs/org/apache/commons/io/monitor/FileAlterationObserver.html
        fileAlterationObserver = new FileAlterationObserver(folderToWatch);

        fileAlterationObserver.addListener(this); // the file system watcher will be notified of changes
        fileAlterationMonitor = new FileAlterationMonitor(100, fileAlterationObserver);

        fileAlterationMonitor.run();
        try {
            fileAlterationMonitor.start();
        } catch (Exception e) {
            LOGGER.warn("Failed to start the file alteration monitor", e);
            throw e;
        }
    }

    /**
     * Refresh the in-memory tree
     */
    public void refresh(){
        // todo auto trigger through quartz job? or sleep x units of time
        currentTree = new TreeReference((FolderReference)buildInMemorySubTree(folderToWatch));
    }

    /**
     * Recreates the in-memory tree based on the actual situation on disk.
     * @param file watched folder
     * @return the root node of the tree
     */
    private Node buildInMemorySubTree(final File file){
        Node retVal = null;

        if(file.isDirectory()){
            // todo check if it still exists
            List<Node> children = new ArrayList<Node>();
            for(File childFile: file.listFiles()){
                children.add(buildInMemorySubTree(childFile));
            }
            retVal = new FolderReference(file.getName(), children);
        }else{
            try {
                retVal = fileReader.readFile(file);
            } catch (FileNotFoundException e) {
                LOGGER.error("Problem occurred while reading file: "+file.getAbsolutePath(), e);  //todo handle error
            }
        }
        
        return retVal;
    }

    //TODO implement/test
    //TODO when modifications are detected by the file alteration observer, notify the index manager
    public void onStart(FileAlterationObserver observer) {
        //LOGGER.info("Folder monitoring started!");
    }

    public void onDirectoryCreate(File directory) {
        //LOGGER.info("Directory created: "+directory.getAbsolutePath());
    }

    public void onDirectoryChange(File directory) {
        //LOGGER.info("Directory changed: "+directory.getAbsolutePath());
    }

    public void onDirectoryDelete(File directory) {
        //LOGGER.info("Directory deleted: "+directory.getAbsolutePath());
    }

    public void onFileCreate(File file) {
        //LOGGER.info("File created: "+file.getAbsolutePath());
    }

    public void onFileChange(File file) {
        //LOGGER.info("File changed: "+file.getAbsolutePath());
    }

    public void onFileDelete(File file) {
        //LOGGER.info("File deleted: "+file.getAbsolutePath());
    }

    public void onStop(FileAlterationObserver observer) {
        //LOGGER.info("Folder monitoring stopped!");
    }
}
