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


        // 0 check if enough space in the repo location to store the chunk

        // 1 construct the path to where the chunk should be located
        // --> simple decomposition of the hash
        
        File fileChunkPath = getPathFor(fileChunk);
        
        
        // 2 check if already exists
        //try to save
        //todo implement: store the chunk in the repo using the provided metadata.. / return status?
    }
    
    public File getPathFor(final FileChunk fileChunk){
        File retVal = null;
        
        StringBuilder path = new StringBuilder(repositoryFolder + File.separator);
        path.append(fileChunk.getDigestAsHexString().substring(0,1));
        System.out.println(path);
        fileChunk.getDigestAsHexString();
        System.out.println(fileChunk.getDigestAsHexString());
        return null;
    }

    //todo add method to get info about the repo (free space etc)?
}
