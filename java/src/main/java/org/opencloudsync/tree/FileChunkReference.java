package org.opencloudsync.tree;

import org.apache.commons.lang3.Validate;
import org.opencloudsync.DigestHolder;
import org.opencloudsync.FileChunk;

/**
 * Immutable reference to a file chunk in the repository.
 * Date: 20/01/12
 * Time: 17:24
 */
public class FileChunkReference implements Node, DigestHolder {
    /**
     * Digest of the file chunk.
     */
    private final byte[] digest;
    /**
     * String version of the digest.
     */
    private final String digestAsHexString;

    public FileChunkReference(final FileChunk fileChunk){
        Validate.notNull(fileChunk, "The file chunk cannot be null!");
        //todo alternative: pass directly the digest & hex string to ensure that
        //it's not possible to keep a handle on the FileChunk (it must be freed right after to avoid wasting memory)
        this.digest = fileChunk.getDigest();
        this.digestAsHexString = fileChunk.getDigestAsHexString();
    }

    public byte[] getDigest(){
        return digest;
    }

    public String getDigestAsHexString(){
        return digestAsHexString;
    }

    public boolean isLeaf() {
        return true;
    }
}
