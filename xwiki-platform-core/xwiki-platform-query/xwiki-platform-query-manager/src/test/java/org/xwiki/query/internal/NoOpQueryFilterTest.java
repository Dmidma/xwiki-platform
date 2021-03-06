/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.query.internal;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link NoOpQueryFilter}
 *
 * @version $Id$
 * @since 5.1M2
 */
public class NoOpQueryFilterTest
{
    @Test
    public void verifyStatementAndResultsAreUnchanged()
    {
        NoOpQueryFilter filter = new NoOpQueryFilter();

        String statement = "statement";
        Assert.assertSame(statement, filter.filterStatement(statement, "language"));

        List results = Arrays.asList("result1", "result2");
        Assert.assertSame(results, filter.filterResults(results));
    }
}
