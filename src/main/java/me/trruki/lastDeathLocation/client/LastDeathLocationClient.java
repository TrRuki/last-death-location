package me.trruki.lastDeathLocation.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.util.Optional;

public class LastDeathLocationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommands.literal("lastdeath")
                    .executes(context -> {
                        Minecraft client = Minecraft.getInstance();
                        if (client.player != null) {
                            Optional<GlobalPos> lastDeathOpt = client.player.getLastDeathLocation();
                            if (lastDeathOpt.isEmpty()) {
                                client.gui.chatListener().handleSystemMessage(Component.literal("§f[§dLast Death Location§f] §cDeath location not found"), false);
                            } else {
                                GlobalPos lastDeath = lastDeathOpt.get();
                                client.gui.chatListener().handleSystemMessage(Component.literal(String.format("§f[§dLast Death Location§f] §aX: %d §8| §aY: %d §8| §aZ: %d §8| §aDimension: %s", lastDeath.pos().getX(), lastDeath.pos().getY(), lastDeath.pos().getZ(), lastDeath.dimension().identifier())).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.copy.click").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)))).withClickEvent(new ClickEvent.CopyToClipboard(String.format("X: %d Y: %d Z: %d", lastDeath.pos().getX(), lastDeath.pos().getY(), lastDeath.pos().getZ())))), false);
                            }
                        }
                        return 1;
                    }));
        });
    }
}