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
package com.seaglass.painter;

/**
 * ScrollBarThumbPainter implementation.
 */
public final class ScrollBarThumbPainter extends AbstractImagePainter {
    // package private integers representing the available states that
    // this painter will paint. These are used when creating a new instance
    // of ScrollBarThumbPainter to determine which region/state is being painted
    // by that instance.
    public static final int BACKGROUND_DISABLED  = 1;
    public static final int BACKGROUND_ENABLED   = 2;
    public static final int BACKGROUND_FOCUSED   = 3;
    public static final int BACKGROUND_MOUSEOVER = 4;
    public static final int BACKGROUND_PRESSED   = 5;

    public ScrollBarThumbPainter(PaintContext ctx, int state) {
        super(ctx, state);
    }

    @Override
    protected String getImageName(int state) {
        switch (state) {
        case BACKGROUND_DISABLED:
            return "h_scroll_thumb_disabled";
        case BACKGROUND_ENABLED:
            return "h_scroll_thumb";
        case BACKGROUND_MOUSEOVER:
            return "h_scroll_thumb";
        case BACKGROUND_PRESSED:
            return "h_scroll_thumb_pressed";
        }
        return null;
    }
}
