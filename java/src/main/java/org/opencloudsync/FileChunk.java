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
package org.opencloudsync;

import org.apache.commons.codec.binary.Hex;
import org.opencloudsync.utils.DigestUtils;

public class FileChunk implements DigestHolder {
    private final byte[] bytes;
    private final byte[] digest;
    private final String digestAsHexString;

    public FileChunk(final byte[] bytes) {
        this.bytes = bytes;

        digest = DigestUtils.sha(bytes);
        digestAsHexString = Hex.encodeHexString(digest);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getDigest(){
        return digest;
    }
    
    public String getDigestAsHexString(){
        return digestAsHexString;
    }
}
