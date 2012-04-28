package org.opencloudsync;

import org.apache.commons.lang3.Validate;
import org.opencloudsync.tree.TreeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import javax.sql.DataSource;

/**
 * DAO for the index data.
 */
//todo rename to IndexService
public class IndexManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexManager.class);

    // todo replace by JPA?
    private JdbcTemplate jdbcTemplate;

    public IndexManager() {
        LOGGER.trace("Index Manager initialization");
    }

    @Required
    public void setDataSource(final DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Index the given tree.
     * @param treeReference the tree to index
     */
    public void save(final TreeReference treeReference){
        Validate.notNull(treeReference, "The tree reference cannot be null!");

        //todo get latest tree reference from db
        //compare hashes
        //if no previous version or different hash: save
        //otherwise, nothing to do
        //todo implement
    }

    /**
     * Returns the latest tree from the index or null if none exists.
     * @return the latest tree from the index or null if none exists
     */
    public TreeReference getLatestTree(){
        TreeReference retVal = null;
        //todo implement

        return retVal;
    }
}
