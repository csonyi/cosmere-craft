package com.csonyi.cosmerecraft.gui;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Set;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public class AllomanticLineRenderer {

  private final float partialTicks;
  private final PoseStack poseStack;
  private final Matrix4f projectionMatrix;
  private final Camera camera;
  private final Set<BlockPos> anchors;

  public AllomanticLineRenderer(RenderLevelStageEvent event, Set<BlockPos> anchors) {
    this.partialTicks = event.getPartialTick();
    this.poseStack = event.getPoseStack();
    this.projectionMatrix = event.getProjectionMatrix();
    this.camera = event.getCamera();
    this.anchors = anchors;
  }

  public void renderLines() {
    anchors.forEach(this::renderLine);
  }

  private void renderLine(BlockPos anchorPos) {
    renderStraightLine(anchorPos);
    renderSquigglyLine(anchorPos);
  }

  private void renderStraightLine(BlockPos anchorPos) {
    var anchorCenter = anchorPos.getCenter().toVector3f();
    var entityPosition = camera.getEntity()
        .getPosition(partialTicks)
        .toVector3f()
        .add(0, 1f, 0);

    try (var vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC)) {
      var bufferbuilder = Tesselator.getInstance().getBuilder();
      bufferbuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
      bufferbuilder
          .vertex(entityPosition.x, entityPosition.y, entityPosition.z)
          .color(66, 215, 245, 255)
          .endVertex();
      bufferbuilder
          .vertex(anchorCenter.x, anchorCenter.y, anchorCenter.z)
          .color(66, 215, 245, 255)
          .endVertex();
      vertexBuffer.bind();
      vertexBuffer.upload(bufferbuilder.end());
      poseStack.pushPose();
      poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
      var shader = GameRenderer.getPositionColorShader();
      vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shader);
      poseStack.popPose();
      VertexBuffer.unbind();
    }
  }

  private void renderSquigglyLine(BlockPos anchorPos) {
    var anchorCenter = anchorPos.getCenter().toVector3f();
    var entityPosition = camera.getEntity()
        .getPosition(partialTicks)
        .toVector3f()
        .add(0, 1f, 0);

    try (var vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC)) {
      var bufferbuilder = Tesselator.getInstance().getBuilder();
      bufferbuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

      // Define the number of segments and the amplitude of the squiggly line
      var segments = 1000;
      var amplitude = 0.01f;

      bufferbuilder
          .vertex(entityPosition.x, entityPosition.y, entityPosition.z)
          .color(66, 215, 245, 255)
          .endVertex();

      for (int i = 0; i <= segments; ++i) {
        // Calculate the t value for this segment
        var t = ((float) i / segments);

        // Calculate the x, y, and z coordinates for this segment
        var x = entityPosition.x + t * (anchorCenter.x - entityPosition.x) + amplitude * (float) Math.sin(t * 40 * Math.PI);
        var y = entityPosition.y + t * (anchorCenter.y - entityPosition.y);
        var z = entityPosition.z + t * (anchorCenter.z - entityPosition.z) + amplitude * (float) Math.cos(t * 40 * Math.PI);

        // Add the vertex to the buffer
        bufferbuilder
            .vertex(x, y, z)
            .color(66, 215, 245, 128)  // Set the color and transparency
            .endVertex();
        bufferbuilder
            .vertex(x, y, z)
            .color(66, 215, 245, 128)  // Set the color and transparency
            .endVertex();
      }

      vertexBuffer.bind();
      vertexBuffer.upload(bufferbuilder.end());
      poseStack.pushPose();
      poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
      vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionColorShader());
      poseStack.popPose();
      VertexBuffer.unbind();
    }
  }

}
