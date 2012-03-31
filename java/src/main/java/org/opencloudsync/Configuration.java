package org.opencloudsync;

/**
 *
 */
public class Configuration {
    private final String folderToWatch;

    public Configuration(final String folderToWatch){
        //todo check args
        this.folderToWatch = folderToWatch;
    }
    
    public String getFolderToWatch(){
        return folderToWatch;
    }
}
