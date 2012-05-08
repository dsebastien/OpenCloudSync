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
package org.opencloudsync.dao;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.opencloudsync.Configuration;
import org.opencloudsync.tree.FileChunkReference;
import org.opencloudsync.tree.FileNode;
import org.opencloudsync.tree.FileReference;
import org.opencloudsync.tree.FolderReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.naming.OperationNotSupportedException;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.Arrays;

/**
 * {@linkplain DataAccessObject} for {@link FileNode} objects.
 */
@Repository
public class FileDAO implements DataAccessObject<FileNode>{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDAO.class);

    private JdbcTemplate jdbcTemplate;

    //todo avoid dependency on configuration class?
    private Configuration configuration;

    private FileChunkDAO fileChunkDAO;

    public FileDAO() {
    }

    @Autowired
    public void setDataSource(final DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setConfiguration(final Configuration configuration){
        this.configuration = configuration;
    }

    @Autowired
    public void setFileChunkDAO(final FileChunkDAO fileChunkDAO){
        this.fileChunkDAO = fileChunkDAO;
    }

    /**
     * Save or update the index with the given {@link FileNode}. If the {@link FileNode} already exists, then the id is returned. Otherwise, it is saved.
     * Also, everything linked to that {@link FileNode} will be saved (i.e., files/folders for {@link FolderReference} and {@link org.opencloudsync.tree.FileChunkReference} for files.
     * @param fileNode the {@link FileNode} to save or update
     */
    public int saveOrUpdate(final FileNode fileNode){
        int retVal = -1;
        //todo fix this ugly stuff :)
        if(fileNode instanceof FolderReference){
            retVal = saveOrUpdateFolder((FolderReference)fileNode);
        }else if(fileNode instanceof FileReference){
            retVal = saveOrUpdateFile((FileReference)fileNode);
        }

        return retVal;
    }

    /**
     * Save the given {@link FolderReference} in the db. Saving a {@link FolderReference} will also save or update all the children elements (folders, files and file chunks).
     * @param folderReference the {@link FolderReference} to save
     * @return the id of the row or -1 in all other cases.
     */
    public int saveOrUpdateFolder(final FolderReference folderReference){
        int retVal;

        Validate.notNull(folderReference, "The folder reference cannot be null!");
        // try to find a that has the same digest as the current tree root node
        // since the digest of a folder uniquely identifies it and all of its content
        LOGGER.trace("Checking if the following folder reference already exists in the index or if it needs to be saved: "+ folderReference.getDigest());
        int existingFolder = findIdByReference(folderReference);
        if(existingFolder != -1){
            LOGGER.trace("The given folder reference already exists in the index. Nothing to do. Id: "+existingFolder);
            retVal = existingFolder;
        }else{
            LOGGER.trace("The given folder reference doesn't exist yet in the index. Saving it.");
            //todo handle DataAccessException?
            jdbcTemplate.update("INSERT INTO "+configuration.getDbTableFile()+" (digest, name) VALUES(?,?)", new Object[] { folderReference.getDigest(), folderReference.getName() }, new int[] { Types.BINARY, Types.VARCHAR});
            retVal = findIdByReference(folderReference);
            LOGGER.trace("Folder reference saved successfully. Id: "+retVal);
            LOGGER.trace("Now saving the files contained in that folder.");
            for(FileNode child: folderReference.getChildren()){
                int childId = saveOrUpdate(child);
                createParentChildLinkFolder(retVal, childId);
            }
        }

        return retVal;
    }

    /**
     * Creates a link between the given folder and file node.
     * @param folderId the id of the folder (parent)
     * @param childId the id of the file node (child)
     */
    protected void createParentChildLinkFolder(int folderId, int childId){
        // todo check args
        jdbcTemplate.update("INSERT INTO "+configuration.getDbTableFileHasFiles()+" (id_file_parent, id_file_child) VALUES(?,?)", new Object[] { folderId, childId });
    }

    /**
     * Save the given {@link FileReference} in the db.
     * @param fileReference the {@link FileReference} to save
     * @return the id of the row or -1 in all other cases.
     */
    public int saveOrUpdateFile(final FileReference fileReference){
        Validate.notNull(fileReference, "The file reference cannot be null!");
        // find a file that has the same digest as the current file
        // since the digest of a file uniquely identifies it and all of its content
        LOGGER.trace("Checking if the following file reference already exists in the index or if it needs to be saved: "+ fileReference.getDigest());

        int retVal;

        int existingFileReferenceId = findIdByReference(fileReference);
        if(existingFileReferenceId != -1){
            LOGGER.trace("The given file reference already exists in the index. Nothing to do.");
            retVal = existingFileReferenceId;
        }else{
            LOGGER.trace("The given file reference doesn't exist yet in the index. Saving it.");
            //todo handle DataAccessException?
            jdbcTemplate.update("INSERT INTO "+configuration.getDbTableFile()+" (digest, name) VALUES(?,?)", new Object[] { fileReference.getDigest(), fileReference.getName() }, new int[] { Types.BINARY, Types.VARCHAR});
            retVal = findIdByReference(fileReference);

            LOGGER.trace("Now saving the chunks corresponding to that file.");
            for(FileChunkReference child: fileReference.getChildren()){
                int childId = fileChunkDAO.saveOrUpdate(child);
                createParentChildLinkFile(retVal, childId);
            }
        }

        return retVal;
    }

    /**
     * Creates a link between the given file and file chunk.
     * @param fileId the id of the file (parent)
     * @param childId the id of the file chunk (child)
     */
    protected void createParentChildLinkFile(int fileId, int childId){
        //todo check args
        jdbcTemplate.update("INSERT INTO "+configuration.getDbTableFileHasFileChunks()+" (id_file,id_filechunk) VALUES(?,?)", new Object[] { fileId, childId });
    }

    /**
     * Returns the id of the given {@link FileNode} in the DB if it exists (searches using the digest).
     * Only the digests are compared since that is what uniquely identifies nodes
     * @param fileNode the {@link FileNode} to search for (search using the digest)
     * @return the id of the {@link FileNode} if it exists or -1 otherwise
     */
    @Override
    public int findIdByReference(FileNode fileNode) {
        Validate.notNull(fileNode, "The node reference cannot be null!");

        int retVal = -1;
        try{
            retVal = jdbcTemplate.queryForInt("SELECT id_file FROM " + configuration.getDbTableFile() + " WHERE CAST(digest as VARCHAR) = CAST(? as VARCHAR)", new Object[]{fileNode.getDigest()}, new int[]{Types.BINARY});
        } catch (DataAccessException dae){
            LOGGER.trace("File node not found: "+dae);
        }

        return retVal;
    }

    @Override
    public byte[] getDigest(int id) {
        throw new NotImplementedException();  //todo implement
    }
}

