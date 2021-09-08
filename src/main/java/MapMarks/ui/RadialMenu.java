package MapMarks.ui;

import MapMarks.MapMarks;
import MapMarks.utils.ColorDatabase;
import MapMarks.utils.SoundHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.utils.EaselInputHelper;
import easel.utils.colors.EaselColors;

import java.util.ArrayList;
import java.util.Arrays;

public class RadialMenu extends AbstractWidget<RadialMenu> {
    private static final float WIDTH = 500;
    private static final float HEIGHT = 500;

    private static final float THRESHOLD = 0.5f * RadialMenuObject.WIDTH; // TODO: maybe scale this up a tad (1.1x?)

    private static final float PI = (float) Math.PI;

    private final float thetaStart;
    private final float thetaDelta;

    private ArrayList<RadialMenuObject> objects;
    private RadialMenuObject centerObject;

    private static Color centerDefaultColor = EaselColors.fromHexString("8d8c80");

    int selectedIndex = -1;

    private boolean isOpen = false;
    private int startX;
    private int startY;

    public RadialMenu() {
        objects = new ArrayList<>(
//                Arrays.asList(
//                        new RadialMenuObject(EaselColorHelper.fromHexString("f1e9cf"), EaselColorHelper.fromHexString("b6ad8c")), // light gray
//                        new RadialMenuObject(EaselColorHelper.fromHexString("db2424"), EaselColorHelper.fromHexString("5e3333")), // red
//                        new RadialMenuObject(EaselColorHelper.fromHexString("41b611"), EaselColorHelper.fromHexString("3e5f30")), // green
//                        new RadialMenuObject(EaselColorHelper.fromHexString("2bafbf"), EaselColorHelper.fromHexString("335357")), // blue
//                        new RadialMenuObject(EaselColorHelper.fromHexString("ab30ba"), EaselColorHelper.fromHexString("3d2b3f")), // purple
//                        new RadialMenuObject(EaselColorHelper.fromHexString("cdb156"), EaselColorHelper.fromHexString("403a27")) // yellow
//                )
                Arrays.asList(
//                        new RadialMenuObject(Color.CORAL, Color.LIME), // test
                        new RadialMenuObject(ColorDatabase.DEFAULT_GRAY, ColorDatabase.DEFAULT_GRAY_DIMMED), // light gray
                        new RadialMenuObject(ColorDatabase.DEFAULT_RED, ColorDatabase.DEFAULT_RED_DIMMED), // red
                        new RadialMenuObject(ColorDatabase.DEFAULT_GREEN, ColorDatabase.DEFAULT_GREEN_DIMMED), // green
                        new RadialMenuObject(ColorDatabase.DEFAULT_BLUE, ColorDatabase.DEFAULT_BLUE_DIMMED), // blue
                        new RadialMenuObject(ColorDatabase.DEFAULT_PURPLE, ColorDatabase.DEFAULT_PURPLE_DIMMED), // purple
                        new RadialMenuObject(ColorDatabase.DEFAULT_YELLOW, ColorDatabase.DEFAULT_YELLOW_DIMMED) // yellow
                )
        );

        thetaDelta = (2.0f * PI) / objects.size();
        thetaStart = (PI / 2.0f) - thetaDelta;

        centerObject = new RadialMenuObject(centerDefaultColor, centerDefaultColor);
    }

    public void open() {
        isOpen = true;

        selectedIndex = -1;

        startX = EaselInputHelper.getMouseX();
        startY = EaselInputHelper.getMouseY();

        anchoredCenteredOnMouse();
    }

    public void close() {
        isOpen = false;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public Color getSelectedColorOrDefault() {
        if (selectedIndex != -1) {
            return objects.get(selectedIndex).getBaseColor();
        } else {
            return objects.get(1).getBaseColor();
        }
    }

    @Override
    public float getContentWidth() {
        return WIDTH;
    }

    @Override
    public float getContentHeight() {
        return HEIGHT;
    }

    @Override
    public RadialMenu anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);

        final float cx = getContentCenterX();
        final float cy = getContentCenterY();

        final float distance = 70;

        float theta = thetaStart;

        for (RadialMenuObject obj : objects) {
            float dx = (float) (distance * Math.cos(theta));
            float dy = (float) (distance * Math.sin(theta));

            obj.anchoredAt(cx + dx, cy + dy, AnchorPosition.CENTER, movementSpeed);

            theta += thetaDelta;
        }

        centerObject.anchoredAt(cx, cy, AnchorPosition.CENTER, movementSpeed);

        return this;
    }

    private int computeClosestObjectIndex(float theta) {
        if (theta < 0)
            theta += 2 * PI;

        float targetTheta = thetaStart;

        int target = -1;
        float thetaDifference = 2 * PI;

        for (int i = 0; i < objects.size(); ++i) {
            float potentialThetaDifference = Math.abs(targetTheta - theta);

            if (potentialThetaDifference < thetaDifference) {
                target = i;
                thetaDifference = potentialThetaDifference;
            }

            targetTheta += thetaDelta;
        }

        return target;
    }

    @Override
    protected void updateWidget() {
        super.updateWidget();

        if (isOpen) {
            // TODO
            int currX = EaselInputHelper.getMouseX();
            int currY = EaselInputHelper.getMouseY();

            int dx = currX - startX;
            int dy = currY - startY;

            float distanceFromStart = (float) Math.sqrt(dx * dx + dy * dy);

            if (distanceFromStart > THRESHOLD) {
                int nextSelectedIndex = computeClosestObjectIndex((float) Math.atan2(dy, dx));

                // Selection changed
                if (nextSelectedIndex != selectedIndex) {
                    this.selectedIndex = nextSelectedIndex;

                    if (selectedIndex != -1) {
                        SoundHelper.playRadialChangeSound(selectedIndex, objects.size());

                        RadialMenuObject selected = objects.get(selectedIndex);
                        selected.setDimmed(false);
                        MapMarks.legendObject.setColor(selected.getBaseColor());
//                    centerObject.setBaseColor(selected.getBaseColor());

                        // Dim the rest
                        for (int i = 0; i < objects.size(); ++i) {
                            if (i != selectedIndex) {
                                objects.get(i).setDimmed(true);
                            }
                        }
                    }
                }
            } else {
//                centerObject.setBaseColor(centerDefaultColor);
                objects.forEach(object -> object.setDimmed(false));
                selectedIndex = -1;
            }

        }
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        if (isOpen) {
            for (int i = 0; i < objects.size(); ++i) {
                if (i != selectedIndex) {
                    objects.get(i).render(sb);
                }
            }
//            objects.forEach(object -> object.render(sb));
            centerObject.render(sb);

            // Render the selected one on top
            if (selectedIndex != -1)
                objects.get(selectedIndex).render(sb);
        }
    }
}
