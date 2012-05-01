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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.opencloudsync.tree.TreeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

/**
 * DAO for the index data.
 */
//todo rename to IndexService
public class IndexManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexManager.class);

    private JdbcTemplate jdbcTemplate;

    //todo avoid dependency on configuration class?
    private Configuration configuration;

    public IndexManager() {
        LOGGER.trace("Index Manager initialization");
    }

    @Required
    public void setDataSource(final DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Required
    public void setConfiguration(final Configuration configuration){
        this.configuration = configuration;
    }

    /**
     * Update the index with the given tree
     * @param treeReference the tree to update the index with
     */
    public void update(final TreeReference treeReference){
        Validate.notNull(treeReference, "The tree reference cannot be null!");

        int latestTreeId = getLatestTreeId();

        if(latestTreeId != -1){
            byte[] latestTreeDigest = getDigestForTree(latestTreeId);
            LOGGER.trace("Digest for latest tree: "+Hex.encodeHexString(latestTreeDigest));
            LOGGER.trace("Digest for current tree: "+treeReference.getDigestAsHexString());

            if(Arrays.equals(latestTreeDigest, treeReference.getDigest())){
                LOGGER.trace("The latest and current tree have the same digest. Updating the last update time.");
                //set the last update time
                //setLastUpdateTime(latestTreeId);
            }else{
                LOGGER.trace("The latest and current tree have different digests. Saving the new tree");
                //todo save the tree
            }
        }else{
            LOGGER.trace("No tree in the index yet, saving it");
            save(treeReference);
        }
    }

    /**
     * Saves the given tree.
     * @param treeReference the given tree.
     */
    private void save(final TreeReference treeReference){
        Validate.notNull(treeReference, "The tree reference cannot be null!");

        jdbcTemplate.update("INSERT INTO "+configuration.getDbTableTree()+" (digest, creation_time) VALUES(?,?)", new Object[] { treeReference.getDigest(), new Timestamp(new Date().getTime())}, new int[] { Types.BINARY, Types.TIMESTAMP});
        
        //treeReference.getRootNode()
        //--> folderReference: save
    }

    /**
     * Returns the latest tree id from the DB.
     * @return the latest tree id from the DB or -1 if no tree in the DB
     */
    public int getLatestTreeId(){
        int retVal = -1;

        int numberOfTrees = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM "+configuration.getDbTableTree());

        if(numberOfTrees > 0){
            retVal = jdbcTemplate.queryForInt("SELECT id_tree FROM "+configuration.getDbTableTree()+" ORDER BY last_update_time DESC LIMIT 1");
        }

        return retVal;
    }
    
    public byte[] getDigestForTree(int id){
        return jdbcTemplate.queryForObject("SELECT digest FROM "+configuration.getDbTableTree()+" WHERE id_tree = ?",new RowMapper<byte[]>() {
            @Override
            public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getBytes("digest");
            }
        },id);
    }

    //todo implement method to retrieve the tree with given id (complete tree)
}
