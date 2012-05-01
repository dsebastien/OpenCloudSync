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
 * Immutable reference to a file chunk in the repository.
 * Date: 20/01/12
 * Time: 17:24
 */
public class FileChunkReference implements Node, DigestHolder {
    /**
     * Digest of the file chunk.
     */
    private final byte[] digest;
    /**
     * String version of the digest.
     */
    private final String digestAsHexString;

    /**
     * Construct a {@link FileChunkReference}. Only accepts the raw data to avoid keeping references to the objects and thus preventing memory from being freed.
     * @param digest the chunk's digest
     * @param digestAsHexString the chunk's digest as hex String
     */
    public FileChunkReference(final byte[] digest, final String digestAsHexString){
        Validate.notNull(digest, "The digest cannot be null!");
        Validate.notEmpty(digestAsHexString, "The digest as hex string cannot be null or empty!");
        this.digest = digest;
        this.digestAsHexString = digestAsHexString;
    }

    public byte[] getDigest(){
        return digest;
    }

    public String getDigestAsHexString(){
        return digestAsHexString;
    }

    public boolean isLeaf() {
        return true;
    }
}
