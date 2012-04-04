package org.opencloudsync;

import org.opencloudsync.tree.FileReference;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Entity capable of reading a file on the system, storing it in the local repository and returning a reference to the stored file.
 */
public interface FileReader{

    /**
     * Reads a file on the system and stores it in the local repository.
     * @param file the file to read
     * @return a reference to the object stored in the local repository
     * @throws FileNotFoundException
     * @throws  IllegalArgumentException if the given file is null or is not a file
     */
    FileReference readFile(final File file) throws FileNotFoundException;
}
