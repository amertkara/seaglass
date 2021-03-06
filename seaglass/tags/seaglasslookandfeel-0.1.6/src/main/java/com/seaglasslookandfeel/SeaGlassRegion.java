/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the SeaGlass Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id$
 */
package com.seaglasslookandfeel;

import javax.swing.plaf.synth.Region;

public class SeaGlassRegion extends Region {

    public static final Region SEARCH_FIELD_FIND_BUTTON   = new SeaGlassRegion("SearchFieldFindButton", null, true);

    public static final Region SEARCH_FIELD_CANCEL_BUTTON = new SeaGlassRegion("SearchFieldCancelButton", null, true);

    public static final Region SCROLL_BAR_CAP             = new SeaGlassRegion("ScrollBarCap", null, true);

    protected SeaGlassRegion(String name, String ui, boolean subregion) {
        super(name, ui, subregion);
    }
}
