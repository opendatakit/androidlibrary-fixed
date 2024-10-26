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
}
