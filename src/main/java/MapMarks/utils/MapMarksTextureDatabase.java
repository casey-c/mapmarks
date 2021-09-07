package MapMarks.utils;

import com.badlogic.gdx.graphics.Texture;
import easel.utils.textures.ITextureDatabaseEnum;

public enum MapMarksTextureDatabase implements ITextureDatabaseEnum {
    LEGEND_BASE("MapMarks/textures/legend_base.png"),
    LEGEND_TRIM("MapMarks/textures/legend_trim.png"),

    RADIAL_BASE("MapMarks/textures/radial_base.png"),
    RADIAL_TRIM("MapMarks/textures/radial_trim.png")
    ;

    private final String internalPath;
    private Texture texture;

    MapMarksTextureDatabase(String internalPath) {
        this.internalPath = internalPath;
    }

    public void load() {
        this.texture = new Texture(internalPath);
    }

    public Texture getTexture() {
        return texture;
    }
}