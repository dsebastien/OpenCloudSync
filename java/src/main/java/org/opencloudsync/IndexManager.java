package org.opencloudsync;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;

/**
 *
 */
//todo rename to IndexService
public class IndexManager implements FileAlterationListener {
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


    //todo implement/test
    @Override
    public void onStart(FileAlterationObserver observer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDirectoryCreate(File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDirectoryChange(File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDirectoryDelete(File directory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFileCreate(File file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFileChange(File file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFileDelete(File file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
