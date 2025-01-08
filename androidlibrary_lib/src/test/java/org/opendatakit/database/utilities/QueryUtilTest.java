package org.opendatakit.database.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueryUtilTest {

    @Test
    public void buildSqlStatement_withBasicInput_returnBasicOutput() {
        String tableId = "test_table";
        String sql = QueryUtil.buildSqlStatement(tableId, null, null, null, null, null);
        assertEquals("SELECT * FROM \"test_table\" ", sql);
    }

    @Test
    public void buildSqlStatement_withWhereClause_returnWhereStatement() {
        String whereClause = "name = 'Bob'";
        String sql = QueryUtil.buildSqlStatement("test_table", whereClause, null, null, null, null);
        assertEquals("SELECT * FROM \"test_table\"  WHERE name = 'Bob'", sql);
    }

    @Test
    public void buildSqlStatement_withHavingClause_returnHavingStatement() {
        String[] groupBy = { "class" };
        String havingClause = "COUNT(class) > 4";
        String sql = QueryUtil.buildSqlStatement("test_table", null, groupBy, havingClause, null, null);
        assertEquals("SELECT * FROM \"test_table\"  GROUP BY class HAVING COUNT(class) > 4", sql);
    }

    @Test
    public void buildSqlStatement_withOrderBy_returnOrderByStatement() {
        String[] orderBy = { "updated_at" };
        String sql = QueryUtil.buildSqlStatement("test_table", null, null, null, orderBy, null);
        assertEquals("SELECT * FROM \"test_table\"  ORDER BY updated_at ASC", sql);
    }

    @Test
    public void buildSqlStatement_withOrderByDirection_returnOrderByDirectionStatement() {
        String[] orderBy = { "created_at" };
        String[] orderByDirection = { "DESC" };
        String sql = QueryUtil.buildSqlStatement("test_table", null, null, null, orderBy, orderByDirection);
        assertEquals("SELECT * FROM \"test_table\"  ORDER BY created_at DESC", sql);
    }

    @Test
    public void buildSqlStatement_withAllClauses_returnComplexStatement() {
        String[] groupBy = { "class" };
        String[] orderBy = { "created_at", "name" };
        String[] orderByDirection = { "DESC", "ASC" };
        String sql = QueryUtil.buildSqlStatement("test_table", "name = 'Bob'", groupBy, "COUNT(class) > 4", orderBy, orderByDirection);
        assertEquals("SELECT * FROM \"test_table\"  WHERE name = 'Bob' GROUP BY class HAVING COUNT(class) > 4 ORDER BY created_at DESC, name ASC", sql);

    }
}
