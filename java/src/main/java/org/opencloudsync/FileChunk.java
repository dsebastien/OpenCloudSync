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
