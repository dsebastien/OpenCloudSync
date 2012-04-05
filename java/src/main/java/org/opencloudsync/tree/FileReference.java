package org.opencloudsync.tree;

import org.apache.commons.codec.binary.Hex;
import org.opencloudsync.DigestHolder;
import org.opencloudsync.utils.DigestUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 20/01/12
 * Time: 17:23
 */
public class FileReference implements Node, DigestHolder {
    private final String name;
    private byte[] digest;
    private String digestAsHexString;

    //todo add other metadata (?)
    private final List<FileChunkReference> chunks = new ArrayList<FileChunkReference>();
    
    public FileReference(final String name, final List<FileChunkReference> chunks){
        //todo check args
        this.name = name;
        this.chunks.addAll(chunks); // we just add the given chunks

        // The digest of a file is the combination of all file chunks hashes & the hash of the file name
        // todo maybe better not to include the file name in the digest calculation?
        // it'd mean that the hash of a file is only dependent on its contents thus even if the name of the file changes
        // it'll still be possible to recognize that it's actually the same content
        final MessageDigest messageDigest = DigestUtils.getShaDigest();
        for(Node node: chunks){
            DigestUtils.updateDigest(messageDigest, node.getDigest());
        }
        DigestUtils.updateDigest(messageDigest, name);
        digest = messageDigest.digest();
        digestAsHexString = Hex.encodeHexString(digest);
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
        return digest;
    }

    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
