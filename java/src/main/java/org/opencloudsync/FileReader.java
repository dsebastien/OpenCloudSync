/**
 * OpenCloudSync: Open source cloud synchronization solution; an extensible software that allows you to synchronize your data with different storage systems.
 *
 *     Copyright (C) 2012 Sebastien Dubois <seb__at__dsebastien.net>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
