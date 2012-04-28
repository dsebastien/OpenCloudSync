package org.opencloudsync.tree;

import org.opencloudsync.DigestHolder;

/**
 * Date: 20/01/12
 * Time: 17:22
 */
public class TreeReference implements DigestHolder{
    private final Node rootNode;
    /**
     * The digest of the file (combination of the digests of all chunks of the file.
     */
    private byte[] digest;
    /**
     * String version of the file digest.
     */
    private String digestAsHexString;

    public TreeReference(final Node rootNode) {
        //todo check arg
        this.rootNode = rootNode;

        // todo usefulness of treereference if it's just a wrapper around the root node of the tree??
        digest = rootNode.getDigest();
        digestAsHexString = rootNode.getDigestAsHexString();
    }

    public Node getRootNode(){
        return rootNode;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
