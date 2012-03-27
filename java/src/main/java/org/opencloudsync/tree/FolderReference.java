package org.opencloudsync.tree;

import org.opencloudsync.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 23/01/12
 * Time: 08:21
 */
public class FolderReference implements Node{
    private String name;

    //TODO !! concurrency/synchronization issues

    private final List<Node> nodes = new ArrayList<Node>();

    public FolderReference(final String name){
        // todo check argument
        this.name = name;
    }

    public FolderReference(final List<Node> nodes){
        //TODO check the provided list
        this.nodes.addAll(nodes);
    }

    @Override
    public boolean isLeaf() {
        return nodes.isEmpty();
    }

    public String getName() {
        return name;
    }

    /**
     * Get the list of nodes; this method does not leak the list reference
     * @return read-only copy of the list of nodes
     */
    public List<Node> getNodes() {
        final List<Node> nodes = new ArrayList<Node>();
        nodes.addAll(nodes);
        return nodes;
    }
    
    public FolderReference addNode(final Node node){
        this.nodes.add(node);
        return this;
    }

}
