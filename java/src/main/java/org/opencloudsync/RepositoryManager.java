package org.opencloudsync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RepositoryManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryManager.class);

    private final File repositoryFolder;

    public RepositoryManager(final File repositoryFolder) {
        //todo check argument (not null)
        this.repositoryFolder = repositoryFolder;
        initializeRepository();
    }

    private void initializeRepository(){
        //todo implement: check if the folder already exist or not, if not, try to create it, etc
    }

    public void saveChunk(final FileChunk fileChunk){
        //todo check arg


        // todo check if enough space in the repo to store the chunk

        // 1 construct the path to where the chunk should be located
        // --> simple decomposition of the hash
        
        File fileChunkPath = getPathFor(fileChunk);
        
        // 2 check if already exists
        //try to save
        //todo implement: store the chunk in the repo using the provided metadata.. / return status?
    }

    /**
     * Determines where a given {@link FileChunk} should be stored in the repository.
     * @param fileChunk
     * @return the location where the {@link FileChunk} should be stored in the repository.
     */
    public File getPathFor(final FileChunk fileChunk){
        final String digestAsHexString = fileChunk.getDigestAsHexString();
        
        // todo review: okay like that or store it like git?
        StringBuilder path = new StringBuilder(repositoryFolder + File.separator)
            .append(digestAsHexString.substring(0,4))
            .append(File.separator)
            .append(digestAsHexString.substring(4, 8))
            .append(File.separator)
            .append(digestAsHexString.substring(8,12))
            .append(File.separator)
            .append(digestAsHexString.substring(12,16))
            .append(File.separator)
            .append(digestAsHexString.substring(16,20))
            .append(File.separator)
            .append(digestAsHexString.substring(20,24));
        LOGGER.debug("Digest: "+digestAsHexString);
        LOGGER.debug("Path: "+path);
        return new File(path.toString());
    }

    //todo add method to get info about the repo (free space etc)?
}
