package org.opencloudsync.tree;

import org.opencloudsync.FileChunkReference;
import org.opencloudsync.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 20/01/12
 * Time: 17:23
 */
public class FileReference implements Node {
    //todo final? how to update indexed information? create brand new objects all the time or update these?
    private final String name;
    //todo add other metadata (?)
    private final List<FileChunkReference> chunks = new ArrayList<FileChunkReference>();
    
    public FileReference(final String name, final List<FileChunkReference> chunks){
        //todo check args
        this.name = name;
        this.chunks.addAll(chunks); // we just add the given chunks
    }

    public String getName() {
        return name;
    }

    public List<FileChunkReference> getChunks() {
        return chunks;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
