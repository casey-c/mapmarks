package MapMarks.utils;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SoundHelper {
    public static void playRadialOpenSound() {
//        int roll = MathUtils.random(3);
//        switch (roll) {
//            case 0: {
//                CardCrawlGame.sound.play("MAP_SELECT_1");
//                break;
//            }
//            case 1: {
//                CardCrawlGame.sound.play("MAP_SELECT_2");
//                break;
//            }
//            case 2: {
//                CardCrawlGame.sound.play("MAP_SELECT_3");
//                break;
//            }
//            default: {
//                CardCrawlGame.sound.play("MAP_SELECT_4");
//            }
//        }
    }

    public static void playMapScratchSound() {
        int roll = MathUtils.random(3);
        switch (roll) {
            case 0: {
                CardCrawlGame.sound.play("MAP_HOVER_1");
                break;
            }
            case 1: {
                CardCrawlGame.sound.play("MAP_HOVER_2");
                break;
            }
            case 2: {
                CardCrawlGame.sound.play("MAP_HOVER_3");
                break;
            }
            default: {
                CardCrawlGame.sound.play("MAP_HOVER_4");
            }
        }
    }

    public static void playRadialChangeSound(int index, int max) {
        float pitchAdjust = ((float)index / (float)max) * 1.8f;
        //CardCrawlGame.sound.play("MAP_MARKS_CLICK");
        CardCrawlGame.sound.playA("MAP_MARKS_CLICK", pitchAdjust);
    }

    public static void playRadialCloseSound() {
//        CardCrawlGame.sound.play("MAP_CLOSE");
    }
}
