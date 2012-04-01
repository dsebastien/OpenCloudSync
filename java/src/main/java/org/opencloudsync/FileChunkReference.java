package org.opencloudsync;

/**
 * Date: 20/01/12
 * Time: 17:24
 */
public class FileChunkReference {
    private final String reference;
    
    public FileChunkReference(final String reference){
        //todo check argument
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
