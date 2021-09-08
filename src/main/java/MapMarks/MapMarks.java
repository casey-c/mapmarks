package MapMarks;

import MapMarks.ui.LegendObject;
import MapMarks.ui.MapTile;
import MapMarks.ui.RadialMenu;
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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AnchorPosition;
import easel.utils.EaselSoundHelper;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public static LegendObject legendObject;

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");
        TextureLoader.loadTextures(MapMarksTextureDatabase.values());
        //Easel.initialize();

        menu = new RadialMenu();
        legendObject = new LegendObject()
                .onLeftClick(onClick -> {
                    EaselSoundHelper.cawCaw();
                })
                .anchoredAt(1575, 767, AnchorPosition.CENTER)
        ;

    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        menu.render(sb);
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

        // Transition: Started right clicking
        if (InputHelper.isMouseDown_R && !rightMouseDown) {
            rightMouseDown = true;

            if (MapTileManager.isAnyTileHovered()) {
                if (MapTileManager.hoveredTileIsHighlighted()) {
                    // TODO: start unhighlighting everything, starting with this hovered highlighted node
                    rightMouseDownMode = RightMouseDownMode.UNHIGHLIGHTING;

                    MapTileManager.setHoveredTileHighlightStatus(false);
                }
                else {
                    // TODO: start highlighting everything, starting with this hovered unhighlighted node
                    rightMouseDownMode = RightMouseDownMode.HIGHLIGHTING;

                    MapTileManager.setHoveredTileHighlightStatus(true);
                }
            }
            else {
                // Not on a node, so we can open the radial menu
                SoundHelper.playRadialOpenSound();
                menu.open();

                rightMouseDownMode = RightMouseDownMode.RADIAL_MENU;
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
                    Color newColor = menu.getSelectedColorOrDefault();
                    legendObject.setColor(newColor);

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