package com.csonyi.cosmerecraft.client.capability.anchors;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.capability.anchors.AnchorObserver;
import com.csonyi.cosmerecraft.client.gui.AllomanticLineRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CosmereCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class LocalAnchorEventHandlers {

  /**
   * Hooks into the world rendering process to draw the allomantic lines.
   *
   * @param event The NeoForge event used to hook into the renderer.
   */
  @SubscribeEvent
  public static void renderAllomanticLines(RenderLevelStageEvent event) {
    if (RenderLevelStageEvent.Stage.AFTER_PARTICLES.equals(event.getStage())) {
      var localPlayer = Minecraft.getInstance().player;
      if (localPlayer == null) {
        return;
      }
      if (IAllomancy.of(localPlayer).isBurningAnyOf(AllomanticMetal.STEEL, AllomanticMetal.IRON)) {
        var anchors = new AnchorObserver(localPlayer).getAnchorsInRange();
        new AllomanticLineRenderer(event, anchors).renderLines();
      }
    }
  }
}
