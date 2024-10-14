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
   public void testGetJsonRepresentation(){
      String ruleId = generateId();
      ColorRule cr = new ColorRule(ruleId, "myElement1", ColorRule.RuleType.NO_OP, "-1", Color.YELLOW, Color.BLACK);
      TreeMap<String,Object> expected = new TreeMap<>();
      expected.put("mValue", "-1");
      expected.put("mElementKey", "myElement1");
      expected.put("mOperator", "NO_OP");
      expected.put("mId", ruleId);
      expected.put("mForeground", Color.YELLOW);
      expected.put("mBackground", Color.BLACK);

      assertEquals(expected, cr.getJsonRepresentation());
   }

   @Test
   public void testToString() {
      ColorRule cr = new ColorRule("uuid13", "myElement", ColorRule.RuleType.EQUAL, "1", Color.BLUE, Color.WHITE);
      String expected = "[id=uuid13, elementKey=myElement, operator=EQUAL, value=1, background=-1, foreground=-16776961]";

      assertEquals(expected, cr.toString());
   }

   private static final String APP_NAME = "colorRuleTest";
   private static final String TABLE_ID_1 = "myTableId_1";
   private static final String COLOR_COL = "Color_Col";

   @Test
   public void testCheckMatch() {
      ColorRule cr1 = new ColorRule(generateId(), "myElement1",  ColorRule.RuleType.EQUAL, "1", Color.BLUE, Color.WHITE);
      ColorRule cr2 = new ColorRule(generateId(), "myElement2",  ColorRule.RuleType.LESS_THAN, "2", Color.BLUE, Color.WHITE);
      ColorRule cr3 = new ColorRule(generateId(), "myElement3",  ColorRule.RuleType.LESS_THAN_OR_EQUAL, "3", Color.BLUE, Color.WHITE);
      ColorRule cr4 = new ColorRule(generateId(), "myElement4",  ColorRule.RuleType.GREATER_THAN, "4", Color.BLUE, Color.WHITE);
      ColorRule cr5 = new ColorRule(generateId(), "myElement5",  ColorRule.RuleType.GREATER_THAN_OR_EQUAL, "5", Color.BLUE, Color.WHITE);

      List<Column> columns = new ArrayList<>();
      columns.add(new Column(COLOR_COL, COLOR_COL, ElementDataType.integer.name(), null));
      OrderedColumns orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID_1, columns);
      String[] primaryKeys = {cr1.getRuleId(), cr2.getRuleId(), cr3.getRuleId(), cr4.getRuleId(), cr5.getRuleId()};
      String[] elementKeys = {"myElement1", "myElement2", "myElement3", "myElement4", "myElement5", "myElement"};
      BaseTable table = new BaseTable(primaryKeys, elementKeys, generateElementKeyToIndex(elementKeys), 5);
      Row row;
      TypedRow rowToMatch;
      row= new Row(new String[]{"1","1","3","5","5","0"}, table);
      table.addRow(row);
      rowToMatch = new TypedRow(table.getRowAtIndex(0),orderedColumns);

      assertTrue(cr1.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr2.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr3.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr4.checkMatch(ElementDataType.integer, rowToMatch));
      assertTrue(cr5.checkMatch(ElementDataType.integer, rowToMatch));

      row= new Row(new String[]{"0","2","4","4","3"}, table);
      table.addRow(row);
      rowToMatch = new TypedRow(table.getRowAtIndex(1),orderedColumns);

      assertFalse(cr1.checkMatch(ElementDataType.integer, rowToMatch));
      assertFalse(cr2.checkMatch(ElementDataType.integer, rowToMatch));
      assertFalse(cr3.checkMatch(ElementDataType.integer, rowToMatch));
      assertFalse(cr4.checkMatch(ElementDataType.integer, rowToMatch));
      assertFalse(cr5.checkMatch(ElementDataType.integer, rowToMatch));
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
}
