package org.opendatakit.database.data;

import android.os.Parcel;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.aggregate.odktables.rest.entity.Column;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ColumnListTest {

    private List<Column> testColumns;

    @Before
    public void setUp() {
        // Initialize a list of columns
        testColumns = new ArrayList<>();
        testColumns.add(new Column("elementKey1", "elementName1", "elementType1", "childKeys1"));
        testColumns.add(new Column("elementKey2", "elementName2", "elementType2", "childKeys2"));
    }

    @Test
    public void ConstructorWithValidList() {
        ColumnList columnList = new ColumnList(testColumns);
        assertNotNull(columnList);
        assertEquals(testColumns, columnList.getColumns());
    }


    @Test
    public void WriteToParcel() {
        ColumnList columnList = new ColumnList(testColumns);
        Parcel parcel = Parcel.obtain();
        columnList.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        ColumnList createdFromParcel = ColumnList.CREATOR.createFromParcel(parcel);
        assertEquals(testColumns.size(), createdFromParcel.getColumns().size());

        for (int i = 0; i < testColumns.size(); i++) {
            Column original = testColumns.get(i);
            Column fromParcel = createdFromParcel.getColumns().get(i);
            assertEquals(original.getElementKey(), fromParcel.getElementKey());
            assertEquals(original.getElementName(), fromParcel.getElementName());
            assertEquals(original.getElementType(), fromParcel.getElementType());
            assertEquals(original.getListChildElementKeys(), fromParcel.getListChildElementKeys());
        }

        parcel.recycle();
    }

    @Test
    public void ReadFromParcel() {
        Parcel parcel = Parcel.obtain();
        parcel.writeInt(2);

        for (Column column : testColumns) {
            parcel.writeString(column.getElementKey());
            parcel.writeString(column.getElementName());
            parcel.writeString(column.getElementType());
            parcel.writeString(column.getListChildElementKeys());
        }

        parcel.setDataPosition(0);

        ColumnList columnList = ColumnList.CREATOR.createFromParcel(parcel);
        assertEquals(testColumns.size(), columnList.getColumns().size());

        for (int i = 0; i < testColumns.size(); i++) {
            Column original = testColumns.get(i);
            Column fromParcel = columnList.getColumns().get(i);
            assertEquals(original.getElementKey(), fromParcel.getElementKey());
            assertEquals(original.getElementName(), fromParcel.getElementName());
            assertEquals(original.getElementType(), fromParcel.getElementType());
            assertEquals(original.getListChildElementKeys(), fromParcel.getListChildElementKeys());
        }

        parcel.recycle();
    }

    @Test
    public void ParcelCreator() {
        ColumnList[] columnArray = ColumnList.CREATOR.newArray(2);
        assertEquals(2, columnArray.length);
    }
}