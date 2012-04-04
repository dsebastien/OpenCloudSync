package org.opencloudsync;

/**
 * OpenCloudSync configuration.
 */
public class Configuration {
    private final String folderToWatch;
    private final String version;

    public Configuration(final String folderToWatch, final String version){
        //todo check args
        this.folderToWatch = folderToWatch;
        this.version = version;
    }
    
    public String getFolderToWatch(){
        return folderToWatch;
    }
    
    public String getVersion(){
        return version;
    }
}
