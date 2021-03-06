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
package com.seaglasslookandfeel.painter.button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.seaglasslookandfeel.effect.Effect;
import com.seaglasslookandfeel.effect.SeaGlassDropShadowEffect;
import com.seaglasslookandfeel.painter.ButtonPainter.Which;

/**
 * Paint a (possibly) segmented button. The default colors are suitable for
 * drawing on a default background, for instance, a dialog box.
 * 
 * @author Kathryn Huxtable
 */
public class SegmentedButtonPainter extends ButtonVariantPainter {

    enum SegmentStatus {
        NONE, FIRST, MIDDLE, LAST
    };

    private static final Color OUTER_FOCUS_COLOR = new Color(0x8072a5d2, true);
    private static final Color INNER_FOCUS_COLOR = new Color(0x73a4d1);

    private final Color        colorShadow       = new Color(0x000000);
    private Effect             dropShadow        = new SeaGlassDropShadowEffect();

    Path2D                     path              = new Path2D.Double();

    public ButtonStateColors   enabled;
    public ButtonStateColors   enabledPressed;
    public ButtonStateColors   defaultButton;
    public ButtonStateColors   defaultPressed;
    public ButtonStateColors   disabled;
    public ButtonStateColors   disabledSelected;

    /**
     * Create a segmented button painter.
     * 
     * @param state
     *            the button state.
     * @param ctx
     *            the paint context.
     */
    public SegmentedButtonPainter(Which state, PaintContext ctx) {
        super(state, ctx);

        // Set the default colors.
        setEnabled(new ButtonStateColors(new Color(0xf3ffffff, true), new Color(0x00ffffff, true), new Color(0x00f7fcff, true), new Color(
            0xffffffff, true), 0.5f, new Color(0xa8d2f2), new Color(0x88ade0), new Color(0x5785bf)));
        setEnabledPressed(new ButtonStateColors(new Color(0xb3eeeeee, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffb4d9ee, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDefault(new ButtonStateColors(new Color(0xc0ffffff, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true), new Color(
            0xffC0E8FF, true), 0.4f, new Color(0x276FB2), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDefaultPressed(new ButtonStateColors(new Color(0xc0eeeeee, true), new Color(0x00eeeeee, true), new Color(0x00A8D9FC, true),
            new Color(0xffB4D9EE, true), 0.4f, new Color(0x134D8C), new Color(0x4F7BBF), new Color(0x3F76BF)));
        setDisabled(new ButtonStateColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true), new Color(
            0xffF7FCFF, true), 0.4f, new Color(0xeeeeee), new Color(0x8AAFE0), new Color(0x5785BF)));
        setDisabledSelected(new ButtonStateColors(new Color(0xc0F4F8FB, true), new Color(0x00ffffff, true), new Color(0x00A8D9FC, true),
            new Color(0xffF7FCFF, true), 0.4f, new Color(0xaaaaaa), new Color(0x8AAFE0), new Color(0x5785BF)));
    }

    public void setEnabled(ButtonStateColors enabled) {
        this.enabled = enabled;
    }

    public void setEnabledPressed(ButtonStateColors enabledPressed) {
        this.enabledPressed = enabledPressed;
    }

    public void setDefault(ButtonStateColors defaultButton) {
        this.defaultButton = defaultButton;
    }

    public void setDefaultPressed(ButtonStateColors defaultPressed) {
        this.defaultPressed = defaultPressed;
    }

    public void setDisabled(ButtonStateColors disabled) {
        this.disabled = disabled;
    }

    public void setDisabledSelected(ButtonStateColors disabledSelected) {
        this.disabledSelected = disabledSelected;
    }

    /**
     * {@inheritDoc}
     */
    public void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {

        ButtonStateColors colors = getButtonColors();
        SegmentStatus segmentStatus = getSegmentStatus(c);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (focused) {
            path = decodeOuterFocus(segmentStatus, width, height);
            g.setColor(OUTER_FOCUS_COLOR);
            g.fill(path);
            path = decodeInnerFocus(segmentStatus, width, height);
            g.setColor(INNER_FOCUS_COLOR);
            g.fill(path);
        }

        path = decodeBorder(segmentStatus, width, height);
        if (!focused) {
            g.drawImage(createDropShadowImage(path, width, height), 0, 0, null);
        }
        g.setPaint(decodeGradientBackground(path, colors.backgroundTop, colors.backgroundBottom));
        g.fill(path);
        path = decodeInterior(segmentStatus, width, height);
        g.setColor(colors.mainColor);
        g.fill(path);
        g.setPaint(decodeGradientBottomShine(path, colors.lowerShineTop, colors.lowerShineBottom, colors.lowerShineMidpoint));
        g.fill(path);
        g.setPaint(decodeGradientTopShine(path, colors.upperShineTop, colors.upperShineBottom));
        g.fill(path);
    }

    /**
     * Get the button colors for the state.
     * 
     * @return the button color set.
     */
    ButtonStateColors getButtonColors() {
        switch (state) {
        case BACKGROUND_DEFAULT:
        case BACKGROUND_DEFAULT_FOCUSED:
        case BACKGROUND_SELECTED:
        case BACKGROUND_SELECTED_FOCUSED:
            return defaultButton;

        case BACKGROUND_PRESSED_DEFAULT:
        case BACKGROUND_PRESSED_DEFAULT_FOCUSED:
            return defaultPressed;

        case BACKGROUND_DISABLED:
            return disabled;

        case BACKGROUND_ENABLED:
        case BACKGROUND_FOCUSED:
            return enabled;

        case BACKGROUND_PRESSED:
        case BACKGROUND_PRESSED_FOCUSED:
        case BACKGROUND_PRESSED_SELECTED:
        case BACKGROUND_PRESSED_SELECTED_FOCUSED:
            return enabledPressed;

        case BACKGROUND_DISABLED_SELECTED:
            return disabledSelected;
        }

        return enabled;
    }

    /**
     * Get the segment status (if any) from the component's client properties.
     * 
     * @param c
     *            the component.
     * @return the segment status.
     */
    SegmentStatus getSegmentStatus(JComponent c) {
        Object buttonType = c.getClientProperty("JButton.buttonType");
        SegmentStatus segmentStatus = SegmentStatus.NONE;
        if (buttonType != null && buttonType instanceof String && ((String) buttonType).startsWith("segmented")) {
            String position = (String) c.getClientProperty("JButton.segmentPosition");
            if ("first".equals(position)) {
                segmentStatus = SegmentStatus.FIRST;
            } else if ("middle".equals(position)) {
                segmentStatus = SegmentStatus.MIDDLE;
            } else if ("last".equals(position)) {
                segmentStatus = SegmentStatus.LAST;
            }
        }
        return segmentStatus;
    }

    /**
     * Create a drop shadow image.
     * 
     * @param s
     *            the shape to use as the shade.
     * @param width
     *            TODO
     * @param height
     *            TODO
     */
    BufferedImage createDropShadowImage(Shape s, int width, int height) {
        BufferedImage bimage = SeaGlassDropShadowEffect.createBufferedImage(width, height, true);
        Graphics2D gbi = bimage.createGraphics();
        gbi.setColor(colorShadow);
        gbi.fill(s);
        return dropShadow.applyEffect(bimage, null, width, height);
    }

    Path2D decodeOuterFocus(SegmentStatus segmentStatus, int width, int height) {
        double arcSize = 6d;
        int x = 0;
        int y = 0;
        switch (segmentStatus) {
        case FIRST:
            decodeFirstSegmentPath(x, y, width, height, arcSize, arcSize);
            break;
        case MIDDLE:
            decodeMiddleSegmentPath(x, y, width, height);
            break;
        case LAST:
            decodeLastSegmentPath(x, y, width, height, arcSize, arcSize);
            break;
        default:
            decodeDefaultPath(x, y, width, height, arcSize, arcSize);
            break;
        }
        return path;
    }

    Path2D decodeInnerFocus(SegmentStatus segmentStatus, int width, int height) {
        double arcSize = 5d;
        int x = 1;
        int y = 1;
        width -= 2;
        height -= 2;
        switch (segmentStatus) {
        case FIRST:
            decodeFirstSegmentPath(x, y, width + 1, height, arcSize, arcSize);
            break;
        case MIDDLE:
            decodeMiddleSegmentPath(x - 1, y, width + 2, height);
            break;
        case LAST:
            decodeLastSegmentPath(x - 1, y, width + 1, height, arcSize, arcSize);
            break;
        default:
            decodeDefaultPath(x, y, width, height, arcSize, arcSize);
            break;
        }
        return path;
    }

    Path2D decodeBorder(SegmentStatus segmentStatus, int width, int height) {
        double arcSize = 4d;
        int x = 2;
        int y = 2;
        width -= 4;
        height -= 4;
        switch (segmentStatus) {
        case FIRST:
            decodeFirstSegmentPath(x, y, width + 2, height, arcSize, arcSize);
            break;
        case MIDDLE:
            decodeMiddleSegmentPath(x - 2, y, width + 4, height);
            break;
        case LAST:
            decodeLastSegmentPath(x - 2, y, width + 2, height, arcSize, arcSize);
            break;
        default:
            decodeDefaultPath(x, y, width, height, arcSize, arcSize);
            break;
        }
        return path;
    }

    Path2D decodeInterior(SegmentStatus segmentStatus, int width, int height) {
        double arcSize = 3d;
        int x = 3;
        int y = 3;
        width -= 6;
        height -= 6;
        switch (segmentStatus) {
        case FIRST:
            decodeFirstSegmentPath(x, y, width + 2, height, arcSize, arcSize);
            break;
        case MIDDLE:
            decodeMiddleSegmentPath(x - 3, y, width + 5, height);
            break;
        case LAST:
            decodeLastSegmentPath(x - 3, y, width + 3, height, arcSize, arcSize);
            break;
        default:
            decodeDefaultPath(x, y, width, height, arcSize, arcSize);
            break;
        }
        return path;
    }

    private void decodeDefaultPath(int left, int top, int width, int height, Double arcW, Double arcH) {
        int bottom = top + height;
        int right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH);
        path.quadTo(left, bottom, left + arcW, bottom);
        path.lineTo(right - arcW, bottom);
        path.quadTo(right, bottom, right, bottom - arcH);
        path.lineTo(right, top + arcH);
        path.quadTo(right, top, right - arcW, top);
        path.closePath();
    }

    private void decodeFirstSegmentPath(int left, int top, int width, int height, Double arcW, Double arcH) {
        int bottom = top + height;
        int right = left + width;
        path.reset();
        path.moveTo(left + arcW, top);
        path.quadTo(left, top, left, top + arcH);
        path.lineTo(left, bottom - arcH);
        path.quadTo(left, bottom, left + arcW, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);
        path.closePath();
    }

    private void decodeLastSegmentPath(int left, int top, int width, int height, Double arcW, Double arcH) {
        int bottom = top + height;
        int right = left + width;
        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right - arcW, bottom);
        path.quadTo(right, bottom, right, bottom - arcH);
        path.lineTo(right, top + arcH);
        path.quadTo(right, top, right - arcW, top);
        path.closePath();
    }

    private void decodeMiddleSegmentPath(int left, int top, int width, int height) {
        int bottom = top + height;
        int right = left + width;
        path.reset();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, top);
        path.closePath();
    }

    /**
     * Create the gradient for the background of the button. This creates the
     * border.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientBackground(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * Create the gradient for the shine at the bottom of the button.
     * 
     * @param color1
     * @param color2
     * @param midpoint
     */
    private Paint decodeGradientBottomShine(Shape s, Color color1, Color color2, float midpoint) {
        Color midColor = new Color(deriveARGB(color1, color2, midpoint) & 0xFFFFFF, true);
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, midpoint, 1f }, new Color[] {
            color1,
            midColor,
            color2 });
    }

    /**
     * Create the gradient for the shine at the top of the button.
     * 
     * @param s
     * @param color1
     * @param color2
     * @return
     */
    private Paint decodeGradientTopShine(Shape s, Color color1, Color color2) {
        Rectangle2D bounds = s.getBounds2D();
        float x = (float) bounds.getX();
        float y = (float) bounds.getY();
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        return decodeGradient((0.5f * w) + x, y, (0.5f * w) + x, h + y, new float[] { 0f, 1f }, new Color[] { color1, color2 });
    }

    /**
     * A set of colors to use for the button.
     */
    public class ButtonStateColors {

        public Color upperShineTop;
        public Color upperShineBottom;
        public Color lowerShineTop;
        public Color lowerShineBottom;
        public float lowerShineMidpoint;
        public Color mainColor;
        public Color backgroundTop;
        public Color backgroundBottom;

        public ButtonStateColors(Color upperShineTop, Color upperShineBottom, Color lowerShineTop, Color lowerShineBottom,
            float lowerShineMidpoint, Color mainColor, Color backgroundTop, Color backgroundBottom) {
            this.upperShineTop = upperShineTop;
            this.upperShineBottom = upperShineBottom;
            this.lowerShineTop = lowerShineTop;
            this.lowerShineBottom = lowerShineBottom;
            this.lowerShineMidpoint = lowerShineMidpoint;
            this.mainColor = mainColor;
            this.backgroundTop = backgroundTop;
            this.backgroundBottom = backgroundBottom;
        }
    }
}
