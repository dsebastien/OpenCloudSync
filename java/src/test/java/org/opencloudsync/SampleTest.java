package org.opencloudsync;

import org.apache.commons.io.IOUtils;
import org.opencloudsync.utils.DigestUtils;

import java.io.*;
import java.security.MessageDigest;
import java.util.List;

public class SampleTest {
    public static void main(String... args){

        String a1 = "9f932ed8105524e91a78ec9e4d4707316ed8f08d";
        String a2 = "fe2e43cd254deabc4d45s67cdabd34ds32fs23s2";

        MessageDigest m1 = DigestUtils.getShaDigest();
        MessageDigest m2 = DigestUtils.getShaDigest();
        
        DigestUtils.updateDigest(m1,a1);
        DigestUtils.updateDigest(m1,a2);
        
        DigestUtils.updateDigest(m2,a2);
        DigestUtils.updateDigest(m2,a1);

        String r1 = DigestUtils.shaHex(m1.digest());
        String r2 = DigestUtils.shaHex(m2.digest());

        System.out.println("r1: "+r1);
        System.out.println("r2: "+r2);
        
        
        /*
        File inputFile = new File("./sample-data/sample.txt");
        File outputFile = new File("./sample-data/sample-output.txt");


        FileReader fileReader = new FileReaderImpl();

        try {
            List<FileChunk> fileChunks = fileReader.readFile(inputFile);
            System.out.println("chunks: "+fileChunks.size());
            System.out.println("first chunk bytes: " + fileChunks.get(0).getBytes().length);

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            
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
        */
    }
}
