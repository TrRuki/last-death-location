package me.trruki;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public class LastDeathLocationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("lastdeath")
                    .executes(context -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        if (client.player != null){
                            Optional<GlobalPos> lastDeathOpt = client.player.getLastDeathPos();
                            if (lastDeathOpt.isEmpty()){
                                client.inGameHud.getChatHud().addMessage(Text.literal("§f[§dLast Death Location§f] §cDeath location not found"));
                            }
                            else {
                                GlobalPos lastDeath = lastDeathOpt.get();
                                client.inGameHud.getChatHud().addMessage(Text.literal(String.format("§f[§dLast Death Location§f] §aX: %d §8| §aY: %d §8| §aZ: %d §8| §aDimension: %s", lastDeath.pos().getX(), lastDeath.pos().getY(), lastDeath.pos().getZ(), lastDeath.dimension().getValue())).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.copy.click").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))).withClickEvent(new ClickEvent.CopyToClipboard(String.format("X: %d Y: %d Z: %d", lastDeath.pos().getX(), lastDeath.pos().getY(), lastDeath.pos().getZ())))));
                            }
                        }
                        return 1;
                    }));
        });
    }
}
