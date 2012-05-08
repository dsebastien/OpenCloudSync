/**
 * OpenCloudSync: Open source cloud synchronization solution; an extensible software that allows you to synchronize your data with different storage systems.
 *
 *     Copyright (C) 2012 Sebastien Dubois <seb__at__dsebastien.net>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudsync.tree;

import java.util.List;

/**
 * File nodes can contain children (e.g., folder or file contents).
 * T the type of the contained children.
 * Date: 7/05/12
 * Time: 18:13
 */
public interface FileNode<T> extends Node {
    /**
     * Get the name of the {@link FileNode}
     * @return the name of the {@link FileNode}
     */
    String getName();

    /**
     * Get the children of this node.
     * @return the children of this node.
     */
    List<T> getChildren();
}
