package org.opendatakit.database.utilities;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opendatakit.utilities.StaticStateManipulator;

public class QueryUtilTest {

    String tableId;
    String whereClause;
    String [] groupBy;
    String havingClause;
    String [] orderBy;
    String [] direction;

    @BeforeClass
    public static void oneTimeSetUp() {
        StaticStateManipulator.get().reset();
    }

    @Before
    public void setup() {
        tableId = TEST_TABLE;
    }

    @Test
    public void buildSqlStatement_withBasicInput_returnBasicOutput() {
        assertEquals(SELECT_ALL, getBuiltSqlStatement());
    }
    private String getBuiltSqlStatement(){
        return QueryUtil.buildSqlStatement(tableId, whereClause, groupBy, havingClause, orderBy, direction);
    }

    @Test
    public void buildSqlStatement_withWhereClause_returnWhereStatement() {
        whereClause = SORT_FIELD_1 + "=" + SORT_PARAM;
        assertEquals(SELECT_WHERE, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withHavingClause_returnHavingStatement() {
        groupBy = new String[]{GROUP_1};
        havingClause = HAVING_CLAUSE;
        assertEquals(SELECT_HAVING, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withOrderBy_returnOrderByStatement() {
        orderBy = new String[]{SORT_FIELD_3};
        assertEquals(SELECT_ORDER_BY, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withOrderByDirection_returnOrderByDirectionStatement() {
        orderBy = new String[]{SORT_FIELD_2};
        direction = new String[]{DESC};
        assertEquals(SELECT_ORDER_BY_ORDER_DESC, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withAllClauses_returnComplexStatement() {
        whereClause = SORT_FIELD_1 + "=" + SORT_PARAM;
        groupBy = new String[]{GROUP_1, GROUP_2};
        havingClause = HAVING_CLAUSE;
        orderBy = new String[]{SORT_FIELD_2, SORT_FIELD_1, ""};
        direction = new String[]{DESC, ASC, null};
        assertEquals(SELECT_ALL_WHERE_HAVING_ORDER_BY_ORDER_DIRECTION, getBuiltSqlStatement());
    }

    @Test
    public void givenArgString_whenConvertToArray_thenReturnArrayOfString(){
        direction = QueryUtil.convertStringToArray(null);
        assertArrayEquals(direction, new String[]{});
        direction = QueryUtil.convertStringToArray(DESC);
        assertArrayEquals(direction, new String[]{DESC});
    }

    @After
    public void tearDown(){
        tableId = null;
        whereClause = null;
        groupBy = null;
        havingClause = null;
        orderBy = null;
        direction = null;
    }

    private static final String TEST_TABLE = "test_table";
    private static final String SORT_PARAM = "'Bob'";
    private static final String HAVING_CLAUSE = "COUNT(class) > 4";
    private static final String GROUP_1 = "class";
    private static final String GROUP_2 = "birthday";
    private static final String SORT_FIELD_1 = "name";
    private static final String SORT_FIELD_2 = "created_at";
    private static final String SORT_FIELD_3 = "updated_at";
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";
    private static final String SELECT_ALL = "SELECT * FROM \""+TEST_TABLE+"\" " ;
    private static final String SELECT_WHERE = "SELECT * FROM \""+TEST_TABLE+"\"  WHERE "+ SORT_FIELD_1 + "=" + SORT_PARAM;
    private static final String SELECT_HAVING = "SELECT * FROM \""+TEST_TABLE+"\"  GROUP BY class HAVING "+HAVING_CLAUSE;
    private static final String SELECT_ORDER_BY = "SELECT * FROM \""+TEST_TABLE+"\"  ORDER BY "+ SORT_FIELD_3 +" ASC";
    private static final String SELECT_ORDER_BY_ORDER_DESC = "SELECT * FROM \""+TEST_TABLE+"\"  ORDER BY "+ SORT_FIELD_2 +" "+DESC;
    private static final String SELECT_ALL_WHERE_HAVING_ORDER_BY_ORDER_DIRECTION =
            "SELECT * FROM \""+TEST_TABLE+"\"  WHERE "+ SORT_FIELD_1 + "=" + SORT_PARAM +" GROUP BY "+
                    GROUP_1+", "+GROUP_2+" HAVING "+HAVING_CLAUSE+" ORDER BY "+ SORT_FIELD_2 +" "+DESC+", "+ SORT_FIELD_1 +" "+ASC;

    @AfterClass
    public static void clearProps() {
        StaticStateManipulator.get().reset();
    }
}
