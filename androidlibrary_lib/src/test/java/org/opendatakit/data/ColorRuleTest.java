/*
 * Copyright (C) 2015 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.opendatakit.data.ColorRule.RuleType.getValues;

import android.graphics.Color;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.database.data.BaseTable;
import org.opendatakit.database.data.OrderedColumns;
import org.opendatakit.database.data.Row;
import org.opendatakit.database.data.TypedRow;
import org.opendatakit.logging.WebLogger;
import org.opendatakit.logging.desktop.WebLoggerDesktopFactoryImpl;
import org.opendatakit.utilities.StaticStateManipulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@RunWith(JUnit4.class)
public class ColorRuleTest {

   @BeforeClass
   public static void oneTimeSetUp() {
      StaticStateManipulator.get().reset();
      WebLogger.setFactory(new WebLoggerDesktopFactoryImpl());
   }

   @Test
   public void testColorRule() {
      ColorRule cr1 = new ColorRule("myElement", ColorRule.RuleType.EQUAL, "5", Color.BLUE, Color
          .WHITE);
      ColorRule cr2 = new ColorRule("myElement", ColorRule.RuleType.EQUAL, "5", Color.BLUE, Color
          .WHITE);

      Assert.assertTrue(cr1.equalsWithoutId(cr2));
      Assert.assertTrue(cr2.equalsWithoutId(cr1));

      Assert.assertEquals(Color.WHITE, cr1.getBackground());
      Assert.assertEquals(Color.BLUE, cr1.getForeground());
      Assert.assertEquals(ColorRule.RuleType.EQUAL, cr1.getOperator());
      Assert.assertEquals("myElement", cr1.getColumnElementKey());
      Assert.assertEquals("5", cr1.getVal());

      String crs1 = cr1.toString();
      String crs2 = cr2.toString();
      Assert.assertEquals(crs1.substring(crs1.indexOf(',')), crs2.substring((crs2.indexOf(','))));

      Assert.assertNotEquals(cr1.getRuleId(), cr2.getRuleId());
      Assert.assertNotEquals(cr1, cr2);

      cr2.setVal("6");
      Assert.assertFalse(cr1.equalsWithoutId(cr2));
      Assert.assertEquals("6", cr2.getVal());
      cr1.setVal("6");

      Assert.assertTrue(cr1.equalsWithoutId(cr2));

      cr2.setBackground(Color.GREEN);
      Assert.assertFalse(cr1.equalsWithoutId(cr2));
      Assert.assertEquals(Color.GREEN, cr2.getBackground());
      cr1.setBackground(Color.GREEN);

      Assert.assertTrue(cr1.equalsWithoutId(cr2));

      cr2.setForeground(Color.RED);
      Assert.assertFalse(cr1.equalsWithoutId(cr2));
      Assert.assertEquals(Color.RED, cr2.getForeground());
      cr1.setForeground(Color.RED);

      Assert.assertTrue(cr1.equalsWithoutId(cr2));

      cr2.setOperator(ColorRule.RuleType.GREATER_THAN);
      Assert.assertFalse(cr1.equalsWithoutId(cr2));
      Assert.assertEquals(ColorRule.RuleType.GREATER_THAN, cr2.getOperator());
      cr1.setOperator(ColorRule.RuleType.GREATER_THAN);

      Assert.assertTrue(cr1.equalsWithoutId(cr2));

      cr2.setColumnElementKey("fredColumn");
      Assert.assertFalse(cr1.equalsWithoutId(cr2));
      Assert.assertEquals("fredColumn", cr2.getColumnElementKey());
      cr1.setColumnElementKey("fredColumn");

      Assert.assertTrue(cr1.equalsWithoutId(cr2));
   }

   //Test to show if the returned Json representation for ColorRule is correct
   @Test
   public void testGetSymbol() {
      assertEquals("=", ColorRule.RuleType.EQUAL.getSymbol());
   }
   @Test
   public void testGetEnumFromString() {
      CharSequence [] ruleTypeStrings = getValues();
      ColorRule.RuleType expected = ColorRule.RuleType.LESS_THAN;
      assertEquals(expected, ColorRule.RuleType.getEnumFromString(ruleTypeStrings[0].toString()));
      expected = ColorRule.RuleType.LESS_THAN_OR_EQUAL;
      assertEquals(expected, ColorRule.RuleType.getEnumFromString(ruleTypeStrings[1].toString()));
      expected = ColorRule.RuleType.EQUAL;
      assertEquals(expected, ColorRule.RuleType.getEnumFromString(ruleTypeStrings[2].toString()));
      expected = ColorRule.RuleType.GREATER_THAN_OR_EQUAL;
      assertEquals(expected, ColorRule.RuleType.getEnumFromString(ruleTypeStrings[3].toString()));
      expected = ColorRule.RuleType.GREATER_THAN;
      assertEquals(expected, ColorRule.RuleType.getEnumFromString(ruleTypeStrings[4].toString()));
      expected = ColorRule.RuleType.NO_OP;
      assertEquals(expected, ColorRule.RuleType.getEnumFromString(""));
      try {
         ColorRule.RuleType.getEnumFromString("odk");
      }catch (IllegalArgumentException e) {
         assertEquals("unrecognized rule operator: odk", e.getMessage());
      }
   }

   @Test
   public void testGetJsonRepresentation(){
      String ruleId = generateId();
      ColorRule cr = new ColorRule(ruleId, MY_ELEMENT, ColorRule.RuleType.NO_OP, "-1", Color.YELLOW, Color.BLACK);
      TreeMap<String,Object> expected = new TreeMap<>();
      expected.put("mValue", "-1");
      expected.put("mElementKey", MY_ELEMENT);
      expected.put("mOperator", "NO_OP");
      expected.put("mId", ruleId);
      expected.put("mForeground", Color.YELLOW);
      expected.put("mBackground", Color.BLACK);

      assertEquals(expected, cr.getJsonRepresentation());
   }

   @Test
   public void testToString() {
      ColorRule cr = new ColorRule("uuid13", MY_ELEMENT, ColorRule.RuleType.EQUAL, "1", Color.BLUE, Color.WHITE);
      String expected = "[id=uuid13, elementKey=myElement, operator=EQUAL, value=1, background=-1, foreground=-16776961]";

      assertEquals(expected, cr.toString());
   }
   
   @Test
   public void testCheckMatch() {
      //Create test values
      ColorRule cr1 = new ColorRule(generateId(), MY_ELEMENT_1,  ColorRule.RuleType.EQUAL, "1", Color.BLUE, Color.WHITE);
      ColorRule cr2 = new ColorRule(generateId(), MY_ELEMENT_2,  ColorRule.RuleType.LESS_THAN, "2", Color.BLUE, Color.WHITE);
      ColorRule cr3 = new ColorRule(generateId(), MY_ELEMENT_3,  ColorRule.RuleType.LESS_THAN_OR_EQUAL, "3", Color.BLUE, Color.WHITE);
      ColorRule cr4 = new ColorRule(generateId(), MY_ELEMENT_4,  ColorRule.RuleType.GREATER_THAN, "4", Color.BLUE, Color.WHITE);
      ColorRule cr5 = new ColorRule(generateId(), MY_ELEMENT_5,  ColorRule.RuleType.GREATER_THAN_OR_EQUAL, "5", Color.BLUE, Color.WHITE);
      ColorRule cr6 = new ColorRule(generateId(), MY_ELEMENT_6,  ColorRule.RuleType.GREATER_THAN_OR_EQUAL, "5", Color.BLUE, Color.WHITE);
      ColorRule cr = new ColorRule(generateId(), MY_ELEMENT,  ColorRule.RuleType.NO_OP, "5", Color.BLUE, Color.WHITE);

      //Setup Color table
      String[] primaryKeys = {cr1.getRuleId(), cr2.getRuleId(), cr3.getRuleId(), cr4.getRuleId(), cr5.getRuleId()};
      String[] elementKeys = {MY_ELEMENT_1, MY_ELEMENT_2, MY_ELEMENT_3, MY_ELEMENT_4, MY_ELEMENT_5, MY_ELEMENT, MY_ELEMENT_6};
      BaseTable table = new BaseTable(primaryKeys, elementKeys, generateElementKeyToIndex(elementKeys), 5);
      //Define the table's column
      List<Column> columns = new ArrayList<>();
      columns.add(new Column(COLOR_COL, COLOR_COL, ElementDataType.integer.name(), null));
      OrderedColumns orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID_1, columns);
      //Define the table's rows
      Row row;
      TypedRow rowToMatch;
      row= new Row(new String[]{"1","1","3","5","5",null}, table);
      table.addRow(row);
      rowToMatch = new TypedRow(table.getRowAtIndex(0),orderedColumns);
      //Check that all RuleTypes work with integer or number type
      assertTrue(cr1.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr2.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr3.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr4.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr5.checkMatch(ElementDataType.number, rowToMatch));
      assertFalse(cr.checkMatch(ElementDataType.integer, rowToMatch));

      row= new Row(new String[]{"","false","[44,67]","4","3","5","odk"}, table);
      table.addRow(row);
      rowToMatch = new TypedRow(table.getRowAtIndex(1),orderedColumns);
      //Check that RuleTypes work with non-numeric types
      assertFalse(cr1.checkMatch(ElementDataType.string, rowToMatch));
      cr2.setVal("true");
      assertTrue(cr2.checkMatch(ElementDataType.bool, rowToMatch));
      cr3.setVal("[2,3,4]");
      assertFalse(cr3.checkMatch(ElementDataType.array, rowToMatch));
      assertFalse(cr4.checkMatch(ElementDataType.string, rowToMatch));
      assertFalse(cr5.checkMatch(ElementDataType.string, rowToMatch));
     try {
        cr.checkMatch(ElementDataType.integer, rowToMatch);
     }catch (IllegalArgumentException e) {
        assertEquals("unrecognized op passed to checkMatch: NO_OP", e.getMessage());
     }
      cr6.checkMatch(ElementDataType.integer, rowToMatch);
   }

   private String generateId(){
      return UUID.randomUUID().toString();
   }

   private Map<String, Integer> generateElementKeyToIndex(String[] elementKeys) {
      Map<String, Integer> elementKeyToIndex = new HashMap<>();
      for (int i = 0; i < elementKeys.length; i++) {
         elementKeyToIndex.put(elementKeys[i], i);
      }
      return elementKeyToIndex;
   }
   private static final String APP_NAME = "colorRuleTest";
   private static final String TABLE_ID_1 = "myTableId_1";
   private static final String COLOR_COL = "Color_Col";
   private static final String MY_ELEMENT = "myElement";
   private static final String MY_ELEMENT_1 = "myElement1";
   private static final String MY_ELEMENT_2 = "myElement2";
   private static final String MY_ELEMENT_3 = "myElement3";
   private static final String MY_ELEMENT_4 = "myElement4";
   private static final String MY_ELEMENT_5 = "myElement5";
   private static final String MY_ELEMENT_6 = "myElement6";
}
