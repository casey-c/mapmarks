package MapMarks.ui;

import MapMarks.utils.ColorDatabase;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;
import easel.utils.EaselInputHelper;
import easel.utils.colors.EaselColors;

import java.util.function.Consumer;

public class LegendObject extends AbstractWidget<LegendObject> {
    private static final float WIDTH = 70;
    private static final float HEIGHT = 70;

    private static Color SHADOW_COLOR = Color.valueOf("94abb2ff");

    private static Color INVIS_COLOR = Color.valueOf("ffffff00");
    private static Color DIM_COLOR = Color.valueOf("ffffff47");
    private static Color HIGHLIGHT_COLOR = Color.valueOf("ffffff3d");

//    private Color baseColor = ColorDatabase.DEFAULT_RED;
    private ColorEnum color = ColorEnum.RED;
    private float previousAlpha = 0.0f;

    private LayeredTextureWidget ltw;

    public LegendObject() {
        ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
                .withLayer(MapMarksTextureDatabase.LEGEND_SHADOW.getTexture(), SHADOW_COLOR)
                .withLayer(MapMarksTextureDatabase.LEGEND_BASE.getTexture(), color.get())
                .withLayer(MapMarksTextureDatabase.LEGEND_DIM.getTexture(), INVIS_COLOR)
                .withLayer(MapMarksTextureDatabase.LEGEND_HIGHLIGHT.getTexture(), INVIS_COLOR)
                .withLayer(MapMarksTextureDatabase.LEGEND_TRIM.getTexture(), ColorDatabase.UI_TRIM);

        this.onRightMouseDown(me -> me.setDimHighlight(DIM_COLOR, INVIS_COLOR));
        this.onRightMouseUp(me -> me.setDimHighlight(INVIS_COLOR, HIGHLIGHT_COLOR));

        this.onMouseEnter(me -> me.setDimHighlight(INVIS_COLOR, HIGHLIGHT_COLOR));
        this.onMouseLeave(me -> me.setDimHighlight(INVIS_COLOR, INVIS_COLOR));
    }

    private void setDimHighlight(Color dim, Color highlight) {
        this.ltw.withLayerColor(2, dim);
        this.ltw.withLayerColor(3, highlight);
    }

    @Override public float getContentWidth() { return WIDTH; }
    @Override public float getContentHeight() { return HEIGHT; }

    public ColorEnum getColor() { return color; }

    @Override
    public LegendObject anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        ltw.anchoredAt(x, y, anchorPosition, movementSpeed);
        return this;
    }

    // TODO: could be a bug if we set the radial menu item before the thing is fully faded in maybe? may be perma dim (not thinking too hard here)
    public void setAlphaFromLegend(float alpha) {
        if (previousAlpha != alpha) {
            ltw.withLayerColor(1, EaselColors.withOpacity(color.get(), alpha));
            previousAlpha = alpha;
        }
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        ltw.render(sb);
    }

    public void setColor(ColorEnum color) {
        this.color = color;
        ltw.withLayerColor(1, this.color.get());
    }
}
