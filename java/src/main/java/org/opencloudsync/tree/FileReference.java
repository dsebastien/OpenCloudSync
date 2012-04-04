package org.opencloudsync.tree;

import org.opencloudsync.DigestHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 20/01/12
 * Time: 17:23
 */
public class FileReference implements Node, DigestHolder {
    private final String name;
    //todo add other metadata (?)
    private final List<FileChunkReference> chunks = new ArrayList<FileChunkReference>();
    
    public FileReference(final String name, final List<FileChunkReference> chunks){
        //todo check args
        this.name = name;
        this.chunks.addAll(chunks); // we just add the given chunks

        //todo calculate digest
    }

    public String getName() {
        return name;
    }

    public List<FileChunkReference> getChunks() {
        return chunks;
    }

    public boolean isLeaf() {
        return true;
    }

    public byte[] getDigest() {
        // todo implement
        return new byte[0];
    }

    public String getDigestAsHexString() {
        // todo implement
        return null;
    }
}
