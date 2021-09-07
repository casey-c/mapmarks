package MapMarks;

import MapMarks.ui.LegendObject;
import MapMarks.ui.RadialMenu;
import MapMarks.utils.MapMarksTextureDatabase;
import MapMarks.utils.SoundHelper;
import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AnchorPosition;
import easel.utils.EaselSoundHelper;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class MapMarks implements PostInitializeSubscriber, PostUpdateSubscriber, RenderSubscriber {
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

    @Override
    public void receivePostUpdate() {
        if (InputHelper.isMouseDown_R && !rightMouseDown) {
            rightMouseDown = true;

            SoundHelper.playRadialOpenSound();
            menu.open();
        } else if (!InputHelper.isMouseDown_R && rightMouseDown) {
            rightMouseDown = false;
            menu.close();
            SoundHelper.playRadialCloseSound();

            System.out.println("Menu closed. Selected index is: " + menu.getSelectedIndex());

            //
            if (menu.getSelectedIndex() != -1) {
                Color newColor = menu.getSelectedColorOrDefault();
                legendObject.setColor(newColor);
            }
        }

        menu.update();
    }
}