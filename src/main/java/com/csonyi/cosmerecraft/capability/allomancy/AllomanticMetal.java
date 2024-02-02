package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.Config;
import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.networking.MetalReserveChangeHandler;
import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;

public enum AllomanticMetal {

  STEEL, IRON, ZINC, BRASS,
  PEWTER, TIN, COPPER, BRONZE,
  DURALUMIN, ALUMINUM, GOLD, ELECTRUM,
  NICROSIL, CHROMIUM, CADMIUM, BENDALLOY,
  ATIUM, LERASIUM;

  private final Supplier<AttachmentType<Integer>> RESERVE = CosmereCraft.ATTACHMENT_TYPES.register(
      "%s_reserve".formatted(name().toLowerCase()),
      () -> AttachmentType.builder(() -> 0)
          .serialize(Codec.INT)
          .build()
  );

  private final Supplier<AttachmentType<Boolean>> IN_USE = CosmereCraft.ATTACHMENT_TYPES.register(
      "%s_in_use".formatted(name().toLowerCase()),
      () -> AttachmentType.builder(() -> false)
          .serialize(Codec.BOOL)
          .build()
  );

  private final Supplier<AttachmentType<Boolean>> AVAILABLE = CosmereCraft.ATTACHMENT_TYPES.register(
      "%s_available".formatted(name().toLowerCase()),
      () -> AttachmentType.builder(() -> false)
          .serialize(Codec.BOOL)
          .copyOnDeath()
          .build()
  );

  public static boolean canReceive(Player player) {
    return Config.Server.collectiveAllomanticCapacity <= getCollectiveMetalAmount(player);
  }

  private static int getCollectiveMetalAmount(Player player) {
    return Arrays.stream(values())
        .mapToInt(metal -> metal.getReserve(player))
        .sum();
  }

  public int getReserve(Player player) {
    return player.getData(RESERVE);
  }

  public boolean isInUse(Player player) {
    return player.getData(IN_USE);
  }

  public boolean isAvailable(Player player) {
    return player.getData(AVAILABLE);
  }

  public void updateState(Player player, MetalReserveChangeHandler.MetalReserveChange metalReserveChange) {
    player.setData(RESERVE, metalReserveChange.amount());
    player.setData(IN_USE, metalReserveChange.isInUse());
    player.setData(AVAILABLE, metalReserveChange.isAvailable());
  }

  public void receive(Player player, int amount) {
    if (canReceive(player)) {
      player.setData(RESERVE, getReserve(player) + amount);
      updateClient(player);
    }
  }

  public void expend(Player player, int amount) {
    if (canExpend(player)) {
      player.setData(RESERVE, getReserve(player) - amount);
      updateClient(player);
    }
  }

  public boolean canExpend(Player player) {
    return getReserve(player) > 0;
  }

  private void updateClient(Player player) {
    if (player instanceof ServerPlayer serverPlayer) {
      PacketDistributor.PLAYER.with(serverPlayer)
          .send(
              new MetalReserveChangeHandler.MetalReserveChange(
                  this,
                  getReserve(player),
                  isInUse(player),
                  isAvailable(player)));
    }

  }
}
