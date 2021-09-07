package MapMarks.utils;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SoundHelper {
    public static void playRadialOpenSound() {
        int roll = MathUtils.random(3);
        switch (roll) {
            case 0: {
                CardCrawlGame.sound.play("MAP_SELECT_1");
                break;
            }
            case 1: {
                CardCrawlGame.sound.play("MAP_SELECT_2");
                break;
            }
            case 2: {
                CardCrawlGame.sound.play("MAP_SELECT_3");
                break;
            }
            default: {
                CardCrawlGame.sound.play("MAP_SELECT_4");
            }
        }
    }

    public static void playRadialCloseSound() {
        CardCrawlGame.sound.play("MAP_CLOSE");
    }
}
