package org.opencloudsync;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.FileSystem;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    /**
     * Writes the given {@link FileChunk} to the repository. Does nothing if the {@link FileChunk} already exists in the repository.
     * The hex string version of the {@link FileChunk} digest is split in multiple parts and each part is used as folder name. The final part is used as file name.
     * This ensures that not too many files are created in any repository folder.
     * @param fileChunk the {@link FileChunk} to save in the repository
     * @throws IOException if the free disk space is lower tha the data held in the {@link FileChunk} or if an error occurs while writing the {@link FileChunk} to disk
     * @throws IllegalArgumentException if the argument is null
     */
    public void saveChunk(final FileChunk fileChunk) throws IOException{
        Validate.notNull(fileChunk, "The file chunk cannot be null!");

        /*
            Seems broken on mac os x
            //todo introduce jira in apache commons io

        //todo add timeout to the call?
        final long freeSpaceKbInRepository = FileSystemUtils.freeSpaceKb(this.repositoryFolder.getAbsolutePath());
        final long requiredFreeSpaceKb = fileChunk.getBytes().length / FileUtils.ONE_KB;
        if(freeSpaceKbInRepository < requiredFreeSpaceKb){
            throw new IOException("Not enough disk space to store the given file chunk! Required: "+requiredFreeSpaceKb+"KB. Available space: "+freeSpaceKbInRepository);
        }
        */
        
        File fileChunkPath = getPathFor(fileChunk);
        
        if(!fileChunkPath.exists()){
            LOGGER.trace("The '"+fileChunk.getDigestAsHexString()+"' file chunk does not exist yet in the repository; saving it.");
            try {
                //todo check if exists & can write to
                FileOutputStream fileOutputStream = FileUtils.openOutputStream(fileChunkPath);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream); // todo specify the buffer size
                bufferedOutputStream.write(fileChunk.getBytes());
                bufferedOutputStream.flush();
            } catch (IOException e) {
                //todo handle
                LOGGER.warn("Problem while trying to write the following file chunk to disk: "+fileChunk.getDigestAsHexString(),e);
                throw e;
            }
        }else{
            //todo slf4j arguments instead...
            LOGGER.trace("The '"+fileChunk.getDigestAsHexString()+"' file chunk already exists in the repository, nothing to do.");
        }
    }

    /**
     * Determines where a given {@link FileChunk} should be stored in the repository.
     * @param fileChunk
     * @return the location where the {@link FileChunk} should be stored in the repository.
     */
    public File getPathFor(final FileChunk fileChunk){
        final String digestAsHexString = fileChunk.getDigestAsHexString();
        final StringBuilder parentPath = new StringBuilder(repositoryFolder + File.separator)
            .append(digestAsHexString.substring(0,8))
            .append(File.separator)
            .append(digestAsHexString.substring(8, 16))
            .append(File.separator)
            .append(digestAsHexString.substring(16,24))
            .append(File.separator)
            .append(digestAsHexString.substring(24,40));
       
        //todo parent path not seen as a folder???
        //todo add extension to the files?
        LOGGER.debug("Digest: " + digestAsHexString);
        LOGGER.debug("Path: " + parentPath.toString());
        return new File(parentPath.toString());
    }

    //todo add method to get info about the repo (free space etc)
}
