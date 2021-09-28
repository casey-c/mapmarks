package MapMarks;

import MapMarks.ui.LegendObject;
import MapMarks.ui.PaintContainer;
import MapMarks.ui.RadialMenu;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.MapMarksTextureDatabase;
import MapMarks.utils.SoundHelper;
import basemod.BaseMod;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.LegendItem;
import easel.ui.AnchorPosition;
import easel.utils.EaselInputHelper;
import easel.utils.EaselSoundHelper;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpireInitializer
public class MapMarks implements PostInitializeSubscriber, PostUpdateSubscriber, RenderSubscriber, AddAudioSubscriber {
    public static final Logger logger = LogManager.getLogger(MapMarks.class);

    public static void initialize() {
        new MapMarks();
    }

    public MapMarks() {
        BaseMod.subscribe(this);
    }

    private RadialMenu menu;
    public static PaintContainer paintContainer;

    public static LegendObject legendObject;

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");
        TextureLoader.loadTextures(MapMarksTextureDatabase.values());
        //Easel.initialize();

        menu = new RadialMenu();
        legendObject = new LegendObject()
                .onRightClick(onClick -> {
                    EaselSoundHelper.uiClick2();

                    if (EaselInputHelper.isAltPressed()) {
                        paintContainer.clear();
                    }
                    else {
                        MapTileManager.clearAllHighlights();
                    }
                })
                .anchoredAt(1575, 767, AnchorPosition.CENTER)
        ;

        System.out.println("Settings.xScale: " + Settings.xScale);
        System.out.println("Settings.yScale: " + Settings.yScale);
        System.out.println("Settings.scale: " + Settings.scale);

        paintContainer = new PaintContainer();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        menu.render(sb);

//        paintContainer.render(sb);
    }

    private boolean rightMouseDown = false;
    private int previouslySelectedIndex = -1;

    private enum RightMouseDownMode {
        RADIAL_MENU, HIGHLIGHTING, UNHIGHLIGHTING, NONE;
    }

    private RightMouseDownMode rightMouseDownMode = RightMouseDownMode.NONE;


    @Override
    public void receivePostUpdate() {
        // No updates required if we're not on the map screen
        if (!CardCrawlGame.isInARun() || AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            rightMouseDownMode = RightMouseDownMode.NONE;
            return;
        }

        // Update tile manager
        MapTileManager.updateAllTracked();

        if (EaselInputHelper.isAltPressed()) {
            paintContainer.update();

            // Reset the remaining and quit early, since we don't want to highlight or open the radial in this mode
            rightMouseDownMode = RightMouseDownMode.NONE;
            rightMouseDown = false;

            if (menu.isMenuOpen())
                menu.close();

            return;
        }

        // Transition: Started right clicking
        if (InputHelper.isMouseDown_R && !rightMouseDown) {
            rightMouseDown = true;

            // Control will clear any unreachable from the node under the target
            if (EaselInputHelper.isControlPressed()) {
                if (MapTileManager.isAnyTileHovered()) {
                    MapTileManager.removeHighlightsFromUnreachableNodes();
                    MapTileManager.setHoveredTileHighlightStatus(true);
                    rightMouseDownMode = RightMouseDownMode.NONE;
                }
            }
            else if (MapTileManager.isAnyTileHovered()) {
                // Preexisting highlighted tile under cursor where we start clicking
                if (MapTileManager.hoveredTileIsHighlighted()) {
                    // Check if we're doing a repaint (TODO: config option)
                    if (MapTileManager.isARepaint()) {
                        // start highlighting everything, starting with this hovered unhighlighted node
                        rightMouseDownMode = RightMouseDownMode.HIGHLIGHTING;
                        MapTileManager.setHoveredTileHighlightStatus(true);
                    } else {
                        // start unhighlighting everything, starting with this hovered highlighted node
                        rightMouseDownMode = RightMouseDownMode.UNHIGHLIGHTING;
                        MapTileManager.setHoveredTileHighlightStatus(false);
                    }
                }
                // Tile under cursor exists, but is not highlighted
                else {
                    // start highlighting everything, starting with this hovered unhighlighted node
                    rightMouseDownMode = RightMouseDownMode.HIGHLIGHTING;
                    MapTileManager.setHoveredTileHighlightStatus(true);
                }
            } else {
                boolean okayToOpenRadial = !MapMarks.legendObject.isMouseInContentBounds();

                // Probably overly zealous null checking here for no reason
                if (CardCrawlGame.isInARun() && AbstractDungeon.dungeonMapScreen != null && AbstractDungeon.dungeonMapScreen.map != null && AbstractDungeon.dungeonMapScreen.map.legend != null) {
                    for (LegendItem item : AbstractDungeon.dungeonMapScreen.map.legend.items) {
                        if (item.hb.hovered) {
                            okayToOpenRadial = false;
                            break;
                        }
                    }
                }

                // Not on a node, a legend item, or the legend color display object, so we can open the radial menu
                if (okayToOpenRadial) {
                    SoundHelper.playRadialOpenSound();
                    menu.open();

                    rightMouseDownMode = RightMouseDownMode.RADIAL_MENU;
                }
            }

        }
        // Transition: Stopped right clicking
        else if (!InputHelper.isMouseDown_R && rightMouseDown) {
            rightMouseDown = false;
            rightMouseDownMode = RightMouseDownMode.NONE;

            // Finalize the radial menu
            if (menu.isMenuOpen()) {
                menu.close();
                SoundHelper.playRadialCloseSound();

                System.out.println("Menu closed. Selected index is: " + menu.getSelectedIndex());

                // Update the results with the new selection
                int selectedIndex = menu.getSelectedIndex();

                if (selectedIndex != -1 && selectedIndex != previouslySelectedIndex) {
                    ColorEnum newColor = menu.getSelectedColorOrDefault();
                    legendObject.setColor(newColor);

                    //MapTileManager.setHighlightingColor(EaselColors.withOpacity(newColor.get(), 0.8f));
                    MapTileManager.setHighlightingColor(newColor.get());

                    previouslySelectedIndex = selectedIndex;
                }
            }

        }
        // Currently right mouse is held and we're highlighting
        else if (rightMouseDownMode == RightMouseDownMode.HIGHLIGHTING) {
            MapTileManager.setHoveredTileHighlightStatus(true);
        }
        // Currently right mouse is held and we're unhighlighting
        else if (rightMouseDownMode == RightMouseDownMode.UNHIGHLIGHTING) {
            MapTileManager.setHoveredTileHighlightStatus(false);
        }

        menu.update();
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("MAP_MARKS_CLICK", "MapMarks/output_2.wav");
    }
}