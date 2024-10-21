package org.opendatakit.database.data;
import android.os.Parcel;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.ElementType;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.database.data.ColumnDefinition;
import org.opendatakit.database.data.OrderedColumns;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import static org.junit.Assert.*;

public class OrderedColumnsTest {
   private String appName;
    private String tableId;
    private List<Column> testColumns;

    @Before
    public void setUp() {
        appName = "testApp";
        tableId = "testTable";

        testColumns = new ArrayList<>();
        testColumns.add(new Column("elementKey1", "elementName1", "elementType1", "[]")); // Use an empty JSON array for no children
        testColumns.add(new Column("elementKey2", "elementName2", "elementType2", "[]")); // Modify child keys as per actual validation rules
    }


    @Test
    public void ConstructorWithValidList() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        assertNotNull(orderedColumns);
        assertEquals(appName, orderedColumns.getAppName());
        assertEquals(tableId, orderedColumns.getTableId());
        assertNotNull(orderedColumns.getColumnDefinitions());
    }

    @Test
    public void WriteToParcel() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        Parcel parcel = Parcel.obtain();
        orderedColumns.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        OrderedColumns createdFromParcel = OrderedColumns.CREATOR.createFromParcel(parcel);
        assertEquals(appName, createdFromParcel.getAppName());
        assertEquals(tableId, createdFromParcel.getTableId());

        assertEquals(testColumns.size(), createdFromParcel.getColumns().size());

        parcel.recycle();
    }

    @Test
    public void testFind() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);

        ColumnDefinition foundColumn = orderedColumns.find("elementKey1");
        assertNotNull(foundColumn);
        assertEquals("elementKey1", foundColumn.getElementKey());

        try {
            orderedColumns.find("nonExistentKey");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        }
    }
    @Test
    public void GetRetentionColumnNames() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        ArrayList<String> retentionColumns = orderedColumns.getRetentionColumnNames();
        assertNotNull(retentionColumns);
        assertTrue(retentionColumns.size() > 0);  // At least one retention column should exist
    }

    @Test
    public void GraphViewIsPossible() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        boolean graphView = orderedColumns.graphViewIsPossible();
        assertFalse(graphView);
    }

    @Test
    public void MapViewIsPossible() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        boolean mapView = orderedColumns.mapViewIsPossible();
        assertFalse(mapView);
    }

    @Test
    public void GetGeopointColumnDefinitions() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        ArrayList<ColumnDefinition> geopointColumns = orderedColumns.getGeopointColumnDefinitions();
        assertNotNull(geopointColumns);
        assertTrue(geopointColumns.isEmpty());
    }

    @Test
    public void ParcelCreator() {
        OrderedColumns[] orderedColumnsArray = OrderedColumns.CREATOR.newArray(2);
        assertEquals(2, orderedColumnsArray.length);
    }

    @Test
    public void GetDataModel() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        TreeMap<String, Object> dataModel = orderedColumns.getDataModel();
        assertNotNull(dataModel);
        assertTrue(dataModel.size() > 0);
    }

    @Test
    public void GetExtendedDataModel() {
        OrderedColumns orderedColumns = new OrderedColumns(appName, tableId, testColumns);
        TreeMap<String, Object> extendedDataModel = orderedColumns.getExtendedDataModel();
        assertNotNull(extendedDataModel);
        assertTrue(extendedDataModel.size() > 0);
    }
}
