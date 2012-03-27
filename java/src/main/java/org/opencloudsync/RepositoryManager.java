package org.opencloudsync;

import java.io.File;

public class RepositoryManager {
    private final String repositoryLocation;

    public RepositoryManager(final String repositoryLocation) {
        //todo check argument
        this.repositoryLocation = repositoryLocation;
        initializeRepository();
    }

    private RepositoryManager initializeRepository(){
        //todo implement: check if the folder already exist or not, etc

        return this;
    }

    public void storeFild(final File file){
        //todo needed?
    }

    public void storeChunk(final FileChunk fileChunk){
        //todo check arg
        //todo implement: store the chunk in the repo using the provided metadata.. / return status?
    }

    //todo add method to get info about the rpeo (free space etc)?
}
