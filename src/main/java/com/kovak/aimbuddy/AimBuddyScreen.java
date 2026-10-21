package com.kovak.aimbuddy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public final class AimBuddyScreen extends Screen {
    public AimBuddyScreen() {
        super(new StringTextComponent("AimBuddy"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 105;
        int top = this.height / 2 - 88;
        addButton(new Button(left, top, 210, 24, enabledLabel(),
            b -> { ClientConfig.enabled = !ClientConfig.enabled; b.setMessage(new StringTextComponent(enabledLabel())); }));
        addButton(new Button(left, top + 31, 210, 20, hostileLabel(),
            b -> { ClientConfig.targetHostile = !ClientConfig.targetHostile; b.setMessage(new StringTextComponent(hostileLabel())); }));
        addButton(new Button(left, top + 55, 210, 20, passiveLabel(),
            b -> { ClientConfig.targetPassive = !ClientConfig.targetPassive; b.setMessage(new StringTextComponent(passiveLabel())); }));
        addButton(new Button(left, top + 82, 102, 20, rangeLabel(),
            b -> { ClientConfig.range = ClientConfig.range >= 40 ? 5 : ClientConfig.range + 5; b.setMessage(new StringTextComponent(rangeLabel())); }));
        addButton(new Button(left + 108, top + 82, 102, 20, intensityLabel(),
            b -> { ClientConfig.intensity = ClientConfig.intensity >= 0.9 ? 0.1 : ClientConfig.intensity + 0.1; b.setMessage(new StringTextComponent(intensityLabel())); }));
        addButton(new Button(left, top + 111, 210, 20, "Done",
            b -> this.minecraft.setScreen(null)));
    }

    private String enabledLabel() { return "Aim assist: " + (ClientConfig.enabled ? "ON" : "OFF"); }
    private String hostileLabel() { return "Hostile mobs: " + (ClientConfig.targetHostile ? "ON" : "OFF"); }
    private String passiveLabel() { return "Passive mobs: " + (ClientConfig.targetPassive ? "ON" : "OFF"); }
    private String rangeLabel() { return "Range: " + (int)ClientConfig.range + " blocks"; }
    private String intensityLabel() { return "Intensity: " + Math.round(ClientConfig.intensity * 100) + "%"; }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        drawCenteredString(this.font, "AIMBUDDY", this.width / 2, this.height / 2 - 112, 0x66D9C8);
        drawCenteredString(this.font, "Single-player mob target practice", this.width / 2, this.height / 2 - 100, 0xC7D2D8);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
