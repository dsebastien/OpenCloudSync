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
import org.opencloudsync.tree.FolderReference;
import org.opencloudsync.tree.TreeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

/**
 * {@linkplain DataAccessObject} for {@link TreeReference} objects.
 */
@Repository
public class TreeDAO implements DataAccessObject<TreeReference> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeDAO.class);

    private JdbcTemplate jdbcTemplate;

    //todo avoid dependency on configuration class?
    private Configuration configuration;
    
    private FileDAO fileDAO;

    public TreeDAO() {
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
    public void setFileDAO(final FileDAO fileDAO){
        this.fileDAO = fileDAO;
    }

    /**
     * Get the id of the latest tree in the database.
     * @return the id of the latest tree in the database or -1 if there's none
     */
    public int getLatestId(){
        int retVal = -1;

        int numberOfTrees = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM "+configuration.getDbTableTree());

        if(numberOfTrees > 0){
            retVal = jdbcTemplate.queryForInt("SELECT id_tree FROM "+configuration.getDbTableTree()+" ORDER BY last_update_time DESC LIMIT 1");
        }

        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int findIdByReference(final TreeReference treeReference) {
        Validate.notNull(treeReference, "The tree reference cannot be null!");
        int retVal = -1;
        try{
            retVal = jdbcTemplate.queryForInt("SELECT id_tree FROM " + configuration.getDbTableTree() + " WHERE CAST(digest as VARCHAR) = CAST(? as VARCHAR)", new Object[]{treeReference.getDigest()}, new int[]{Types.BINARY});
        } catch (DataAccessException dae){
            LOGGER.trace("Tree not found: "+dae);
        }

        return retVal;
    }

    /**
     * Save or update the index with the given tree. If the tree already exists, then the timestamp is updated. If not, it is saved.
     * Also, everything linked to that tree will be saved (i.e., folders, files and file chunks).
     * @param treeReference the tree to save or update
     */
    @Override
    public int saveOrUpdate(final TreeReference treeReference){
        Validate.notNull(treeReference, "The tree reference cannot be null!");

        int latestTreeId = getLatestId();
        boolean saveRequired = false;
        if(latestTreeId != -1){
            byte[] latestTreeDigest = getDigest(latestTreeId);
            LOGGER.trace("Digest for latest tree: "+ Hex.encodeHexString(latestTreeDigest));
            LOGGER.trace("Digest for current tree: "+treeReference.getDigestAsHexString());

            if(Arrays.equals(latestTreeDigest, treeReference.getDigest())){
                LOGGER.trace("The latest and current tree have the same digest. Updating the last update time.");
                setLastUpdateTime(latestTreeId);
            }else{
                LOGGER.trace("The latest and current tree have different digests. Saving the new tree.");
                saveRequired = true;
            }
        }else{
            LOGGER.trace("No tree in the index yet, saving it");
            saveRequired = true;
        }

        int retVal;
        if(saveRequired){
            retVal = save(treeReference);
        }else{
            retVal = latestTreeId;
        }
        return retVal;
    }

    /**
     * Saves the given {@link TreeReference}. Recursively, it'll also ensure that everything linked to that tree is saved in the index (i.e., folders, files and file chunks).
     * Assumes that no tree with the same digest already exists in the database. If a tree with the same digest already exists, an exception will be thrown
     * @param treeReference the {@link TreeReference} to save
     */
    protected int save(final TreeReference treeReference){
        Validate.notNull(treeReference, "The tree reference cannot be null!");

        //todo handle errors?
        jdbcTemplate.update("INSERT INTO "+configuration.getDbTableTree()+" (digest, last_update_time) VALUES(?,?)", new Object[] { treeReference.getDigest(), new Timestamp(new Date().getTime())}, new int[] { Types.BINARY, Types.TIMESTAMP});
        int retVal = findIdByReference(treeReference);

        for(FolderReference rootNode: treeReference.getRootNodes()){
            int folderId = fileDAO.saveOrUpdate(rootNode);
            // todo check folderId! If -1?
            createParentChildLink(retVal, folderId);
        }

        return retVal;
    }

    /**
     * Creates a link between the given tree and folder.
     * @param treeId the id of the tree (parent)
     * @param folderId the id of the folder (child)
     */
    protected void createParentChildLink(int treeId, int folderId){
        // todo handle errors?
        jdbcTemplate.update("INSERT INTO "+configuration.getDbTableTreeHasFiles()+" (id_tree, id_file) VALUES(?, ?)", new Object[]{treeId, folderId });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getDigest(int id){
        return jdbcTemplate.queryForObject("SELECT digest FROM "+configuration.getDbTableTree()+" WHERE id_tree = ?",new RowMapper<byte[]>() {
            @Override
            public byte[] mapRow(final ResultSet rs, int rowNum) throws SQLException { // todo handle exception?
                return rs.getBytes("digest");
            }
        },id);
        //todo handle case where id not found!
    }

    /**
     * Updates the last update time for the given tree.
     * @param treeId the id of the tree to update
     */
    protected void setLastUpdateTime(int treeId){
        //todo check for errors
        jdbcTemplate.update("UPDATE " + configuration.getDbTableTree() + " SET last_update_time = ? WHERE id_tree = ?)", new Object[]{new Timestamp(new Date().getTime()), treeId}, new int[]{Types.TIMESTAMP, Types.INTEGER});
    }
}
