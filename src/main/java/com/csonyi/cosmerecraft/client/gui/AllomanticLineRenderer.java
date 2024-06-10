package com.csonyi.cosmerecraft.client.gui;

import com.csonyi.cosmerecraft.client.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Set;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Vector3f;

public class AllomanticLineRenderer {

  public static final int SEGMENTS = 1000;
  public static final float AMPLITUDE = 0.01f;
  public static final int RED = 66;
  public static final int GREEN = 215;
  public static final int BLUE = 245;
  private final PoseStack poseStack;
  private final Camera camera;
  private final Vector3f entityPosition;
  private final MultiBufferSource.BufferSource bufferSource;
  private final Set<BlockPos> anchors;

  public AllomanticLineRenderer(RenderLevelStageEvent event, Set<BlockPos> anchors) {
    float partialTicks = event.getPartialTick();
    this.poseStack = event.getPoseStack();
    this.camera = event.getCamera();
    this.entityPosition = camera.getEntity()
        .getPosition(partialTicks)
        .toVector3f()
        .add(0, 1f, 0);
    this.bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();
    this.anchors = anchors;
  }

  public void renderLines() {
    anchors.forEach(this::renderLine);
  }

  private void renderLine(BlockPos anchorPos) {
    var cameraPosition = camera.getPosition().toVector3f();
    var anchorCenter = anchorPos.getCenter().toVector3f();
    var buffer = bufferSource.getBuffer(RenderType.lines());
    var lineTransparency = calculateLineTransparency(anchorCenter, cameraPosition);
    poseStack.pushPose();
    poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
    renderStraightLine(buffer, anchorCenter, lineTransparency);
    renderSquigglyLine(buffer, anchorCenter, lineTransparency);
    bufferSource.endBatch();
    poseStack.popPose();
  }

  private void renderStraightLine(VertexConsumer buffer, Vector3f anchorCenter, int lineTransparency) {
    var matrix = poseStack.last().pose();
    var normal = new Vector3f(anchorCenter).sub(entityPosition).normalize();
    buffer
        .vertex(matrix, entityPosition.x, entityPosition.y, entityPosition.z)
        .color(RED, GREEN, BLUE, lineTransparency)
        .normal(poseStack.last(), normal.x, normal.y, normal.z)
        .endVertex();
    buffer
        .vertex(matrix, anchorCenter.x, anchorCenter.y, anchorCenter.z)
        .color(RED, GREEN, BLUE, lineTransparency)
        .normal(poseStack.last(), normal.x, normal.y, normal.z)
        .endVertex();
  }

  private void renderSquigglyLine(VertexConsumer buffer, Vector3f anchorCenter, int lineTransparency) {
    var matrix = poseStack.last().pose();
    buffer
        .vertex(matrix, entityPosition.x, entityPosition.y, entityPosition.z)
        .color(RED, GREEN, BLUE, lineTransparency)
        .normal(poseStack.last(), 0, 0, 0)
        .endVertex();

    for (int segmentIndex = 0; segmentIndex <= SEGMENTS; ++segmentIndex) {
      var offset = ((float) segmentIndex / SEGMENTS);
      var x = entityPosition.x + offset * (anchorCenter.x - entityPosition.x) + AMPLITUDE * (float) Math.sin(offset * 40 * Math.PI);
      var y = entityPosition.y + offset * (anchorCenter.y - entityPosition.y);
      var z = entityPosition.z + offset * (anchorCenter.z - entityPosition.z) + AMPLITUDE * (float) Math.cos(offset * 40 * Math.PI);

      buffer
          .vertex(matrix, x, y, z)
          .color(RED, GREEN, BLUE, lineTransparency)
          .normal(poseStack.last(), 0, 0, 0)
          .endVertex();
      buffer
          .vertex(matrix, x, y, z)
          .color(RED, GREEN, BLUE, lineTransparency)
          .normal(poseStack.last(), 0, 0, 0)
          .endVertex();
    }
  }

  private int calculateLineTransparency(Vector3f anchorCenter, Vector3f cameraPosition) {
    var distance = cameraPosition.distance(anchorCenter);
    if (distance > ClientConfig.allomanticLineRenderDistance) {
      return 0;
    }
    return Mth.lerpInt(distance / ClientConfig.allomanticLineRenderDistance, 0, 240);
  }
}
