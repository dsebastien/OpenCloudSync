package org.opencloudsync;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.opencloudsync.tree.*;
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
    
    //todo add ignore list (text file in the root folder? e.g., .openCloudSyncIgnoreList

    public FileSystemWatcher(){
    }

    @Required
    public void setFolderToWatch(final File folderToWatch){
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
        // TODO do a first pass on the file system then provide the info to the index manager

        //todo implement file filter (also for the file alteration observer)
        refresh();

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

    // todo check the index to verify if the folder was already watched
    // load root folder info (via File)
    // check index for that folder name in the known TreeReference list
    // need the latest version

    /**
     * Refresh the in-memory tree
     */
    public void refresh(){
        // todo auto trigger through quartz job? or sleep x units of time

        Node rootNode = buildInMemorySubTree(folderToWatch);
        
        TreeReference newTree = new TreeReference(rootNode);
        
        // todo implement
        // todo compare newly created tree with existing one:
        // merge both and create "changes" objects along the way
        // once all changes are gathered: push to the index manager to be inserted in the db as an atomic operation (that will also trigger the repository update)
    }
    
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
                e.printStackTrace();  //todo handle error
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
