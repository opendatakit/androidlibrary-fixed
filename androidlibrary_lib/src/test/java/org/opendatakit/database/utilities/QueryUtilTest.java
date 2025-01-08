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
    private static final String SELECT = "SELECT * FROM ";
    private static final String WHERE = " WHERE ";
    private static final String HAVING = " HAVING ";
    private static final String EQUALS = "=";
    private static final String GROUP_BY = " GROUP BY ";
    private static final String ORDER_BY = " ORDER BY ";

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

    private String getBuiltSqlStatement(){
        return QueryUtil.
                buildSqlStatement(tableId, whereClause, groupBy, havingClause, orderBy, direction);
    }

    @Test
    public void buildSqlStatement_withBasicInput_returnBasicOutput() {
        assertEquals(SELECT+"\""+TEST_TABLE+"\" ", getBuiltSqlStatement());
    }
    @Test
    public void buildSqlStatement_withWhereClause_returnWhereStatement() {
        whereClause = SORT_FIELD_1 + EQUALS + SORT_PARAM;
        String expected = SELECT + "\""+TEST_TABLE+"\" " + WHERE + SORT_FIELD_1 + EQUALS + SORT_PARAM;
        assertEquals(expected, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withHavingClause_returnHavingStatement() {
        groupBy = new String[]{GROUP_1};
        havingClause = HAVING_CLAUSE;
        String expected = SELECT + "\""+TEST_TABLE+"\" "+ GROUP_BY + GROUP_1+HAVING+HAVING_CLAUSE;
        assertEquals(expected, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withOrderBy_returnOrderByStatement() {
        orderBy = new String[]{SORT_FIELD_3};
        String expected = SELECT+"\""+TEST_TABLE+"\" "+ ORDER_BY + SORT_FIELD_3 +" ASC";
        assertEquals(expected, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withOrderByDirection_returnOrderByDirectionStatement() {
        orderBy = new String[]{SORT_FIELD_2};
        direction = new String[]{DESC};
        String expected = SELECT+"\""+TEST_TABLE+"\" "+ORDER_BY+ SORT_FIELD_2 +" "+DESC;
        assertEquals(expected, getBuiltSqlStatement());
    }

    @Test
    public void buildSqlStatement_withAllClauses_returnComplexStatement() {
        whereClause = SORT_FIELD_1 + EQUALS + SORT_PARAM;
        groupBy = new String[]{GROUP_1, GROUP_2};
        havingClause = HAVING_CLAUSE;
        orderBy = new String[]{SORT_FIELD_2, SORT_FIELD_1, ""};
        direction = new String[]{DESC, ASC, null};

        String expected = SELECT+"\""+TEST_TABLE+"\" "+WHERE+SORT_FIELD_1+EQUALS+SORT_PARAM
                +GROUP_BY+GROUP_1+", "+GROUP_2+HAVING+HAVING_CLAUSE+ORDER_BY+
                SORT_FIELD_2 +" "+DESC+", "+ SORT_FIELD_1 +" "+ASC;
        assertEquals(expected, getBuiltSqlStatement());
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

    @AfterClass
    public static void clearProps() {
        StaticStateManipulator.get().reset();
    }
}
