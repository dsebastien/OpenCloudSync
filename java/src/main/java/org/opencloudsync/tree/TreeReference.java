package org.opencloudsync.tree;

/**
 * Date: 20/01/12
 * Time: 17:22
 */
public class TreeReference {
    private final Node rootNode;

    // todo add a tree hash? calculated based on?

    public TreeReference(final Node rootNode) {
        this.rootNode = rootNode;
    }

    public Node getRootNode(){
        return rootNode;
    }
}
