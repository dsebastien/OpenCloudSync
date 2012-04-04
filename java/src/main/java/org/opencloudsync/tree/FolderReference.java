package org.opencloudsync.tree;

import org.opencloudsync.tree.Node;

import java.io.File;
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

    public FolderReference(final File file, final List<Node> nodes){
        // todo check arguments (not null, not empty name, etc)
        this.name = file.getName();
        this.nodes.addAll(nodes);

        //todo internally calculate the hash of the folderreference by combining all underlying hashes
        //+ implement accessor for the resulting value
    }

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
    public List<Node> getChildNodes() {
        final List<Node> nodes = new ArrayList<Node>();
        nodes.addAll(nodes);
        return nodes;
    }
    
    public FolderReference addChildNode(final Node node){
        this.nodes.add(node);
        return this;
    }

    public FolderReference removeChildNodes(){
        this.nodes.clear();
        return this;
    }

}
