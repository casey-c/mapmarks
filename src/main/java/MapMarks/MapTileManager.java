package MapMarks;

import MapMarks.ui.tiles.LargeMapTile;
import MapMarks.ui.tiles.SmallMapTile;
import MapMarks.utils.ColorDatabase;
import MapMarks.utils.SoundHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import easel.ui.AnchorPosition;
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
        private SmallMapTile smallTile;
        private LargeMapTile largeTile;

        RoomType type;

        boolean isHighlighted = false;

        public MapTileMapObject(MapRoomNode node) {
            this.type = RoomType.fromSymbol(node.getRoomSymbol(true));

            this.smallTile = new SmallMapTile();
            this.largeTile = new LargeMapTile();
        }
    }

    private static HashMap<MapRoomNode, MapTileMapObject> tracked = new HashMap<>();

    // TODO: lists for each type, to make accessing all of them of a particular type instant (e.g. using the legend)

    public static void track(MapRoomNode node) {
        tracked.put(node, new MapTileMapObject(node));
    }

    public static boolean shouldRenderLarge(MapRoomNode node) {
        // If current map node (we're already here), renders small
        if (node.equals(AbstractDungeon.getCurrMapNode()))
            return false;

        // If we're choosing the next room and the given node can be clicked on, renders large
        boolean completedRoom = AbstractDungeon.getCurrRoom().phase.equals(AbstractRoom.RoomPhase.COMPLETE);
        boolean normalConnection = AbstractDungeon.getCurrMapNode().isConnectedTo(node);

        // TODO: winged boots requires special handling (need to check relic counter as well, skipping for now)
        //boolean wingedConnection = AbstractDungeon.getCurrMapNode().wingedIsConnectedTo(node);

//        boolean completedRoomAndConnectable =  AbstractDungeon.getCurrRoom().phase.equals(AbstractRoom.RoomPhase.COMPLETE) && (normalConnection || wingedConnection);

        if (completedRoom && normalConnection)
            return true;

        // If the node's hb is hovered, renders large
        if (node.hb.hovered)
            return true;

        // If we're on the floor 0 of an act (i.e. haven't picked on any map nodes yet for this generated map), all bottom floors should be pickable,
        // and thus should render large
        if (!AbstractDungeon.firstRoomChosen && node.y == 0 && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE)
            return true;

        // If we're hovered on the legend item for this node, should render large
        if (AbstractDungeon.dungeonMapScreen.map.legend.isIconHovered(node.getRoomSymbol(true)))
            return true;

        // In case we missed some other cases, we'll just render it small for now (might want to make the fallback render large though)
        return false;
    }

    public static boolean isNodeHighlighted(MapRoomNode node) {
        MapTileMapObject tileObject = tracked.get(node);
        if (tileObject != null)
            return tileObject.isHighlighted;
        else
            return false;
    }

    public static Color getHighlightedNodeColor(MapRoomNode node) {
        MapTileMapObject tileObject = tracked.get(node);

        if (tileObject != null) {
            return tileObject.smallTile.getBaseColor();
        }
        else {
            return Color.WHITE;
        }
    }

    public static void tryRender(SpriteBatch sb, MapRoomNode node, float x, float y) {
        MapTileMapObject tileObject = tracked.get(node);
        if (tileObject != null) {
            //tileObject.tile.anchoredAt(x + 67.0f * Settings.xScale, y + 60.0f * Settings.yScale, AnchorPosition.CENTER);

            // TODO: currently determining whether to render the small or large version of the tile based on the current scale
            //   which is probably NOT a good solution and unstable due to things like lerp snapping between update frames.
            //update: can confirm that scale is NOT usable
            //
            //   Instead: should consider a better solution that looks at the same data that goes into the scale in the first place
            //     (e.g. the legend item's hitbox being hovered, the item itself being hovered, the node being a possible next floor etc.)

            // note: that we're always anchoring, even when not rendering. this is probably bad, but currently needs
            // to be done (i assume - it's old code i'm deciphering) because of updateAllTracked() looking at the bounds
            // TODO: smarter offsets (i've been taking them at complete random and eyeballing them, i should think about what they actually mean)
            tileObject.largeTile.anchoredAt(x - 33, y - 13 - 21, AnchorPosition.LEFT_BOTTOM);
            tileObject.smallTile.anchoredAt(x - 5, y - 13, AnchorPosition.LEFT_BOTTOM);

            // Render either the large or small tile based on the scale
            if (tileObject.isHighlighted) {
                if (shouldRenderLarge(node))
                    tileObject.largeTile.render(sb);
                else
                    tileObject.smallTile.render(sb);
            }
        }

//        EaselGraphicsHelper.drawRect(sb, x, y, 64, 64, DebugWidget.DEBUG_COLOR_0);
    }

    private static MapTileMapObject inbounds = null;

    public static void updateAllTracked() {
        inbounds = null;

        for (MapTileMapObject obj : tracked.values()) {
            obj.smallTile.update();
            obj.largeTile.update();

            // TODO: might need to verify that just checking against the small tile is fine here
            if (obj.smallTile.isMouseInContentBounds()) {
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

                inbounds.smallTile.setBaseColor(highlightingColor);
                inbounds.largeTile.setBaseColor(highlightingColor);

                SoundHelper.playMapScratchSound();
            }
            // Already highlighted has further attempts to highlight: only allow if there is a color change
            // TODO: config option? [enable instant repaint]
            else if (val && isARepaint()) {
                //else if (val && inbounds.tile.getBaseColor() != highlightingColor) {
                inbounds.smallTile.setBaseColor(highlightingColor);
                inbounds.largeTile.setBaseColor(highlightingColor);

                SoundHelper.playMapScratchSound();
            }
        }
    }

    /**
     * @return whether the inbounds tile has a different color than the highlighting color
     */
    public static boolean isARepaint() {
        // TODO: verify that only the small tile is needed here
        if (inbounds != null) {
            return inbounds.smallTile.getBaseColor() != highlightingColor;
        }
        return false;
    }

    private static Color highlightingColor = EaselColors.withOpacity(ColorDatabase.DEFAULT_RED, 0.8f);

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

                obj.smallTile.setBaseColor(highlightingColor);
                obj.largeTile.setBaseColor(highlightingColor);
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
