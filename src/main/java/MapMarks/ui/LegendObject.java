package MapMarks.ui;

import MapMarks.utils.ColorDatabase;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;
import easel.utils.colors.EaselColors;

public class LegendObject extends AbstractWidget<LegendObject> {
    private static final float WIDTH = 50;
    private static final float HEIGHT = 50;

    private Color color = ColorDatabase.DEFAULT_RED;
    private float previousAlpha = 0.0f;

    private LayeredTextureWidget ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
            .withLayer(MapMarksTextureDatabase.LEGEND_BASE.getTexture(), color)
            .withLayer(MapMarksTextureDatabase.LEGEND_TRIM.getTexture(), ColorDatabase.UI_TRIM);

    @Override
    public float getContentWidth() {
        return WIDTH;
    }

    @Override
    public float getContentHeight() {
        return HEIGHT;
    }

    @Override
    public LegendObject anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        ltw.anchoredAt(x, y, anchorPosition, movementSpeed);
        return this;
    }

    // TODO: could be a bug if we set the radial menu item before the thing is fully faded in maybe? may be perma dim (not thinking too hard here)
    public void setAlphaFromLegend(float alpha) {
        if (previousAlpha != alpha) {
            ltw.withLayerColor(0, EaselColors.withOpacity(color, alpha));
            previousAlpha = alpha;
        }
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        ltw.render(sb);
    }

    public void setColor(Color color) {
        this.color = color;
        ltw.withLayerColor(0, this.color);
    }
}
