package com.caedis.freecam.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

import com.caedis.freecam.camera.tripod.TripodRegistry;
import com.caedis.freecam.camera.tripod.TripodSlot;
import com.caedis.freecam.config.GeneralConfig;
import com.caedis.freecam.config.MiscConfig;
import com.caedis.freecam.config.MovementConfig;
import com.gtnewhorizon.gtnhlib.util.AboveHotbarHUD;

import lombok.Getter;

public class FreecamController {

    private static FreecamController INSTANCE = new FreecamController();

    private final Minecraft mc = Minecraft.getMinecraft();
    private final TripodRegistry tripodRegistry = new TripodRegistry();
    private CameraEntity cameraEntity;
    private EntityLivingBase previousRenderViewEntity;
    @Getter
    private boolean active;
    @Getter
    private boolean playerControlled;
    @Getter
    private TripodSlot activeSlot = TripodSlot.NONE;
    private boolean pendingDisable;
    private int previousPerspective = -1;
    private float speedMultiplier = 1.0F;

    private static final float SPEED_SCROLL_STEP = 0.1F;
    private static final float SPEED_MULTIPLIER_MIN = 0.1F;
    private static final float SPEED_MULTIPLIER_MAX = 10.0F;

    private double velocityX;
    private double velocityY;
    private double velocityZ;

    private static final double DEFAULT_SPEED_SCALE = 0.5;
    private static final double CREATIVE_SPEED_SCALE = 2.0;
    private static final double CREATIVE_ACCELERATION = 0.15;
    private static final double CREATIVE_FRICTION = 0.6;
    private static final double SPRINT_MULTIPLIER = 1.5;
    private static final double DIAGONAL_FACTOR = Math.sin(Math.toRadians(45));

    private FreecamController() {}

    public static FreecamController instance() {
        return INSTANCE;
    }

    public static void reset() {
        INSTANCE = new FreecamController();
    }

    public void onDisconnect() {
        if (active) {
            disable();
        }
    }

    public void toggle() {
        if (active) {
            disable();
        } else {
            enable();
        }

        AboveHotbarHUD.renderTextAboveHotbar(
            StatCollector.translateToLocal(active ? "msg.freecam.enable" : "msg.freecam.disable"),
            20,
            true,
            true);
    }

    public void toggleTripod(TripodSlot slot) {
        if (GeneralConfig.disabled) {
            AboveHotbarHUD.renderTextAboveHotbar(StatCollector.translateToLocal("msg.freecam.disable"), 20, true, true);
            return;
        }

        if (active && activeSlot == slot) {
            disable();
            AboveHotbarHUD.renderTextAboveHotbar(
                StatCollector.translateToLocalFormatted("msg.freecam.tripod.close", slot.ordinal()),
                20,
                true,
                true);
            return;
        }

        if (active) {
            switchTripod(slot);
        } else {
            enableTripod(slot);
        }

        AboveHotbarHUD.renderTextAboveHotbar(
            StatCollector.translateToLocalFormatted("msg.freecam.tripod.open", slot.ordinal()),
            20,
            true,
            true);
    }

    public void enable() {
        if (active || GeneralConfig.disabled || mc.thePlayer == null || mc.theWorld == null) return;

        cameraEntity = new CameraEntity(mc.theWorld, mc.thePlayer);
        cameraEntity.setCollisionMode(GeneralConfig.collisionMode);
        previousRenderViewEntity = mc.renderViewEntity;
        previousPerspective = mc.gameSettings.thirdPersonView;
        mc.gameSettings.thirdPersonView = 0;
        mc.renderViewEntity = cameraEntity;
        active = true;
        playerControlled = false;
        activeSlot = TripodSlot.NONE;
        velocityX = 0;
        velocityY = 0;
        velocityZ = 0;

        applyPerspectiveOffset();
    }

    private void enableTripod(TripodSlot slot) {
        if (active || GeneralConfig.disabled || mc.thePlayer == null || mc.theWorld == null) return;

        cameraEntity = tripodRegistry.getOrCreate(slot);
        cameraEntity.setCollisionMode(GeneralConfig.collisionMode);
        previousRenderViewEntity = mc.renderViewEntity;
        if (previousPerspective == -1) {
            previousPerspective = mc.gameSettings.thirdPersonView;
            mc.gameSettings.thirdPersonView = 0;
        }
        mc.renderViewEntity = cameraEntity;
        active = true;
        playerControlled = false;
        activeSlot = slot;
        velocityX = 0;
        velocityY = 0;
        velocityZ = 0;
    }

    private void switchTripod(TripodSlot slot) {
        cameraEntity = tripodRegistry.getOrCreate(slot);
        cameraEntity.setCollisionMode(GeneralConfig.collisionMode);
        mc.renderViewEntity = cameraEntity;
        activeSlot = slot;
        velocityX = 0;
        velocityY = 0;
        velocityZ = 0;
    }

    public void disable() {
        if (!active) return;

        mc.renderViewEntity = previousRenderViewEntity;
        mc.gameSettings.thirdPersonView = previousPerspective;
        previousPerspective = -1;
        previousRenderViewEntity = null;
        cameraEntity = null;
        active = false;
        activeSlot = TripodSlot.NONE;
    }

    private static final double THIRD_PERSON_DISTANCE = 4.0;
    private static final double FIRST_PERSON_DISTANCE = 0.4;

    // main logic from orientCamera
    private void applyPerspectiveOffset() {
        MiscConfig.Perspective perspective = MiscConfig.initialPerspective;
        if (perspective == MiscConfig.Perspective.INSIDE) {
            return;
        }

        double yawRad = Math.toRadians(cameraEntity.rotationYaw);
        double pitchRad = Math.toRadians(cameraEntity.rotationPitch);

        double lookX = -Math.sin(yawRad) * Math.cos(pitchRad);
        double lookY = -Math.sin(pitchRad);
        double lookZ = Math.cos(yawRad) * Math.cos(pitchRad);

        if (perspective == MiscConfig.Perspective.FIRST_PERSON) {
            // move just in front of eyes
            cameraEntity.setPosition(
                cameraEntity.posX + lookX * FIRST_PERSON_DISTANCE,
                cameraEntity.posY + lookY * FIRST_PERSON_DISTANCE,
                cameraEntity.posZ + lookZ * FIRST_PERSON_DISTANCE);
        } else if (perspective == MiscConfig.Perspective.THIRD_PERSON) {
            cameraEntity.setPosition(
                cameraEntity.posX - lookX * THIRD_PERSON_DISTANCE,
                cameraEntity.posY - lookY * THIRD_PERSON_DISTANCE,
                cameraEntity.posZ - lookZ * THIRD_PERSON_DISTANCE);
        } else if (perspective == MiscConfig.Perspective.THIRD_PERSON_MIRROR) {
            cameraEntity.setPosition(
                cameraEntity.posX + lookX * THIRD_PERSON_DISTANCE,
                cameraEntity.posY + lookY * THIRD_PERSON_DISTANCE,
                cameraEntity.posZ + lookZ * THIRD_PERSON_DISTANCE);
            cameraEntity.rotationYaw += 180.0F;
            cameraEntity.prevRotationYaw += 180.0F;
        }
    }

    public void scheduleDisable() {
        pendingDisable = true;
    }

    public void togglePlayerControl() {
        if (!active) return;

        playerControlled = !playerControlled;

        AboveHotbarHUD.renderTextAboveHotbar(
            StatCollector
                .translateToLocal(playerControlled ? "msg.freecam.control.player" : "msg.freecam.control.camera"),
            20,
            true,
            true);
    }

    public void resetTripods() {
        tripodRegistry.clear();

        AboveHotbarHUD
            .renderTextAboveHotbar(StatCollector.translateToLocal("msg.freecam.tripod.reset"), 20, true, true);
    }

    public void adjustSpeed(int scrollDelta) {
        if (scrollDelta > 0) {
            speedMultiplier = Math.min(SPEED_MULTIPLIER_MAX, speedMultiplier + SPEED_SCROLL_STEP);
        } else if (scrollDelta < 0) {
            speedMultiplier = Math.max(SPEED_MULTIPLIER_MIN, speedMultiplier - SPEED_SCROLL_STEP);
        }
        speedMultiplier = Math.round(speedMultiplier * 10.0F) / 10.0F;

        AboveHotbarHUD.renderTextAboveHotbar(
            StatCollector.translateToLocalFormatted("msg.freecam.speed", speedMultiplier),
            20,
            true,
            true);
    }

    public void tick() {
        if (pendingDisable || GeneralConfig.disabled) {
            disable();
        }
        pendingDisable = false;

        if (!active || cameraEntity == null) return;

        if (mc.thePlayer != null && mc.thePlayer.isDead) {
            disable();
            return;
        }

        if (cameraEntity.worldObj != mc.theWorld) {
            disable();
            return;
        }

        cameraEntity.setCollisionMode(GeneralConfig.collisionMode);
        cameraEntity.onUpdate();

        if (playerControlled) return;

        GameSettings gs = mc.gameSettings;
        boolean forward = gs.keyBindForward.getIsKeyPressed();
        boolean back = gs.keyBindBack.getIsKeyPressed();
        boolean left = gs.keyBindLeft.getIsKeyPressed();
        boolean right = gs.keyBindRight.getIsKeyPressed();
        boolean up = gs.keyBindJump.getIsKeyPressed();
        boolean down = gs.keyBindSneak.getIsKeyPressed();
        boolean sprint = gs.keyBindSprint.getIsKeyPressed();

        double speed = MovementConfig.speed * speedMultiplier;
        if (sprint) {
            speed *= SPRINT_MULTIPLIER;
        }

        if (MovementConfig.movementMode == MovementConfig.MovementMode.CREATIVE) {
            tickCreativeMovement(forward, back, left, right, up, down, speed * CREATIVE_SPEED_SCALE);
        } else {
            tickDefaultMovement(forward, back, left, right, up, down, speed * DEFAULT_SPEED_SCALE);
        }

        clampToRenderDistance();
    }

    private void clampToRenderDistance() {
        if (mc.thePlayer == null) return;

        double maxDist = (mc.gameSettings.renderDistanceChunks - 1) * 16.0;
        double dx = cameraEntity.posX - mc.thePlayer.posX;
        double dz = cameraEntity.posZ - mc.thePlayer.posZ;

        double clampedX = Math.max(-maxDist, Math.min(maxDist, dx));
        double clampedZ = Math.max(-maxDist, Math.min(maxDist, dz));

        if (clampedX != dx || clampedZ != dz) {
            cameraEntity.setPosition(mc.thePlayer.posX + clampedX, cameraEntity.posY, mc.thePlayer.posZ + clampedZ);
        }
    }

    private void tickDefaultMovement(boolean forward, boolean back, boolean left, boolean right, boolean up,
        boolean down, double speed) {
        double yawRad = Math.toRadians(cameraEntity.rotationYaw);

        double dx = 0;
        double dz = 0;
        double dy = 0;

        boolean movingForward = forward != back;
        boolean strafing = left != right;

        if (movingForward) {
            double dir = forward ? 1 : -1;
            dx += -Math.sin(yawRad) * dir;
            dz += Math.cos(yawRad) * dir;
        }
        if (strafing) {
            double dir = left ? 1 : -1;
            dx += Math.cos(yawRad) * dir;
            dz += Math.sin(yawRad) * dir;
        }

        // Normalize diagonal horizontal movement
        if (movingForward && strafing) {
            dx *= DIAGONAL_FACTOR;
            dz *= DIAGONAL_FACTOR;
        }

        if (up != down) {
            dy = up ? 1 : -1;
        }

        cameraEntity.moveEntity(dx * speed, dy * speed, dz * speed);
    }

    private void tickCreativeMovement(boolean forward, boolean back, boolean left, boolean right, boolean up,
        boolean down, double speed) {
        double yawRad = Math.toRadians(cameraEntity.rotationYaw);

        double targetX = 0;
        double targetZ = 0;
        double targetY = 0;

        boolean movingForward = forward != back;
        boolean strafing = left != right;

        if (movingForward) {
            double dir = forward ? 1 : -1;
            targetX += -Math.sin(yawRad) * dir;
            targetZ += Math.cos(yawRad) * dir;
        }
        if (strafing) {
            double dir = left ? 1 : -1;
            targetX += Math.cos(yawRad) * dir;
            targetZ += Math.sin(yawRad) * dir;
        }

        if (movingForward && strafing) {
            targetX *= DIAGONAL_FACTOR;
            targetZ *= DIAGONAL_FACTOR;
        }

        if (up != down) {
            targetY = up ? 1 : -1;
        }

        targetX *= speed;
        targetY *= speed;
        targetZ *= speed;

        velocityX += (targetX - velocityX) * CREATIVE_ACCELERATION;
        velocityY += (targetY - velocityY) * CREATIVE_ACCELERATION;
        velocityZ += (targetZ - velocityZ) * CREATIVE_ACCELERATION;

        velocityX *= CREATIVE_FRICTION;
        velocityY *= CREATIVE_FRICTION;
        velocityZ *= CREATIVE_FRICTION;

        if (Math.abs(velocityX) < 0.001) velocityX = 0;
        if (Math.abs(velocityY) < 0.001) velocityY = 0;
        if (Math.abs(velocityZ) < 0.001) velocityZ = 0;

        cameraEntity.moveEntity(velocityX, velocityY, velocityZ);
    }
}
