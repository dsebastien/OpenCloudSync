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

import org.opencloudsync.dao.TreeDAO;
import org.opencloudsync.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Index service.
 */
//todo rename to IndexService
@Service
public class IndexManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexManager.class);

    private TreeDAO treeDAO;

    public IndexManager() {
    }

    @Autowired
    public void setTreeDAO(final TreeDAO treeDAO){
        this.treeDAO = treeDAO;
    }

    /**
     * Save or update the given tree
     * @param treeReference the tree to save or update.
     */
    public void saveOrUpdate(final TreeReference treeReference){
        treeDAO.saveOrUpdate(treeReference);
    }
}
