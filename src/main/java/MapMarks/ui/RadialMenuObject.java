package MapMarks.ui;

import MapMarks.utils.ColorDatabase;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;

public class RadialMenuObject extends AbstractWidget<RadialMenuObject> {
    private LayeredTextureWidget ltw;
    protected static final float WIDTH = 118;
    protected static final float HEIGHT = 108;

//    private Color baseColor;
//    private Color dimColor;
    private ColorEnum color;

    private Color baseColor;
    private Color dimColor;

    private boolean isDimmed = false;

    public RadialMenuObject(Color special) {
        this.color = ColorEnum.WHITE;

        this.baseColor = special;
        this.dimColor = special;

        ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
                .withLayer(MapMarksTextureDatabase.RADIAL_BASE.getTexture(), baseColor)
                .withLayer(MapMarksTextureDatabase.RADIAL_TRIM.getTexture(), ColorDatabase.UI_TRIM);
    }

    public RadialMenuObject(ColorEnum color) {
        this.color = color;

        this.baseColor = color.get();
        this.dimColor = color.getDimmed();

        ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
                .withLayer(MapMarksTextureDatabase.RADIAL_BASE.getTexture(), baseColor)
                .withLayer(MapMarksTextureDatabase.RADIAL_TRIM.getTexture(), ColorDatabase.UI_TRIM);
    }

//    public RadialMenuObject(Color baseColor, Color dimColor) {
//        this.baseColor = baseColor;
//        this.dimColor = dimColor;
//
//        ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
//                .withLayer(MapMarksTextureDatabase.RADIAL_BASE.getTexture(), this.baseColor)
//                .withLayer(MapMarksTextureDatabase.RADIAL_TRIM.getTexture(), ColorDatabase.UI_TRIM);
//    }

    public ColorEnum getColor() {
        return color;
    }

//    public Color getBaseColor() {
//        return baseColor;
//    }
//
//    public void setBaseColor(Color baseColor) {
//        this.baseColor = baseColor;
//        this.ltw.withLayerColor(0, baseColor);
//    }

    public void setDimmed(boolean val) {
        if (isDimmed != val) {
            this.isDimmed = val;

            if (isDimmed) {
                this.ltw.withLayerColor(0, dimColor);
            }
            else {
                this.ltw.withLayerColor(0, baseColor);
            }
        }
    }

    @Override public float getContentWidth() { return WIDTH; }
    @Override public float getContentHeight() { return HEIGHT; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        ltw.render(sb);
    }

    @Override
    public RadialMenuObject anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        ltw.anchoredAt(x, y, anchorPosition, movementSpeed);
        return this;
    }
}
