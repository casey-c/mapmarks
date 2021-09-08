package MapMarks.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.map.MapRoomNode;

public class MapRoomNodePatches {

    @SpirePatch(
            clz = MapRoomNode.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class MapRoomNodeConstructorPatch {
        @SpirePostfixPatch
       public static void Postfix(MapRoomNode _node)  {
            // TODO: construct one MapTileNode (my ui) for each MapRoomNode (the base game object)
            // these need to be tracked somewhere and cleared appropriately
            //
            // potentially also in MapGenerator::createNodes
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
        @SpirePostfixPatch
        public static void Postfix(MapRoomNode _node, SpriteBatch sb) {
            // TODO: render just before this line (near the start of MapRoomNode::render)
            //   [will need an instrument patch probably]
            // this.renderEmeraldVfx(sb);
        }
    }
}
