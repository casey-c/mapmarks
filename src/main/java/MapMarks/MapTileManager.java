package MapMarks;

import MapMarks.ui.MapTile;
import MapMarks.utils.ColorDatabase;
import MapMarks.utils.SoundHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.map.MapRoomNode;
import easel.ui.AnchorPosition;
import easel.ui.debug.DebugWidget;
import easel.utils.EaselGraphicsHelper;
import easel.utils.EaselSoundHelper;
import easel.utils.colors.EaselColors;

import java.util.HashMap;

public class MapTileManager {
    private enum RoomType {
        MONSTER, ELITE, EVENT, BOSS, SHOP, TREASURE, REST, UNKNOWN_TYPE;

        static RoomType fromSymbol(String symbol) {
            if (symbol == null)
                return UNKNOWN_TYPE;

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

        boolean isHighlighted = false;

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
            //tileObject.tile.anchoredAt(x + 67.0f * Settings.xScale, y + 60.0f * Settings.yScale, AnchorPosition.CENTER);
            tileObject.tile.anchoredAt(x - 5, y - 13, AnchorPosition.LEFT_BOTTOM);

            if (tileObject.isHighlighted)
                tileObject.tile.render(sb);
        }

//        EaselGraphicsHelper.drawRect(sb, x, y, 64, 64, DebugWidget.DEBUG_COLOR_0);
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
            // Changing highlight status always succeeds
            if (inbounds.isHighlighted != val) {
                inbounds.isHighlighted = val;
                inbounds.tile.setBaseColor(highlightingColor);
                SoundHelper.playMapScratchSound();
            }
            // Already highlighted has further attempts to highlight: only allow if there is a color change
            // TODO: config option? [enable instant repaint]
            else if (val && isARepaint()) {
                //else if (val && inbounds.tile.getBaseColor() != highlightingColor) {
                inbounds.tile.setBaseColor(highlightingColor);
                SoundHelper.playMapScratchSound();
            }
        }
    }

    /**
     * @return whether the inbounds tile has a different color than the highlighting color
     */
    public static boolean isARepaint() {
        if (inbounds != null) {
            return inbounds.tile.getBaseColor() != highlightingColor;
        }
        return false;
    }

    private static Color highlightingColor = EaselColors.withOpacity(ColorDatabase.DEFAULT_RED, 0.2f);

    public static void setHighlightingColor(Color color) {
        highlightingColor = color;
    }

    public static void clearAllHighlights() {
        for (MapTileMapObject obj : tracked.values()) {
            obj.isHighlighted = false;
        }
    }

    private static boolean hasHighlightedType(RoomType type) {
//        System.out.println("Checking all highlights of type " + type);
        for (MapTileMapObject obj : tracked.values()) {
//            System.out.println("  Checking obj " + obj.type + ", isH " + obj.isHighlighted);
            if (obj.type == type && obj.isHighlighted)
                return true;
        }
        return false;
    }

    public static boolean hasHighlightEvent() {
        return hasHighlightedType(RoomType.EVENT);
    }

    public static boolean hasHighlightMerchant() {
        return hasHighlightedType(RoomType.SHOP);
    }

    public static boolean hasHighlightTreasure() {
        return hasHighlightedType(RoomType.TREASURE);
    }

    public static boolean hasHighlightRest() {
        return hasHighlightedType(RoomType.REST);
    }

    public static boolean hasHighlightEnemy() {
        return hasHighlightedType(RoomType.MONSTER);
    }

    public static boolean hasHighlightElite() {
        return hasHighlightedType(RoomType.ELITE);
    }

    // --------------------------------------------------------------------------------

    private static void highlightAllType(boolean val, RoomType type) {
        // Spaghetti code to put this here but oh well
        if (val)
            EaselSoundHelper.uiClick1();
        else
            EaselSoundHelper.uiClick2();

//        System.out.println("Highlight all highlights of type " + type + " to " + val);
        for (MapTileMapObject obj : tracked.values()) {
//            System.out.println("  Checking " + obj.type + ", isH: " + obj.isHighlighted);
            if (obj.type == type) {
                obj.isHighlighted = val;
                obj.tile.setBaseColor(highlightingColor);
            }
        }
    }

    public static void highlightAllEvents(boolean val) {
        highlightAllType(val, RoomType.EVENT);
    }

    public static void highlightAllMerchant(boolean val) {
        highlightAllType(val, RoomType.SHOP);
    }

    public static void highlightAllTreasure(boolean val) {
        highlightAllType(val, RoomType.TREASURE);
    }

    public static void highlightAllRest(boolean val) {
        highlightAllType(val, RoomType.REST);
    }

    public static void highlightAllEnemy(boolean val) {
        highlightAllType(val, RoomType.MONSTER);
    }

    public static void highlightAllElite(boolean val) {
        highlightAllType(val, RoomType.ELITE);
    }

//        this.items.add(new LegendItem(TEXT[6], ImageMaster.MAP_NODE_TREASURE, TEXT[7], TEXT[8], 2));
//        this.items.add(new LegendItem(TEXT[9], ImageMaster.MAP_NODE_REST, TEXT[10], TEXT[11], 3));
//        this.items.add(new LegendItem(TEXT[12], ImageMaster.MAP_NODE_ENEMY, TEXT[13], TEXT[14], 4));
//        this.items.add(new LegendItem(TEXT[15], ImageMaster.MAP_NODE_ELITE, TEXT[16], TEXT[17], 5));
    public static void clear() {
        tracked.clear();
    }
}
