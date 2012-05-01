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

import org.apache.commons.lang3.Validate;

/**
 * OpenCloudSync configuration.
 */
public class Configuration {
    private final String folderToWatch;
    private final String version;

    private final String dbSchemaName;

    public Configuration(final String folderToWatch, final String version, final String dbSchemaName){
        Validate.notEmpty(folderToWatch, "The folder to watch should not be null or empty!");
        Validate.notEmpty(version, "The version should not be null or empty!");
        Validate.notEmpty(dbSchemaName, "The DB schema name should not be null or empty!");
        
        this.folderToWatch = folderToWatch;
        this.version = version;

        this.dbSchemaName = dbSchemaName;
    }

    /**
     * Get the path to the watched folder.
     * @return the path to the watched folder.
     */
    public String getFolderToWatch(){
        return folderToWatch;
    }

    /**
     * Get the version number of the application.
     * @return the version number of the application.
     */
    public String getVersion(){
        return version;
    }

    public String getDbTableTree() {
        return dbSchemaName+".Tree";
    }

    public String getDbTableFile() {
        return dbSchemaName+".File";
    }
}
