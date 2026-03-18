package com.caedis.freecam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;

import org.lwjgl.input.Keyboard;

import com.caedis.freecam.camera.FreecamController;
import com.caedis.freecam.camera.tripod.TripodSlot;
import com.caedis.freecam.compat.Mods;
import com.gtnewhorizons.angelica.zoom.Zoom;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import ganymedes01.etfuturum.configuration.configs.ConfigFunctions;

public class ClientEventHandler {

    private final KeyBinding toggleKey;
    private final KeyBinding resetTripodsKey;
    private final KeyBinding playerControlKey;
    private boolean toggleKeyHeld;
    private boolean tripodActivated;

    public ClientEventHandler() {
        final String category = "key.categories.freecam";
        toggleKey = new KeyBinding("key.freecam.toggle", Keyboard.KEY_F4, category);
        resetTripodsKey = new KeyBinding("key.freecam.resetTripods", Keyboard.KEY_NONE, category);
        playerControlKey = new KeyBinding("key.freecam.playerControl", Keyboard.KEY_NONE, category);
        ClientRegistry.registerKeyBinding(toggleKey);
        ClientRegistry.registerKeyBinding(resetTripodsKey);
        ClientRegistry.registerKeyBinding(playerControlKey);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;

        FreecamController controller = FreecamController.instance();
        if (toggleKeyHeld) {
            // prevent hotbar key press while toggle key is down
            for (KeyBinding hotbarKey : Minecraft.getMinecraft().gameSettings.keyBindsHotbar) {
                while (hotbarKey.isPressed()) {}
            }
        }
        if (controller.isActive()) {
            controller.tick();
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int key = Keyboard.getEventKey();
        boolean pressed = Keyboard.getEventKeyState();

        if (key == toggleKey.getKeyCode()) {
            if (Mods.EFR.isLoaded() && isEFRGameModeSwitcherKeyPressed()) return;

            // drain the KeyBinding queue so it doesn't fire elsewhere
            while (toggleKey.isPressed()) {}

            if (pressed) {
                toggleKeyHeld = true;
                tripodActivated = false;
            } else {
                if (!tripodActivated) {
                    FreecamController.instance()
                        .toggle();
                }
                toggleKeyHeld = false;
            }
        } else if (toggleKeyHeld) {
            TripodSlot slot = TripodSlot.ofKeyCode(key);
            if (slot != TripodSlot.NONE) {
                int index = slot.ordinal() - 1;
                KeyBinding hotbarKey = Minecraft.getMinecraft().gameSettings.keyBindsHotbar[index];
                // prevent hotbar key press while toggle key is down
                while (hotbarKey.isPressed()) {}

                if (!pressed) {
                    FreecamController.instance()
                        .toggleTripod(slot);
                    tripodActivated = true;
                }
            } else if (!pressed && key == resetTripodsKey.getKeyCode()) {
                // drain it
                while (resetTripodsKey.isPressed()) {}
                FreecamController.instance()
                    .resetTripods();
                tripodActivated = true;
            } else if (!pressed && key == playerControlKey.getKeyCode()) {
                // drain it
                while (playerControlKey.isPressed()) {}
                FreecamController.instance()
                    .togglePlayerControl();
                tripodActivated = true;
            }
        }
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (event.dwheel == 0) return;
        if (!FreecamController.instance()
            .isActive()) return;

        if (Mods.ANGELICA.isLoaded() && isAngelicaZoomKeyPressed()) return;

        FreecamController.instance()
            .adjustSpeed(event.dwheel);
        event.setCanceled(true);
    }

    @Optional.Method(modid = "angelica")
    private boolean isAngelicaZoomKeyPressed() {
        return Zoom.getZoomKey()
            .getIsKeyPressed();
    }

    @Optional.Method(modid = "etfuturum")
    private boolean isEFRGameModeSwitcherKeyPressed() {
        return ConfigFunctions.enableGamemodeSwitcher && Keyboard.isKeyDown(Keyboard.KEY_F3);
    }

    @SubscribeEvent
    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        FreecamController.reset();
    }

    @SubscribeEvent
    public void onClientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        FreecamController.instance()
            .onDisconnect();
        FreecamController.reset();
    }
}
