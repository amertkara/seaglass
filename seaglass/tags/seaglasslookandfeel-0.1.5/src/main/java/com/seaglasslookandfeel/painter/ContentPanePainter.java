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
package com.seaglasslookandfeel.painter;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

/**
 * ContentPanePainter implementation.
 */
public class ContentPanePainter extends AbstractRegionPainter {
    public static enum Which {
        BACKGROUND_ENABLED, BACKGROUND_ENABLED_WINDOWFOCUSED,
    };

    private Color        activeTopColor      = decodeColor("seaGlassToolBarActiveTopT");
    private Color        inactiveTopColor    = decodeColor("seaGlassToolBarInactiveTopT");

    private Color        activeBottomColor   = decodeColor("seaGlassToolBarActiveBottomB");
    private Color        inactiveBottomColor = decodeColor("seaGlassToolBarInactiveBottomB");

    // Refers to one of the static final ints above
    private Which        state;
    private PaintContext ctx;

    public ContentPanePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.NO_CACHING);
    }

    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        paintBackground(g, c, width, height);
    }

    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackground(Graphics2D g, JComponent c, int width, int height) {
        g.setPaint(decodeGradient(state, c, width, height));
        g.fillRect(0, 0, width, height);
    }

    private GradientPaint decodeGradient(Which state, JComponent c, int width, int height) {
        return new GradientPaint(0, 0, getTopColor(state, c), 0, height, getBottomColor(state));
    }

    private Color getTopColor(Which state, JComponent c) {
        if (state == Which.BACKGROUND_ENABLED) {
            return inactiveTopColor;
        } else {
            return activeTopColor;
        }
    }

    private Color getBottomColor(Which state) {
        if (state == Which.BACKGROUND_ENABLED) {
            return inactiveBottomColor;
        } else {
            return activeBottomColor;
        }
    }
}
