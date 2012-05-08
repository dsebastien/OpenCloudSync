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

import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.opencloudsync.DigestHolder;
import org.opencloudsync.utils.DigestUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reference to a folder.
 * Date: 23/01/12
 * Time: 08:21
 */
public class FolderReference implements FileNode<FileNode>{
    /**
     * name of the folder.
     */
    private String name;
    /**
     * digest of the folder. The digest is the combination of the digests of all files and folders in this folder.
     */
    private byte[] digest;
    /**
     * string version of the digest.
     */
    private String digestAsHexString;

    /**
     * all nodes present in this folder.
     */
    private final List<FileNode> nodes = new ArrayList<>();

    /**
     * Default constructor.
     * @param name name of the folder
     * @param nodes list of nodes in the folder
     */
    public FolderReference(final String name, final List<FileNode> nodes){
        Validate.notEmpty(name, "The name cannot be null or empty!");
        Validate.notNull(nodes, "The list of nodes cannot be null!");

        this.name = name;
        this.nodes.addAll(nodes);

        // The digest of a folder is the combination of all the digests of the nodes below it combined with the hash of its name
        final MessageDigest messageDigest = DigestUtils.getShaDigest();
        for(Node node: nodes){
            messageDigest.update(node.getDigest());
            DigestUtils.updateDigest(messageDigest, node.getDigest());
        }
        //The folder name is part of the digest so that a difference with the name of a folder directly impacts the digest of its parent (if there is one) and of the whole tree
        DigestUtils.updateDigest(messageDigest, name);
        digest = messageDigest.digest();
        digestAsHexString = Hex.encodeHexString(digest);
    }

    /**
     * Get the name of the folder.
     * @return the name of the folder.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the list of children; this method does not leak the list reference.
     * @return a read-only copy of the children's list
     */
    @Override
    public List<FileNode> getChildren() {
        return Collections.unmodifiableList(nodes);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getDigest() {
        return digest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
