package org.opencloudsync.tree;

/**
 * Date: 20/01/12
 * Time: 17:22
 */
public class TreeReference {
    private final Node rootNode;

    public TreeReference(final Node rootNode) {
        this.rootNode = rootNode;
    }

    public Node getRootNode(){
        return rootNode;
    }
}
