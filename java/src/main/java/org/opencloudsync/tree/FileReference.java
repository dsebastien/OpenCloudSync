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
 * Immutable reference to a file whose chunks are all available in the repository.
 * Date: 20/01/12
 * Time: 17:23
 */
public class FileReference implements Node, DigestHolder {
    /**
     * The name of the file.
     */
    private final String name;
    /**
     * The digest of the file (combination of the digests of all chunks of the file.
     */
    private byte[] digest;
    /**
     * String version of the file digest.
     */
    private String digestAsHexString;

    //todo add other metadata (?)
    private final List<FileChunkReference> chunks = new ArrayList<FileChunkReference>();
    
    public FileReference(final String name, final List<FileChunkReference> chunks){
        Validate.notEmpty(name, "The name cannot be null or empty!");
        Validate.notNull(chunks, "The chunks cannot be null!");
        this.name = name;
        this.chunks.addAll(chunks); // we just add the given chunks

        // The hash of a file is the combination of the digests of all chunks it is made of
        final MessageDigest messageDigest = DigestUtils.getShaDigest();
        for(Node node: chunks){
            DigestUtils.updateDigest(messageDigest, node.getDigest());
        }
        //The file name is not part of the digest so that a file remains recognizable even if it was renamed
        //DigestUtils.updateDigest(messageDigest, name);
        digest = messageDigest.digest();
        digestAsHexString = Hex.encodeHexString(digest);
    }

    public String getName() {
        return name;
    }

    public List<FileChunkReference> getChunks() {
        //todo return a copy of the list instead? (avoid reference leak)
        return chunks;
    }

    public boolean isLeaf() {
        return true;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getDigestAsHexString() {
        return digestAsHexString;
    }
}
