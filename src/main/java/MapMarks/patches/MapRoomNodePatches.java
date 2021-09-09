package MapMarks.patches;

import MapMarks.MapTileManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import easel.utils.EaselGraphicsHelper;
import easel.utils.colors.EaselColors;

public class MapRoomNodePatches {

//    @SpirePatch(
//            clz = MapRoomNode.class,
//            method = SpirePatch.CONSTRUCTOR
//    )
//    public static class MapRoomNodeConstructorPatch {
//        @SpirePostfixPatch
//        public static void Postfix(MapRoomNode node, int x, int y) {
//            // TODO: construct one MapTileNode (my ui) for each MapRoomNode (the base game object)
//            // these need to be tracked somewhere and cleared appropriately
//            //
//            // potentially also in MapGenerator::createNodes
//
//            // Not special starting floors or boss floors
//            if (x >= 0 && y >= 0) {
//                MapTileManager.track(node);
//            }
//
//            CardCrawlGame.dungeon.getMap();
//        }
//    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateMap"
    )
    public static class PostGenerateDungeonPatch {
        public static void Postfix() {
            MapTileManager.clear();

            AbstractDungeon.map.forEach(list -> list.forEach(node -> {
                if (node.x >= 0 && node.y >= 0)
                    MapTileManager.track(node);
            }));
        }
    }

    @SpirePatch(
            clz = TheEnding.class,
            method = "generateSpecialMap"
    )
    public static class PostEndingGenerateDungeonPatch {
        public static void Postfix() {
            MapTileManager.clear();

            AbstractDungeon.map.forEach(list -> list.forEach(node -> {
                if (node.x >= 0 && node.y >= 0)
                    MapTileManager.track(node);
            }));
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class MapRoomNodeUpdatePatch {
        @SpirePostfixPatch
        public static void Postfix(MapRoomNode node) {
            // use this node as a key into our UI storage map, and update the map tile accordingly.
            // if this node's node.hb is hovered, disable the radial menu from being openable, start tracking global rightclick/drags
        }
    }


    @SpirePatch(
            clz = MapRoomNode.class,
            method = "render"
    )
    public static class MapRoomNodeRenderPatch {
        private static final int IMG_WIDTH = (int) (Settings.xScale * 64.0f);
        private static final float SPACING_X = Settings.isMobile ? (float) IMG_WIDTH * 2.2f : (float) IMG_WIDTH * 2.0f;
        private static final float OFFSET_X = Settings.isMobile ? 496.0f * Settings.xScale : 560.0f * Settings.xScale;
        private static final float OFFSET_Y = 180.0f * Settings.scale;

        public static float computeXFromNode(MapRoomNode node) {
            //return (node.x * SPACING_X + OFFSET_X - 64.0f + node.offsetX) + 64.0f * Settings.scale + (32.0f * Settings.scale);
            //return (node.x * SPACING_X + OFFSET_X - 64.0f + node.offsetX) + 64.0f * Settings.scale;
            //return (node.x * SPACING_X + OFFSET_X - 64.0f + node.offsetX);
            return (node.x * SPACING_X + OFFSET_X + node.offsetX) / Settings.xScale - 64.0f;

//            sb.draw(this.room.getMapImg(),
//                    (float)this.x * SPACING_X + OFFSET_X - 64.0f + this.offsetX,
//                    (float)this.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + this.offsetY,
//                    64.0f,
//                    64.0f,
//                    128.0f,
//                    128.0f,
//                    this.scale * Settings.scale,
//                    this.scale * Settings.scale,
//                    0.0f,
//                    0,
//                    0,
//                    128,
//                    128,
//                    false,
//                    false);
        }

        public static float computeYFromNode(MapRoomNode node) {
            //return (node.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + node.offsetY) + 64.0f * Settings.scale;
            //return (node.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + node.offsetY);

            return (node.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + node.offsetY) / Settings.yScale - 64.0f;
//            return (node.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + node.offsetY) / Settings.scale - 64.0f;

            //return (node.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + node.offsetY) + 64.0f * Settings.scale + (32.0f * Settings.scale);
        }

        @SpirePostfixPatch
        public static void Postfix(MapRoomNode node, SpriteBatch sb) {
//            EaselGraphicsHelper.drawRect(sb, computeXFromNode(node) / Settings.xScale, computeYFromNode(node) / Settings.yScale, 64.0f, 64.0f, EaselColors.withOpacity(EaselColors.HEADER_BLUE(), 0.2f));

//            EaselGraphicsHelper.drawRect(sb, computeXFromNode(node) / Settings.scale + 45.0f, computeYFromNode(node) / Settings.scale + 45.0f, 64.0f, 64.0f, EaselColors.withOpacity(EaselColors.HEADER_GREEN(), 0.2f));

//            EaselGraphicsHelper.drawRect(sb, computeXFromNode(node) / Settings.scale, computeYFromNode(node) / Settings.scale, 128.0f * Settings.scale, 128.0f * Settings.scale, EaselColors.withOpacity(EaselColors.HEADER_PURPLE(), 0.2f));

            // x and y definitely need to be xScale, yScale
            // the offset is not correct and is probably resolution dependent

            MapTileManager.tryRender(sb,
                    node,
//                    computeXFromNode(node) / Settings.xScale + 0.0f,
//                    computeYFromNode(node) / Settings.yScale + 0.0f * Settings.scale / Settings.yScale);
                    computeXFromNode(node) + 32.0f,
                    computeYFromNode(node) + 32.0f
            );
        }
//            // TODO: render just before this line (near the start of MapRoomNode::render)
//            //   [will need an instrument patch probably]
//            // this.renderEmeraldVfx(sb);
//
//
//            // TODO: scaling here is probably incorrect
////            MapTileManager.tryRender(sb, node, x / Settings.xScale, y / Settings.scale);
//
////            sb.draw(this.room.getMapImgOutline(),
////                    (float)this.x * SPACING_X + OFFSET_X - 64.0f + this.offsetX,
////                    (float)this.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + this.offsetY,
////                    64.0f,
////                    64.0f,
////                    128.0f,
////                    128.0f,
////                    this.scale * Settings.scale,
////                    this.scale * Settings.scale,
////                    0.0f,
////                    0,
////                    0,
////                    128,
////                    128,
////                    false,
////                    false);
//        }

//        private static final String replacement =
//                "{ "
////                        + "$1 = "
////                        + "float tileX = " + MapRoomNodeRenderPatch.class.getName() + ".computeXFromNode(this);"
////                        + "float tileY = " + MapRoomNodeRenderPatch.class.getName() + ".computeYFromNode(this);"
////                        + MapTileManager.class.getName() + ".tryRender(sb, this, tileX / Settings.xScale, tileY / Settings.scale);"
////                        + MapTileManager.class.getName() + ".tryRender(sb, this, MapRoomNodeRenderPatch.computeXFromNode(this) / Settings.xScale, MapRoomNodeRenderPatch.computeYFromNode(this) / Settings.scale);"
//                        + MapTileManager.class.getName() + ".tryRender(sb, this, " + MapRoomNodeRenderPatch.class.getName() + ".computeXFromNode(this) / " + Settings.class.getName() + ".xScale, " + MapRoomNodeRenderPatch.class.getName() + ".computeYFromNode(this) / " + Settings.class.getName() + ".scale);"
//                        + "$_ = $proceed($$);"
//                        + " }";
//
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                @Override
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().equals(MapRoomNode.class.getName()) && m.getMethodName().equals("renderEmeraldVfx")) {
//                        m.replace(replacement);
//                    }
//                }
//            };
//        }
    }
}
