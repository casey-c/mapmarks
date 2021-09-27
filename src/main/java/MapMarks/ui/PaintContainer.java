package MapMarks.ui;

import MapMarks.MapMarks;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.utils.EaselInputHelper;

import java.util.ArrayList;

public class PaintContainer extends AbstractWidget<PaintContainer> {
    private static final Texture TEX_CIRCLE = MapMarksTextureDatabase.PAINT_CIRCLE.getTexture();
    private static final float SCALE = 18.0f;

    private static class PaintBlob {
        float x;
        float y;
        float dungeonMapOffsetY;

        Color color;

        public PaintBlob(float x, float y, float dungeonMapOffsetY, Color color) {
            this.x = x;
            this.y = y;
            this.dungeonMapOffsetY = dungeonMapOffsetY;

            this.color = color;

//            sb.draw(this.room.getMapImgOutline(),
//                    (float)this.x * SPACING_X + OFFSET_X - 64.0f + this.offsetX,
//                    (float)this.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + this.offsetY,
//                    64.0f,
//                    64.0f,
//                    128.0f,
//                    128.0f,
//                    this.scale * Settings.scale * 2.0f,
//                    this.scale * Settings.scale * 2.0f,
//                    0.0f,
//                    0,
//                    0,
//                    128,
//                    128,
//                    false,
//                    false);
        }

        public void render(SpriteBatch sb, float currentDungeonMapOffsetY) {
            sb.setColor(color);

            // TODO, these are all temporary and incorrect
            float sx = x;
            float sy = y - (dungeonMapOffsetY - currentDungeonMapOffsetY);

            sb.draw(TEX_CIRCLE,
                    sx * Settings.xScale,
                    sy * Settings.yScale,
                    SCALE * Settings.xScale,
                    SCALE * Settings.yScale);
        }
    }

    private ArrayList<PaintBlob> blobs = new ArrayList<>();

    public PaintContainer() {
        anchoredAt(0, 0, AnchorPosition.LEFT_BOTTOM);
    }

    private void addBlob(Color color) {
        blobs.add(new PaintBlob(EaselInputHelper.getMouseX(),
                EaselInputHelper.getMouseY(),
                DungeonMapScreen.offsetY,
                color));
    }

    public void clear() {
        blobs.clear();
    }

    @Override public float getContentWidth() { return Settings.WIDTH; }
    @Override public float getContentHeight() { return Settings.HEIGHT; }

    private long previousUpdateTime = 0;
    private static final long UPDATE_THRESHOLD_MS = 0;

    @Override
    protected void updateWidget() {
        if (CardCrawlGame.isInARun() && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            // Only active if we're in a run, on the map screen, and have ALT+RIGHT_CLICK pressed
            if (!EaselInputHelper.isAltPressed() || !InputHelper.isMouseDown_R)
                return;

            long currTime = System.currentTimeMillis();

            if (currTime - previousUpdateTime > UPDATE_THRESHOLD_MS) {
                previousUpdateTime = currTime;

                // Enough time has passed since previous update, so we can draw a blob
                addBlob(MapMarks.legendObject.getColor().get());
            }
        }
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        if (CardCrawlGame.isInARun() && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            float currOffset = DungeonMapScreen.offsetY;

            for (PaintBlob blob : blobs) {
                blob.render(sb, currOffset);
            }
        }
    }
}
