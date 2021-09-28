package MapMarks.patches;

import MapMarks.MapMarks;
import MapMarks.ui.LegendObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.Legend;
import easel.ui.AnchorPosition;

public class LegendPatches {
//    // Anchors the legend object into position
//    @SpirePatch(
//            clz = Legend.class,
//            method = SpirePatch.CONSTRUCTOR
//    )
//    public static class LegendConstructorPatch {
//        @SpirePostfixPatch
//        public static void Postfix(Legend _legend) {
//            MapMarks.legendObject = new LegendObject()
//                    .anchoredAt(1575, 765, AnchorPosition.CENTER);
//        }
//    }

    // Renders the legend object
    @SpirePatch(
            clz = Legend.class,
            method = "render"
    )
    public static class LegendObjectPatch {
        @SpirePostfixPatch
        public static void Postfix(Legend legend, SpriteBatch sb) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            //if (legend.c.a >= 0.8f && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
                MapMarks.legendObject.setAlphaFromLegend(legend.c.a);
                MapMarks.legendObject.render(sb);
            }
        }
    }

    // Updates the legend object
    @SpirePatch(
            clz = Legend.class,
            method = "update"
    )
    public static class LegendUpdatePatch {
        @SpirePostfixPatch
        public static void Postfix(Legend _legend, float mapAlpha, boolean isMapScreen) {
            // TODO: probably don't need to actually copy the game code's check here
            if (mapAlpha >= 0.8f && isMapScreen) {
                MapMarks.legendObject.update();
            }
        }
    }

    // Renders the paint container
    @SpirePatch(
            clz = Legend.class,
            method = "render"
    )
    public static class LegendRenderPaintPatch {
        @SpirePostfixPatch
        public static void Postfix(Legend _legend, SpriteBatch sb) {
            MapMarks.paintContainer.render(sb);
        }
    }
}
