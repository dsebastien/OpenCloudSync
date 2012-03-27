package org.opencloudsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface FileReader{
    int ChunkSize = 512;

    List<FileChunk> readFile(final File f) throws FileNotFoundException;

}
