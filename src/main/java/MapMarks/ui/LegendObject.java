package MapMarks.ui;

import MapMarks.utils.ColorDatabase;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
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

    public static final String TIP_HEADER = "Map Marks: Controls";
    public static final String TIP_BODY = "#gRight+Click a node to toggle its highlight. #gRight+Click+Drag to toggle many nodes at once. #gRight+Click the legend items to toggle all of a certain type. #gRight+Click this legend button to clear all highlights. NL NL #pRight+Click+Drag outside nodes to open a radial menu to select the color. NL NL #rALT+Right+Click to start painting with the current color. #rALT+Right+Click this legend button to clear all pen drawings. NL NL #bControl+Right+Click a node to clear all highlights from nodes unreachable to it (i.e. no path between another previously highlighted node and the current target).";
//    public static final String TIP_BODY = "#gRight+Click this legend button to clear all highlights. #gRight+Click a node to toggle highlights. #gRight+Click and drag to highlight/unhighlight many nodes at once. #gRight+Click outside nodes to open a radial menu to select the color. #gRight+Click the legend items (e.g. Elites, Treasure, etc.) to toggle all nodes of a certain type. NL NL #rALT+Right+Click to start painting with the current color. #rALT+Right+Click this legend button to clear all pen drawings. NL NL #bControl+Right+Click a node to clear all highlights from nodes unreachable to it (i.e. no path between another previously highlighted node and the current target). NL NL Note: Unreachable nodes due to pathing choices are hidden by default; hovering over this legend button will reveal highlighted but unreachable nodes.";
    public static final String TIP_HEADER_SUCCINCT = "Map Marks";
    public static final String TIP_BODY_SUCCINCT = "Hold #gShift to show instructions and controls for this mod.";

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

    @Override
    protected void updateWidget() {
        super.updateWidget();
        if (hb.hovered) {
            if (EaselInputHelper.isShiftPressed()) {
                TipHelper.renderGenericTip(1500.0f * Settings.xScale,
                        (getBottom() - 20.0f) * Settings.scale,
                        //270.0f * Settings.scale,
                        TIP_HEADER,
                        TIP_BODY);
            }
            else {
                TipHelper.renderGenericTip(1500.0f * Settings.xScale,
//                        (getBottom() - 20.0f) * Settings.scale,
                        270.0f * Settings.scale,
                        TIP_HEADER_SUCCINCT,
                        TIP_BODY_SUCCINCT);
            }
        }

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
