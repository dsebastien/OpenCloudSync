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

import org.apache.commons.codec.binary.Hex;
import org.opencloudsync.DigestHolder;
import org.opencloudsync.utils.DigestUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 23/01/12
 * Time: 08:21
 */
public class FolderReference implements Node, DigestHolder{
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
    private final List<Node> nodes = new ArrayList<Node>();

    /**
     * Default constructor.
     * @param name name of the folder
     * @param nodes list of nodes in the folder
     */
    public FolderReference(final String name, final List<Node> nodes){
        // todo check arguments (not null, not empty name, etc)
        this.name = name;
        this.nodes.addAll(nodes);

        // The digest of a folder is the combination of all the hashes of the nodes below it & the hash of the folder name
        // todo maybe better not to include the folder name in the digest calculation?
        // it'd mean that the hash of a folder is only dependent on its contents thus even if the name of the folder changes
        // it'll still be possible to recognize that it's actually the same content
        final MessageDigest messageDigest = DigestUtils.getShaDigest();
        for(Node node: nodes){
            messageDigest.update(node.getDigest());
            DigestUtils.updateDigest(messageDigest, node.getDigest());
        }

        // The folder name is not included in the digest in order to be able to recognize a folder based on its contents even if it was renamed
        //DigestUtils.updateDigest(messageDigest, name);
        digest = messageDigest.digest();
        digestAsHexString = Hex.encodeHexString(digest);
    }

    public boolean isLeaf() {
        return nodes.isEmpty();
    }

    /**
     * Get the name of the folder.
     * @return the name of the folder.
     */
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

    public byte[] getDigest() {
        return digest;
    }

    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
