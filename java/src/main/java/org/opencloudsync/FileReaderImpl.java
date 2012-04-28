package org.opencloudsync;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opencloudsync.tree.FileChunkReference;
import org.opencloudsync.tree.FileReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @see FileReader
 */
public class FileReaderImpl implements FileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileReaderImpl.class);
    
    private int maxFileChunkSize;
    private RepositoryManager repositoryManager;

    public FileReaderImpl(){
    }

    @Required
    public void setRepositoryManager(final RepositoryManager repositoryManager){
        // todo check arg
        this.repositoryManager = repositoryManager;
    }

    @Required
    public void setMaxFileChunkSize(int maxFileChunkSize){
        //todo check value (> O)
        this.maxFileChunkSize = maxFileChunkSize;
    }

    /**
     * @see FileReader
     */
    public FileReference readFile(final File file) throws FileNotFoundException {
        if(file == null || !file.exists() || !file.isFile() || !file.canRead()){
            throw new IllegalArgumentException("The file must be provided, exist, be a file not a directory and be readable");
        }

        //todo ensure that we use a collection where the order of retrieval is guaranteed!
        List<FileChunkReference> fileChunkReferences = new ArrayList<FileChunkReference>();

        FileChunk fileChunk;
        byte[] fileChunkBytes;

        //todo evaluate chunk size based on file size
        byte[] buffer = new byte[maxFileChunkSize];

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = FileUtils.openInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fileInputStream, maxFileChunkSize);

            while(bis.available() > 0){
                int readBytes = bis.read(buffer);

                if(readBytes > 0){
                    fileChunkBytes = new byte[readBytes];
                    for(int i = 0; i < readBytes; i++){
                        fileChunkBytes[i] = buffer[i];
                    }

                    fileChunk = new FileChunk(fileChunkBytes);

                    //todo might not be the best way to do this
                    // might be better to split this method/class in two to avoid mixing responsibilities
                    // might also be better to use an interface instead of an actual implementation
                    repositoryManager.saveChunk(fileChunk);

                    fileChunkReferences.add(new FileChunkReference(fileChunk));

                    fileChunk = null;
                    fileChunkBytes = null;
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Error while trying to read the file!",e);
        } finally{
            IOUtils.closeQuietly(fileInputStream);
        }
        return new FileReference(file.getName(), fileChunkReferences);
    }
}
