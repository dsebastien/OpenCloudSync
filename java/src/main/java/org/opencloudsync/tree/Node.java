package org.opencloudsync.tree;

import org.opencloudsync.DigestHolder;

/**
 * Marker interface for tree nodes
 * Date: 23/01/12
 * Time: 08:21
 */
public interface Node extends DigestHolder{ //todo feels like a little hack to extend DigestHolder
    boolean isLeaf();
}
