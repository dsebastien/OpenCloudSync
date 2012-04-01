package org.opencloudsync;

import java.util.List;

public class FileChunk {
    private final byte[] bytes;

    public FileChunk(final byte[] bytes) {
        this.bytes = bytes;
        //tàdà,calculate checksum
    }

    public byte[] getBytes() {
        return bytes;
    }
    
    public String getChecksum(){
        //todo implement
        return null;
    }
}
