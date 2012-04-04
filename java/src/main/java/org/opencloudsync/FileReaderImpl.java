package org.opencloudsync;

import org.apache.commons.io.IOUtils;
import org.opencloudsync.tree.FileChunkReference;
import org.opencloudsync.tree.FileReference;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @see FileReader
 */
public class FileReaderImpl implements FileReader {
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

        FileReference retVal = null;
        //todo ensure that we use a collection where the order of retrieval is guaranteed!
        List<FileChunkReference> fileChunkReferences = new ArrayList<FileChunkReference>();

        FileChunk fileChunk = null;
        byte[] fileChunkBytes;
        
        byte[] buffer = new byte[maxFileChunkSize];

        FileInputStream fileInputSteam = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fileInputSteam, maxFileChunkSize);

        try {
            while(bis.available() > 0){
                int readBytes = bis.read(buffer);

                if(readBytes > 0){
                    fileChunkBytes = new byte[readBytes];
                    for(int i = 0; i < readBytes; i++){
                        fileChunkBytes[i] = buffer[i];
                    }

                    //todo might be optimized by avoiding creating a filechunk object
                    fileChunk = new FileChunk(fileChunkBytes);

                    //todo might not be the best way to do this
                    // might be better to split this method in two to avoid mixing responsibilities
                    repositoryManager.saveChunk(fileChunk);

                    fileChunkReferences.add(new FileChunkReference(fileChunk));

                    fileChunk = null;
                    fileChunkBytes = null;
                }
            }
        } catch (IOException e) {
            //todo handle
            e.printStackTrace();
        } finally{
            IOUtils.closeQuietly(fileInputSteam);
        }

        return new FileReference(file.getName(), fileChunkReferences);
    }
}
