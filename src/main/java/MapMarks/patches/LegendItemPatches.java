package MapMarks.patches;

import MapMarks.MapTileManager;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.LegendItem;
import easel.utils.EaselSoundHelper;

import java.util.HashMap;

public class LegendItemPatches {
    public static HashMap<LegendItem, LegendItemHandler> handler = new HashMap<>();

    private static class LegendItemHandler {
        Runnable rightClickCallback;

        boolean isRightClickStarted = false;
        boolean isRightClickBlocked = false;

        public LegendItemHandler(Runnable callback) {
            this.rightClickCallback = callback;
        }
    }

    @SpirePatch(
            clz = LegendItem.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class LegendItemConstructorPatch {
        @SpirePostfixPatch
        public static void Postfix(LegendItem item, String _label, Texture _img, String _tipHeader, String _tipBody, int index) {
//    public LegendItem(String label, Texture img, String tipHeader, String tipBody, int index) {
            if (index == 0) {
                handler.put(item, new LegendItemHandler(() -> {
                    if (MapTileManager.hasHighlightEvent())
                        MapTileManager.highlightAllEvents(false);
                    else
                        MapTileManager.highlightAllEvents(true);
                }));
            } else if (index == 1) {
                handler.put(item, new LegendItemHandler(() -> {
                    if (MapTileManager.hasHighlightMerchant())
                        MapTileManager.highlightAllMerchant(false);
                    else
                        MapTileManager.highlightAllMerchant(true);
                }));
            } else if (index == 2) {
                handler.put(item, new LegendItemHandler(() -> {
                    if (MapTileManager.hasHighlightTreasure())
                        MapTileManager.highlightAllTreasure(false);
                    else
                        MapTileManager.highlightAllTreasure(true);
                }));
            } else if (index == 3) {
                handler.put(item, new LegendItemHandler(() -> {
                    if (MapTileManager.hasHighlightRest())
                        MapTileManager.highlightAllRest(false);
                    else
                        MapTileManager.highlightAllRest(true);
                }));
            } else if (index == 4) {
                handler.put(item, new LegendItemHandler(() -> {
                    if (MapTileManager.hasHighlightEnemy())
                        MapTileManager.highlightAllEnemy(false);
                    else
                        MapTileManager.highlightAllEnemy(true);
                }));
            } else if (index == 5) {
                handler.put(item, new LegendItemHandler(() -> {
                    if (MapTileManager.hasHighlightElite())
                        MapTileManager.highlightAllElite(false);
                    else
                        MapTileManager.highlightAllElite(true);
                }));
            }
//            this.items.add(new LegendItem(TEXT[0], ImageMaster.MAP_NODE_EVENT, TEXT[1], TEXT[2], 0));
//            this.items.add(new LegendItem(TEXT[3], ImageMaster.MAP_NODE_MERCHANT, TEXT[4], TEXT[5], 1));
//            this.items.add(new LegendItem(TEXT[6], ImageMaster.MAP_NODE_TREASURE, TEXT[7], TEXT[8], 2));
//            this.items.add(new LegendItem(TEXT[9], ImageMaster.MAP_NODE_REST, TEXT[10], TEXT[11], 3));
//            this.items.add(new LegendItem(TEXT[12], ImageMaster.MAP_NODE_ENEMY, TEXT[13], TEXT[14], 4));
//            this.items.add(new LegendItem(TEXT[15], ImageMaster.MAP_NODE_ELITE, TEXT[16], TEXT[17], 5));
        }
    }

    @SpirePatch(
            clz = LegendItem.class,
            method = "update"
    )
    public static class LegendItemUpdatePatch {
        public static void Postfix(LegendItem item) {
            if (CardCrawlGame.isInARun() && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
                LegendItemHandler h = handler.get(item);

                if (h == null)
                    return;

                if (item.hb.hovered) {
                    if (InputHelper.isMouseDown_R && !h.isRightClickStarted) {
                        h.isRightClickStarted = true;
                    } else if (!InputHelper.isMouseDown_R && h.isRightClickStarted) {
                        h.isRightClickStarted = false;

                        // Click finished
                        if (!h.isRightClickBlocked) {
                            h.rightClickCallback.run();
//                            EaselSoundHelper.cawCaw();
                        }
                        else {
                            h.isRightClickBlocked = false;
                        }
                    }
                }
                // Not hovered
                else {
                    h.isRightClickStarted = false;

                    if (InputHelper.isMouseDown_R)
                        h.isRightClickBlocked = true;
                    else
                        h.isRightClickBlocked = false;
                }

            }
        }
    }
}
