package com.caedis.freecam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.caedis.freecam.camera.FreecamController;
import com.caedis.freecam.camera.tripod.TripodSlot;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class ClientEventHandler {

    public static KeyBinding toggleKey;
    public static KeyBinding resetTripodsKey;
    public static KeyBinding playerControlKey;
    private boolean toggleKeyHeld;
    private boolean tripodActivated;

    public void init() {
        String category = "key.categories.freecam";
        toggleKey = new KeyBinding("key.freecam.toggle", Keyboard.KEY_F4, category);
        resetTripodsKey = new KeyBinding("key.freecam.resetTripods", Keyboard.KEY_R, category);
        playerControlKey = new KeyBinding("key.freecam.playerControl", Keyboard.KEY_C, category);

        ClientRegistry.registerKeyBinding(toggleKey);
        ClientRegistry.registerKeyBinding(resetTripodsKey);
        ClientRegistry.registerKeyBinding(playerControlKey);

        MinecraftForge.EVENT_BUS.register(this);
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
            // disable perspective swap key while in freecam
            while (Minecraft.getMinecraft().gameSettings.keyBindTogglePerspective.isPressed()) {}
            controller.tick();
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int key = Keyboard.getEventKey();
        boolean pressed = Keyboard.getEventKeyState();

        if (key == toggleKey.getKeyCode()) {
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
        if (event.dwheel != 0 && FreecamController.instance()
            .isActive()) {
            FreecamController.instance()
                .adjustSpeed(event.dwheel);
            event.setCanceled(true);
        }
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
