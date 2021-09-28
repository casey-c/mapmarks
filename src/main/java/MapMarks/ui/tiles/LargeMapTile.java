package MapMarks.ui.tiles;


import MapMarks.utils.ColorDatabase;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;
import easel.utils.colors.EaselColors;

public class LargeMapTile extends AbstractWidget<LargeMapTile> {
    // Not statically defined because I'm probably going to mess with the tile images a ton still
    // and prefer to keep them dynamically pulled from the texture
    public final float WIDTH;
    public final float HEIGHT;

    private LayeredTextureWidget ltw;

//    private Color baseColor = EaselColors.withOpacity(ColorDatabase.DEFAULT_RED, 0.2f);
    private Color baseColor = ColorEnum.RED.get();

    private static final Color trimColor = ColorDatabase.UI_TRIM;

    public LargeMapTile() {
        Texture base = MapMarksTextureDatabase.LARGE_TILE_OUTER_BASE.getTexture();

        this.WIDTH = base.getWidth();
        this.HEIGHT = base.getHeight();

        this.ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
                .withLayer(MapMarksTextureDatabase.LARGE_TILE_SHADOW.getTexture())
                .withLayer(MapMarksTextureDatabase.LARGE_TILE_OUTER_BASE.getTexture(), baseColor)
                .withLayer(MapMarksTextureDatabase.LARGE_TILE_INNER_BASE.getTexture(), baseColor)
                .withLayer(MapMarksTextureDatabase.LARGE_TILE_TRIM.getTexture(), trimColor)
        ;

    }

    public void setBaseColor(Color baseColor) {
        if (this.baseColor != baseColor) {
            this.baseColor = baseColor;
            this.ltw.withLayerColor(1, baseColor);
            this.ltw.withLayerColor(2, baseColor);
        }
    }

    public Color getBaseColor() {
        return baseColor;
    }

    @Override public float getContentWidth() { return WIDTH; }
    @Override public float getContentHeight() { return HEIGHT; }

    @Override
    public LargeMapTile anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        ltw.anchoredAt(x, y, anchorPosition, movementSpeed);
        return this;
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        ltw.render(sb);

//        EaselGraphicsHelper.drawDebugRects(sb, this);
//        if (hb != null)
//            hb.render(sb);
    }
}
