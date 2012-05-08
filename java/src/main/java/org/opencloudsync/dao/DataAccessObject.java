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

import org.opencloudsync.tree.Node;

/**
 * Data Access Object generic interface.
 */
public interface DataAccessObject<T> {
    /**
     * Save or the given object in the db.
     * @param reference the object to save
     * @return the id of the row or -1 in all other cases.
     */
    int saveOrUpdate(final T reference);

    /**
     * Find the id corresponding object in the db.
     * @param reference the object to find
     * @return the id of the found object or -1 if not found
     */
    int findIdByReference(final T reference);

    /**
     * Return the digest of the {@link Node} with the given id
     * @param id the id of the {@link Node} to return the digest of
     * @return the digest of the given {@link Node}
     */
    byte[] getDigest(int id);
}
