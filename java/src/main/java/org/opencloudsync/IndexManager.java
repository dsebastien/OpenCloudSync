package org.opencloudsync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 *
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
}
