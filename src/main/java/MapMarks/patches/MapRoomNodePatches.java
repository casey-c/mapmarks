package MapMarks.patches;

import MapMarks.MapTileManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MapRoomNodePatches {

    @SpirePatch(
            clz = MapRoomNode.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class MapRoomNodeConstructorPatch {
        @SpirePostfixPatch
        public static void Postfix(MapRoomNode node, int x, int y) {
            // TODO: construct one MapTileNode (my ui) for each MapRoomNode (the base game object)
            // these need to be tracked somewhere and cleared appropriately
            //
            // potentially also in MapGenerator::createNodes

            // Not special starting floors or boss floors
            if (x >= 0 && y >= 0) {
                MapTileManager.track(node);
            }
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
            return node.x * SPACING_X + OFFSET_X - 64.0f + node.offsetX;
        }

        public static float computeYFromNode(MapRoomNode node) {
            return node.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 64.0f + node.offsetY;
        }

        @SpirePostfixPatch
        public static void Postfix(MapRoomNode node, SpriteBatch sb) {
            MapTileManager.tryRender(sb,
                    node,
                    computeXFromNode(node) / Settings.xScale,
                    computeYFromNode(node) / Settings.scale);
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
