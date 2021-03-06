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

import com.seaglasslookandfeel.effect.DropShadowEffect;
import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;
import com.seaglasslookandfeel.painter.util.ShapeUtil;

/**
 * ScrollBarButtonPainter implementation.
 */
public final class ScrollBarButtonPainter extends AbstractRegionPainter {
    public static enum Which {
        FOREGROUND_ENABLED,
        FOREGROUND_DISABLED,
        FOREGROUND_PRESSED,
        FOREGROUND_INCREASE_ENABLED,
        FOREGROUND_INCREASE_DISABLED,
        FOREGROUND_INCREASE_PRESSED,

        FOREGROUND_ENABLED_TOGETHER,
        FOREGROUND_DISABLED_TOGETHER,
        FOREGROUND_PRESSED_TOGETHER,
        FOREGROUND_INCREASE_ENABLED_TOGETHER,
        FOREGROUND_INCREASE_DISABLED_TOGETHER,
        FOREGROUND_INCREASE_PRESSED_TOGETHER,

        FOREGROUND_CAP,
    }

    private ButtonStateColors capColors                = new ButtonStateColors(new Color(0xffffff), new Color(0xbbbbbb),
                                                           new Color(0xbdbdbd), new Color(0x555555));

    private ButtonStateColors disabledIncreaseApart    = new ButtonStateColors(new Color(0xd1d1d1), new Color(0xffffff),
                                                           new Color(0xbdbdbd), new Color(0x80555555, true));
    private ButtonStateColors disabledIncreaseTogether = new ButtonStateColors(new Color(0xd1d1d1), new Color(0xe5e5e5),
                                                           new Color(0xbdbdbd), new Color(0x80555555, true));
    private ButtonStateColors enabledIncreaseApart     = new ButtonStateColors(new Color(0xd1d1d1), new Color(0xffffff),
                                                           new Color(0xbdbdbd), new Color(0x555555));
    private ButtonStateColors enabledIncreaseTogether  = new ButtonStateColors(new Color(0xd1d1d1), new Color(0xe5e5e5),
                                                           new Color(0xbdbdbd), new Color(0x555555));
    private ButtonStateColors pressedIncreaseApart     = new ButtonStateColors(new Color(0x8fb1d1), new Color(0xcee2f5),
                                                           new Color(0x82abd0), new Color(0x555555));
    private ButtonStateColors pressedIncreaseTogether  = new ButtonStateColors(new Color(0x8fb1d1), new Color(0xcee2f5),
                                                           new Color(0x82abd0), new Color(0x555555));

    private ButtonStateColors disabledDecreaseApart    = new ButtonStateColors(new Color(0xffffff), new Color(0xcccccc),
                                                           new Color(0xbdbdbd), new Color(0x80555555, true));
    private ButtonStateColors disabledDecreaseTogether = new ButtonStateColors(new Color(0xffffff), new Color(0xe9e9e9),
                                                           new Color(0xbdbdbd), new Color(0x80555555, true));
    private ButtonStateColors enabledDecreaseApart     = new ButtonStateColors(new Color(0xffffff), new Color(0xcccccc),
                                                           new Color(0xbdbdbd), new Color(0x555555));
    private ButtonStateColors enabledDecreaseTogether  = new ButtonStateColors(new Color(0xffffff), new Color(0xe9e9e9),
                                                           new Color(0xbdbdbd), new Color(0x555555));
    private ButtonStateColors pressedDecreaseApart     = new ButtonStateColors(new Color(0xcee2f5), new Color(0x8fb1d1),
                                                           new Color(0x82abd0), new Color(0x555555));
    private ButtonStateColors pressedDecreaseTogether  = new ButtonStateColors(new Color(0xcee2f5), new Color(0x8fb1d1),
                                                           new Color(0x82abd0), new Color(0x555555));

    private Color             darkDivider              = new Color(0x1f000000, true);
    private Color             lightDivider             = new Color(0x3fffffff, true);

    private Effect            dropShadow               = new ScrollButtonDropShadowEffect();

    private Which             state;
    private PaintContext      ctx;

    public ScrollBarButtonPainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch (state) {
        case FOREGROUND_DISABLED:
            paintDecreaseButtonApart(g, c, width, height, disabledDecreaseApart);
            break;
        case FOREGROUND_DISABLED_TOGETHER:
            paintDecreaseButtonTogether(g, c, width, height, disabledDecreaseTogether);
            break;
        case FOREGROUND_ENABLED:
            paintDecreaseButtonApart(g, c, width, height, enabledDecreaseApart);
            break;
        case FOREGROUND_ENABLED_TOGETHER:
            paintDecreaseButtonTogether(g, c, width, height, enabledDecreaseTogether);
            break;
        case FOREGROUND_PRESSED:
            paintDecreaseButtonApart(g, c, width, height, pressedDecreaseApart);
            break;
        case FOREGROUND_PRESSED_TOGETHER:
            paintDecreaseButtonTogether(g, c, width, height, pressedDecreaseTogether);
            break;
        case FOREGROUND_INCREASE_DISABLED:
            paintIncreaseButtonApart(g, c, width, height, disabledIncreaseApart);
            break;
        case FOREGROUND_INCREASE_DISABLED_TOGETHER:
            paintIncreaseButtonTogether(g, c, width, height, disabledIncreaseTogether);
            break;
        case FOREGROUND_INCREASE_ENABLED:
            paintIncreaseButtonApart(g, c, width, height, enabledIncreaseApart);
            break;
        case FOREGROUND_INCREASE_ENABLED_TOGETHER:
            paintIncreaseButtonTogether(g, c, width, height, enabledIncreaseTogether);
            break;
        case FOREGROUND_INCREASE_PRESSED:
            paintIncreaseButtonApart(g, c, width, height, pressedIncreaseApart);
            break;
        case FOREGROUND_INCREASE_PRESSED_TOGETHER:
            paintIncreaseButtonTogether(g, c, width, height, pressedIncreaseTogether);
            break;
        case FOREGROUND_CAP:
            paintCap(g, c, width, height, capColors);
            break;
        }
    }

    @Override
    protected PaintContext getPaintContext() {
        return ctx;
    }

    private void paintCap(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = ShapeUtil.createScrollCap(0, 0, width, height);
        dropShadow.fill(g, s);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(0, 0, width - 1, 0);
    }

    private void paintIncreaseButtonApart(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = ShapeUtil.createScrollButtonApart(0, 0, width, height);
        dropShadow.fill(g, s);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(0, 0, width - 2, 0);
        s = ShapeUtil.createArrowLeft(width / 2.0 - 5, height / 2.0 - 2, 4, 6);
        g.setColor(colors.foreground);
        g.fill(s);
    }

    private void paintDecreaseButtonApart(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = ShapeUtil.createScrollButtonApart(0, 0, width, height);
        dropShadow.fill(g, s);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(0, 0, width - 2, 0);
        s = ShapeUtil.createArrowLeft(width / 2.0 - 4, height / 2.0 - 3, 4, 6);
        g.setColor(colors.foreground);
        g.fill(s);
    }

    private void paintIncreaseButtonTogether(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = ShapeUtil.createScrollButtonTogetherIncrease(0, 0, width, height);
        dropShadow.fill(g, s);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(0, 0, width - 1, 0);
        g.setColor(lightDivider);
        g.drawLine(width - 1, 1, width - 1, height - 1);
        s = ShapeUtil.createArrowLeft(width / 2.0 - 3, height / 2.0 - 3, 4, 6);
        g.setColor(colors.foreground);
        g.fill(s);
    }

    private void paintDecreaseButtonTogether(Graphics2D g, JComponent c, int width, int height, ButtonStateColors colors) {
        Shape s = ShapeUtil.createScrollButtonTogetherDecrease(0, 0, width, height);
        dropShadow.fill(g, s);
        g.setPaint(decodeButtonGradient(s, colors.top, colors.bottom));
        g.fill(s);
        g.setColor(colors.line);
        g.drawLine(2, 0, width - 1, 0);
        g.setColor(darkDivider);
        g.drawLine(width - 1, 1, width - 1, height - 1);
        s = ShapeUtil.createArrowLeft(width / 2.0, height / 2.0 - 3, 4, 6);
        g.setColor(colors.foreground);
        g.fill(s);
    }

    private Paint decodeButtonGradient(Shape s, Color top, Color bottom) {
        int width = s.getBounds().width;
        int height = s.getBounds().height;
        return decodeGradient(0, height / 2, width - 1, height / 2, new float[] { 0f, 1f }, new Color[] { top, bottom });
    }

    /**
     * Customized Nimbus's drop shadow effect for text fields.
     */
    private static class ScrollButtonDropShadowEffect extends DropShadowEffect {

        public ScrollButtonDropShadowEffect() {
            color = new Color(150, 150, 150);
            angle = 0;
            distance = 0;
            size = 3;
            opacity = 0.25f;
        }
    }

    private static class ButtonStateColors {
        public Color top;
        public Color bottom;
        public Color line;
        public Color foreground;

        public ButtonStateColors(Color top, Color bottom, Color line, Color foreground) {
            this.top = top;
            this.bottom = bottom;
            this.line = line;
            this.foreground = foreground;
        }
    }
}
