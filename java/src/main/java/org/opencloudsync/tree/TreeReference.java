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
import org.apache.commons.lang3.Validate;
import org.opencloudsync.DigestHolder;
import org.opencloudsync.utils.DigestUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 20/01/12
 * Time: 17:22
 */
public class TreeReference implements DigestHolder{
    private final List<FolderReference> rootNodes = new ArrayList<>();
    /**
     * The digest of the file (combination of the digests of all chunks of the file.
     */
    private byte[] digest;
    /**
     * String version of the file digest.
     */
    private String digestAsHexString;

    public TreeReference(final List<FolderReference> rootNodes) {
        Validate.notEmpty(rootNodes, "The root nodes list cannot be null or empty!");
        this.rootNodes.addAll(rootNodes);

        // The digest of a tree is the combination of all the digests of the nodes below it
        final MessageDigest messageDigest = DigestUtils.getShaDigest();
        for(Node node: rootNodes){
            messageDigest.update(node.getDigest());
            DigestUtils.updateDigest(messageDigest, node.getDigest());
        }
        digest = messageDigest.digest();
        digestAsHexString = Hex.encodeHexString(digest);
    }

    public List<FolderReference> getRootNodes(){
        return rootNodes; // todo return a copy instead?
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
