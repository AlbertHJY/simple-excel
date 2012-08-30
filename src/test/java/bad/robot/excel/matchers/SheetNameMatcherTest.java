/*
 * Copyright (c) 2012, bad robot (london) ltd.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package bad.robot.excel.matchers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static bad.robot.excel.WorkbookResource.getWorkbook;
import static bad.robot.excel.matchers.SheetNameMatcher.containsSameNamedSheetsAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class SheetNameMatcherTest {

    private final Description description = new StringDescription();

    private HSSFWorkbook workbookWithOneNamedSheet;
    private HSSFWorkbook workbookWithManyNamedSheets;

    @Before
    public void loadWorkbooks() throws IOException {
        workbookWithOneNamedSheet = getWorkbook("workbookWithOneSheet.xls");
        workbookWithManyNamedSheets = getWorkbook("workbookWithMultipleNamedSheets.xls");
    }

    @Test
    public void exampleUsages() {
        assertThat(workbookWithManyNamedSheets, containsSameNamedSheetsAs(workbookWithManyNamedSheets));
        assertThat(workbookWithOneNamedSheet, not(containsSameNamedSheetsAs(workbookWithManyNamedSheets)));
    }

    @Test
    public void matches() {
        assertThat(containsSameNamedSheetsAs(workbookWithManyNamedSheets).matches(workbookWithManyNamedSheets), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(containsSameNamedSheetsAs(workbookWithManyNamedSheets).matches(workbookWithOneNamedSheet), is(false));
    }

    @Test
    public void descriptionWhenNotPreviousDescriptionHasBeenAppended() {
        containsSameNamedSheetsAs(workbookWithManyNamedSheets).describeTo(description);
        assertThat(description.toString(), is("named \"Sheet1\", \"Another Sheet\", \"Yet Another Sheet\", \"Sheet5\""));
    }

    @Test
    public void descriptionWhenPreviousDescriptionHasBeenAppended() {
        description.appendText("Expected: ");
        containsSameNamedSheetsAs(workbookWithManyNamedSheets).describeTo(description);
        assertThat(description.toString(), is("Expected: workbook to contain sheets named \"Sheet1\", \"Another Sheet\", \"Yet Another Sheet\", \"Sheet5\""));
    }

    @Test
    public void mismatch() {
        containsSameNamedSheetsAs(workbookWithManyNamedSheets).matchesSafely(workbookWithOneNamedSheet, description);
        assertThat(description.toString(), is("\"Another Sheet\", \"Yet Another Sheet\", \"Sheet5\" were not found"));
    }
}
