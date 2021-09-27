package MapMarks.utils;

import com.badlogic.gdx.graphics.Texture;
import easel.utils.textures.ITextureDatabaseEnum;

public enum MapMarksTextureDatabase implements ITextureDatabaseEnum {
    PAINT_CIRCLE("MapMarks/textures/circle18.png"),

    LEGEND_BASE("MapMarks/textures/legend_base.png"),
    LEGEND_TRIM("MapMarks/textures/legend_trim.png"),

//    MAP_TILE_BASE("MapMarks/textures/tile/base.png"),
//    MAP_TILE_TRIM("MapMarks/textures/tile/trim.png"),
//    MAP_TILE_SHADOW("MapMarks/textures/tile/shadow.png"),

    SMALL_TILE_SHADOW("MapMarks/textures/small/shadow.png"),
    SMALL_TILE_BASE("MapMarks/textures/small/base.png"),
    SMALL_TILE_TRIM("MapMarks/textures/small/trim.png"),

    LARGE_TILE_SHADOW("MapMarks/textures/large/shadow.png"),
    LARGE_TILE_INNER_BASE("MapMarks/textures/large/inner_base.png"),
    LARGE_TILE_OUTER_BASE("MapMarks/textures/large/outer_base.png"),
    LARGE_TILE_TRIM("MapMarks/textures/large/trim.png"),

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
