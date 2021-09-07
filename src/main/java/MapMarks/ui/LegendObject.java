package MapMarks.ui;

import MapMarks.utils.ColorDatabase;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;

public class LegendObject extends AbstractWidget<LegendObject> {
    private static final float WIDTH = 50;
    private static final float HEIGHT = 50;

    private LayeredTextureWidget ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
            .withLayer(MapMarksTextureDatabase.LEGEND_BASE.getTexture(), ColorDatabase.DEFAULT_RED)
            .withLayer(MapMarksTextureDatabase.LEGEND_TRIM.getTexture(), ColorDatabase.UI_TRIM)
            ;

    @Override public float getContentWidth() { return WIDTH; }
    @Override public float getContentHeight() { return HEIGHT; }

    @Override
    public LegendObject anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        ltw.anchoredAt(x, y, anchorPosition, movementSpeed);
        return this;
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        ltw.render(sb);
    }

    public void setColor(Color color) {
        ltw.withLayerColor(0, color);
    }
}
