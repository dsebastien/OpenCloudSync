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

import org.apache.commons.lang3.Validate;
import org.opencloudsync.Configuration;
import org.opencloudsync.tree.FileChunkReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.sql.Types;

/**
 * {@linkplain DataAccessObject} for {@link org.opencloudsync.tree.FileChunkReference} objects.
 */
@Repository
public class FileChunkDAO implements DataAccessObject<FileChunkReference>{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileChunkDAO.class);
    private JdbcTemplate jdbcTemplate;

    //todo avoid dependency on configuration class?
    private Configuration configuration;

    public FileChunkDAO() {
    }


    @Autowired
    public void setDataSource(final DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setConfiguration(final Configuration configuration){
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int saveOrUpdate(final FileChunkReference fileChunkReference){
        Validate.notNull(fileChunkReference, "The file chunk reference cannot be null!");
        // find a file chunk that has the same digest as the current file chunk
        // since the digest of a file chunk uniquely identifies it
        LOGGER.trace("Checking if the following file chunk reference already exists in the index or if it needs to be saved: "+ fileChunkReference.getDigestAsHexString());

        int retVal;

        int existingFileChunk = findIdByReference(fileChunkReference);
        if(existingFileChunk != -1){
            LOGGER.trace("The given file chunk reference already exists in the index. Nothing to do.");
            retVal = existingFileChunk;
        }else{
            LOGGER.trace("The given file chunk reference doesn't exist yet in the index. Saving it.");
            //todo handle DataAccessException?
            jdbcTemplate.update("INSERT INTO "+configuration.getDbTableFileChunk()+" (digest) VALUES(?)", new Object[] { fileChunkReference.getDigest() }, new int[] { Types.BINARY });
            retVal = findIdByReference(fileChunkReference);
            LOGGER.trace("File chunk saved successfully: "+fileChunkReference.getDigestAsHexString()+". Id: "+retVal);
        }

        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getDigest(int id) {
        throw new NotImplementedException(); //todo implement
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int findIdByReference(final FileChunkReference fileChunkReference){
        Validate.notNull(fileChunkReference, "The file chunk reference cannot be null!");

        int retVal = -1;
        try{
            retVal = jdbcTemplate.queryForInt("SELECT id_filechunk FROM " + configuration.getDbTableFileChunk() + " WHERE CAST(digest as VARCHAR) = CAST(? as VARCHAR)", new Object[]{fileChunkReference.getDigest()}, new int[]{Types.BINARY});
        } catch (DataAccessException dae){
            LOGGER.trace("File chunk not found: "+dae);
        }

        return retVal;
    }
}
