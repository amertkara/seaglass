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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import javax.swing.JComponent;

import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerSize;
import com.seaglasslookandfeel.painter.util.ShapeUtil.CornerStyle;

/**
 * SpinnerNextButtonPainter implementation.
 */
public final class SpinnerNextButtonPainter extends AbstractCommonColorsPainter {
    public static enum Which {
        BACKGROUND_DISABLED,
        BACKGROUND_ENABLED,
        BACKGROUND_FOCUSED,
        BACKGROUND_PRESSED_FOCUSED,
        BACKGROUND_PRESSED,
        FOREGROUND_DISABLED,
        FOREGROUND_ENABLED,
        FOREGROUND_FOCUSED,
        FOREGROUND_PRESSED_FOCUSED,
        FOREGROUND_PRESSED,
    }

    private Color             spinnerNextBorderBottomEnabled   = decodeColor("spinnerNextBorderBottomEnabled");
    private Color             spinnerNextBorderBottomPressed   = decodeColor("spinnerNextBorderBottomPressed");
    private Color             spinnerNextInteriorBottomEnabled = decodeColor("spinnerNextInteriorBottomEnabled");
    private Color             spinnerNextInteriorBottomPressed = decodeColor("spinnerNextInteriorBottomPressed");

    private TwoColors         pressedButtonBorder              = getCommonBorderColors(CommonControlType.PRESSED);
    private TwoColors         spinnerNextBorderEnabled         = new TwoColors(pressedButtonBorder.top, spinnerNextBorderBottomEnabled);
    private TwoColors         spinnerNextBorderPressed         = new TwoColors(pressedButtonBorder.top, spinnerNextBorderBottomPressed);
    private TwoColors         spinnerNextBorderDisabled        = disable(spinnerNextBorderEnabled);

    private FourColors        pressedButtonInterior            = getCommonInteriorColors(CommonControlType.PRESSED);
    private FourColors        selectedButtonInterior           = getCommonInteriorColors(CommonControlType.SELECTED);
    private TwoColors         spinnerNextInteriorEnabled       = new TwoColors(selectedButtonInterior.top, spinnerNextInteriorBottomEnabled);
    private TwoColors         spinnerNextInteriorPressed       = new TwoColors(pressedButtonInterior.top, spinnerNextInteriorBottomPressed);
    private TwoColors         spinnerNextInteriorDisabled      = desaturate(desaturate(spinnerNextInteriorEnabled));

    private PaintContext      ctx;
    private CommonControlType type;
    private boolean           focused;
    private boolean           isForeground;

    public SpinnerNextButtonPainter(Which state) {
        super();
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);

        type = getButtonType(state);
        focused = (state == Which.BACKGROUND_FOCUSED || state == Which.BACKGROUND_PRESSED_FOCUSED);
        isForeground = isForeground(state);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        if (isForeground) {
            paintArrow(g, width, height);
        } else {
            paintButton(g, c, width, height);
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private CommonControlType getButtonType(Which state) {
        switch (state) {
        case BACKGROUND_DISABLED:
        case FOREGROUND_DISABLED:
            return CommonControlType.DISABLED;
        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
            return CommonControlType.ENABLED;
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            return CommonControlType.PRESSED;
        }
        return null;
    }

    private boolean isForeground(Which state) {
        switch (state) {
        case FOREGROUND_DISABLED:
        case FOREGROUND_ENABLED:
        case FOREGROUND_FOCUSED:
        case FOREGROUND_PRESSED_FOCUSED:
        case FOREGROUND_PRESSED:
            return true;
        default:
            return false;
        }
    }

    private void paintButton(Graphics2D g, JComponent c, int width, int height) {
        boolean useToolBarColors = isInToolBar(c);
        Shape s;

        if (focused) {
            s = createButtonShape(0, 0, width, height, CornerSize.OUTER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.OUTER_FOCUS, useToolBarColors));
            g.fill(s);

            s = createButtonShape(0, 1, width - 1, height - 1, CornerSize.INNER_FOCUS);
            g.setPaint(getFocusPaint(s, FocusType.INNER_FOCUS, useToolBarColors));
            g.fill(s);
        }

        s = createButtonShape(0, 2, width - 2, height - 2, CornerSize.BORDER);
        g.setPaint(getSpinnerNextBorderPaint(s, type));
        g.fill(s);

        s = createButtonShape(1, 3, width - 4, height - 4, CornerSize.INTERIOR);
        g.setPaint(getSpinnerNextInteriorPaint(s, type));
        g.fill(s);
    }

    private void paintArrow(Graphics2D g, int width, int height) {
        Shape s = createArrowShape(width, height);
        g.setPaint(getCommonArrowPaint(s, type));
        g.fill(s);
    }

    private Shape createButtonShape(int x, int y, int width, int height, CornerSize size) {
        return ShapeUtil.createRoundRectangle(x, y, width, height, size, CornerStyle.SQUARE, CornerStyle.SQUARE, CornerStyle.SQUARE,
            CornerStyle.ROUNDED);
    }

    private Shape createArrowShape(int left, int height) {
        int centerX = 8;
        int centerY = height / 2;
        return ShapeUtil.createArrowUp(centerX, centerY, 4, 3);
    }

    public Paint getSpinnerNextBorderPaint(Shape s, CommonControlType type) {
        TwoColors colors = getSpinnerNextBorderColors(type);
        return createVerticalGradient(s, colors);
    }

    public Paint getSpinnerNextInteriorPaint(Shape s, CommonControlType type) {
        TwoColors colors = getSpinnerNextInteriorColors(type);
        return createVerticalGradient(s, colors);
    }

    private TwoColors getSpinnerNextBorderColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return spinnerNextBorderDisabled;
        case ENABLED:
            return spinnerNextBorderEnabled;
        case PRESSED:
            return spinnerNextBorderPressed;
        }
        return null;
    }

    private TwoColors getSpinnerNextInteriorColors(CommonControlType type) {
        switch (type) {
        case DISABLED:
            return spinnerNextInteriorDisabled;
        case ENABLED:
            return spinnerNextInteriorEnabled;
        case PRESSED:
            return spinnerNextInteriorPressed;
        }
        return null;
    }
}
