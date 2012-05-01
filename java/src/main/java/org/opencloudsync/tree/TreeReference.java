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

import org.apache.commons.lang3.Validate;
import org.opencloudsync.DigestHolder;

/**
 * Date: 20/01/12
 * Time: 17:22
 */
public class TreeReference implements DigestHolder{
    private final FolderReference rootNode;
    /**
     * The digest of the file (combination of the digests of all chunks of the file.
     */
    private byte[] digest;
    /**
     * String version of the file digest.
     */
    private String digestAsHexString;

    public TreeReference(final FolderReference rootNode) { // todo accept a list of folderreferences: roots -> multiple folders
        Validate.notNull(rootNode, "The root node cannot be null!");
        this.rootNode = rootNode;

        //todo with multiple folders, calculate the hash of the tree as the combination of the hashes of all tree roots
        digest = rootNode.getDigest();
        digestAsHexString = rootNode.getDigestAsHexString();
    }

    public FolderReference getRootNode(){
        return rootNode;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
