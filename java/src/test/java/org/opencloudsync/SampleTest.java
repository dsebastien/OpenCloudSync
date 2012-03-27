package org.opencloudsync;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

public class SampleTest {
    public static void main(String... args){
        File inputFile = new File("./sample-data/sample.txt");
        File outputFile = new File("./sample-data/sample-output.txt");

        FileReader fileReader = new FileReaderImpl();

        try {
            List<FileChunk> fileChunks = fileReader.readFile(inputFile);
            System.out.println("chunks: "+fileChunks.size());
            System.out.println("first chunk bytes: " + fileChunks.get(0).getBytes().length);

            //todo
            //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(yourFile));
            //bos.write(fileBytes);
            //bos.flush();
            //bos.close();

            OutputStream outputStream = new FileOutputStream(outputFile);
            
            for(FileChunk chunk: fileChunks){
                try {
                    IOUtils.write(chunk.getBytes(), outputStream);
                } catch (IOException e) {
                    //todo handle
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
