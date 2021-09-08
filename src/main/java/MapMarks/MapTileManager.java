package MapMarks;

import MapMarks.ui.MapTile;
import MapMarks.utils.SoundHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.map.MapRoomNode;
import easel.ui.AnchorPosition;
import easel.utils.EaselSoundHelper;

import java.util.HashMap;

public class MapTileManager {
    private enum RoomType {
        MONSTER, ELITE, EVENT, BOSS, SHOP, TREASURE, REST, UNKNOWN_TYPE;

        static RoomType fromSymbol(String symbol) {
            if (symbol.equals("M"))
                return MONSTER;
            else if (symbol.equals("?"))
                return EVENT;
            else if (symbol.equals("B"))
                return BOSS;
            else if (symbol.equals("E"))
                return ELITE;
            else if (symbol.equals("R"))
                return REST;
            else if (symbol.equals("$"))
                return SHOP;
            else if (symbol.equals("T"))
                return TREASURE;
            else
                return UNKNOWN_TYPE;
        }
    }

    private static class MapTileMapObject {
        MapTile tile;
        RoomType type;

        boolean isHighlighted = true;

        public MapTileMapObject(MapRoomNode node) {
            this.type = RoomType.fromSymbol(node.getRoomSymbol(true));

            this.tile = new MapTile()
//                    .onMouseEnter(tile -> {
//                                EaselSoundHelper.uiClick1();
//                            }
//                    )
//                    .onMouseLeave(tile -> {
//                                EaselSoundHelper.uiClick2();
//                            }
//                    )
            ;
        }
    }

    private static HashMap<MapRoomNode, MapTileMapObject> tracked = new HashMap<>();

    // TODO: lists for each type, to make accessing all of them of a particular type instant (e.g. using the legend)

    public static void track(MapRoomNode node) {
        tracked.put(node, new MapTileMapObject(node));
    }

    public static void tryRender(SpriteBatch sb, MapRoomNode node, float x, float y) {
        MapTileMapObject tileObject = tracked.get(node);
        if (tileObject != null) {
            tileObject.tile.anchoredAt(x + 67.0f, y + 60.0f, AnchorPosition.CENTER);

            if (tileObject.isHighlighted)
                tileObject.tile.render(sb);
        }
    }

    private static MapTileMapObject inbounds = null;

    public static void updateAllTracked() {
        inbounds = null;

        for (MapTileMapObject obj : tracked.values()) {
            obj.tile.update();

            if (obj.tile.isMouseInContentBounds()) {
                inbounds = obj;
            }
        }
    }

    public static boolean isAnyTileHovered() {
        return inbounds != null;
    }

    public static boolean hoveredTileIsHighlighted() {
        return (inbounds != null && inbounds.isHighlighted);
    }

    public static void setHoveredTileHighlightStatus(boolean val) {
        if (inbounds != null) {
            if (inbounds.isHighlighted != val) {
                inbounds.isHighlighted = val;
                SoundHelper.playMapScratchSound();
            }
        }
    }

    private static Color highlightingColor;
    public static void setHighlightingColor(Color color) {
        highlightingColor = color;
    }
}
