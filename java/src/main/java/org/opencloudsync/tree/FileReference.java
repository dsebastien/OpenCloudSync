package org.opencloudsync.tree;

import org.apache.commons.codec.binary.Hex;
import org.opencloudsync.DigestHolder;
import org.opencloudsync.utils.DigestUtils;
import org.springframework.ui.context.Theme;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Immutable reference to a file whose chunks are all available in the repository.
 * Date: 20/01/12
 * Time: 17:23
 */
public class FileReference implements Node, DigestHolder {
    /**
     * The name of the file.
     */
    private final String name;
    /**
     * The digest of the file (combination of the digests of all chunks of the file.
     */
    private byte[] digest;
    /**
     * String version of the file digest.
     */
    private String digestAsHexString;

    //todo add other metadata (?)
    private final List<FileChunkReference> chunks = new ArrayList<FileChunkReference>();
    
    public FileReference(final String name, final List<FileChunkReference> chunks){
        //todo check args
        this.name = name;
        this.chunks.addAll(chunks); // we just add the given chunks

        // The hash of a file is the combination of the digests of all chunks it is made of
        final MessageDigest messageDigest = DigestUtils.getShaDigest();
        for(Node node: chunks){
            DigestUtils.updateDigest(messageDigest, node.getDigest());
        }
        //The file name is not part of the digest so that a file remains recognizable even if it was renamed
        //DigestUtils.updateDigest(messageDigest, name);
        digest = messageDigest.digest();
        digestAsHexString = Hex.encodeHexString(digest);
    }

    public String getName() {
        return name;
    }

    public List<FileChunkReference> getChunks() {
        //todo return a copy of the list instead? (avoid reference leak)
        return chunks;
    }

    public boolean isLeaf() {
        return true;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
