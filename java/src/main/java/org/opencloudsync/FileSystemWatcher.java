package org.opencloudsync;

import org.opencloudsync.tree.FolderReference;
import org.opencloudsync.tree.Node;
import org.opencloudsync.tree.TreeReference;

import java.io.File;

/**
 *
 */
public class FileSystemWatcher {
    //todo handle multiple folders?
    private final File folderToWatch;
    private final IndexManager indexManager;

    private TreeReference currentTree;

    //todo add ignore list (text file in the root folder? e.g., .openCloudSyncIgnoreList

    public FileSystemWatcher(final File folderToWatch, final IndexManager indexManager){
        //TODO validate arguments + validate that Folder and not file, exists, etc

        FolderReference rootNode = new FolderReference(folderToWatch.getName());
        this.currentTree = new TreeReference(rootNode);

        this.folderToWatch = folderToWatch;
        this.indexManager = indexManager;
    }

    // check the index to verify if the folder was already watched
    // load root folder info (via File)
    // check index for that folder name in the known TreeReference list
    // need the latest version


    /**
     * Refresh the in-memory tree
     */
    public void refresh(){
        // todo auto trigger through quartz job? or sleep x units of time

        // todo improve how to manage this? pattern? avoid instanceof!!
        FolderReference rootNode = (FolderReference) currentTree.getRootNode();

        // todo implement
    }
}
