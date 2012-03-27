package org.opencloudsync;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReaderImpl implements FileReader {
    //todo take from config file?
    private int chunkSize = 512;
    
    @Override
    public List<FileChunk> readFile(final File f) throws FileNotFoundException {
        if(f == null || !f.exists() || !f.isFile() || !f.canRead()){
            throw new IllegalArgumentException("The file must be provided, exist, be a file not a directory and be readable");
        }

        // todo optimize
        List<FileChunk> fileChunkList = new ArrayList<FileChunk>();
        FileChunk fileChunk = null;
        byte[] fileChunkBytes;
        
        byte[] buffer = new byte[chunkSize];

        FileInputStream fileInputSteam = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fileInputSteam, chunkSize);

        try {
            while(bis.available() > 0){
                int readBytes = bis.read(buffer);

                if(readBytes > 0){
                    fileChunkBytes = new byte[readBytes];
                    for(int i = 0; i < readBytes; i++){
                        fileChunkBytes[i] = buffer[i];
                    }
                    fileChunk = new FileChunk(fileChunkBytes);
                    fileChunkList.add(fileChunk);
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

        return fileChunkList;
    }
}
