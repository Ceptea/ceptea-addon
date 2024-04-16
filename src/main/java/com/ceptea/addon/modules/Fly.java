package com.ceptea.addon.modules;

import com.ceptea.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;

public class Fly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Your speed when flying.")
            .max(40)
        .defaultValue(0.1)
        .min(0.0)
        .build()
    );
    boolean swi = false;
    double cooldown = -1;
    public Fly() {
        super(Addon.CATEGORY, "fly", "A improved flight module.");
    }
    @EventHandler
    public void onSneak(PacketEvent.Send event) {
        if (event.packet instanceof ClientCommandC2SPacket e) {
            if (e.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY) {
                event.cancel();
            }
        }
    }
    @EventHandler
    public void onPreTick(TickEvent.Pre event) {
        mc.player.setVelocity(0,0,0);
        if (System.currentTimeMillis() > cooldown) {
            addPos(0, 0.15, 0);
            swi = true;
            cooldown = System.currentTimeMillis() + 400;

            return;
        }
        if (swi) {
            addPos(0, -0.15, 0);
            swi = false;
        }
        strafe(speed.get().floatValue());
        if (mc.options.jumpKey.isPressed()) {
            mc.player.addVelocity(0,speed.get()/4,0);
        }
        if (mc.options.sneakKey.isPressed()) {
            mc.player.addVelocity(0,-speed.get()/4,0);
        }
    }

    public void strafe(float speed) {
        double x = 0;
        double z = 0;
        double side = getSide();
        double front = getForward();

        x = (front * Math.cos(Math.toRadians(mc.player.getYaw() + 90)));
        z = (front * Math.sin(Math.toRadians(mc.player.getYaw() + 90)));

        x += (float) (side * Math.cos(Math.toRadians(mc.player.getYaw())));
        z += (float) (side * Math.sin(Math.toRadians(mc.player.getYaw())));


        x /= 5;
        z /= 5;

        x *= speed;
        z *= speed;


        mc.player.setVelocity(x, mc.player.getVelocity().y, z);
    }


    public float getSide() {
        float side = 0;
        if (mc.options.leftKey.isPressed()) {
            side += 1;
        }
        if (mc.options.rightKey.isPressed()) {
            side -= 1;
        }
        return side;
    }

    public float getForward() {
        float forward = 0;
        if (mc.options.backKey.isPressed()) {
            forward -= 1;
        }
        if (mc.options.forwardKey.isPressed()) {
            forward += 1;
        }
        return forward;
    }
    public  void addPos(double x, double y, double z) {

        mc.player.setPos(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);

    }
}
