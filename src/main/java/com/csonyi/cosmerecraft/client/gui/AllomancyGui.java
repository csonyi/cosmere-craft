package com.csonyi.cosmerecraft.client.gui;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.networking.ServerBurnStateUpdateHandler;
import com.csonyi.cosmerecraft.util.ColorUtils;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AllomancyGui extends Screen {

  // Add this to the render function to display the mouse coordinates for debugging:
  // guiGraphics.drawString(font, Component.literal("X: %s Y: %s".formatted(mouseX, mouseY)), 2, 2, 0xffffff);

  private static final ResourceLocation BACKGROUND = ResourceUtils.modLocation("textures/gui/allomancy_gui_bg.png");
  private static final ResourceLocation TYPE_BOX = ResourceUtils.modLocation("textures/gui/allomancy_gui_box.png");
  private static final ResourceLocation BUTTON_BACKGROUND = ResourceUtils.modLocation("textures/gui/allomancy_gui_button.png");

  private int backgroundXMargin;
  private int backgroundYMargin;
  private int backgroundWidth;
  private int backgroundHeight;
  private int typeBoxWidth;
  private int typeBoxHeight;
  private int typeBoxXMargin;
  private int typeBoxYMargin;
  private int typeBoxXPadding;
  private int typeBoxYPadding;
  private int typeBoxHeaderHeight;
  private int editorXMargin;
  private int editorYMargin;
  private int editorXPadding;
  private int editorYPadding;
  private int editorWidth;
  private int editorHeight;
  private int editorComponentXPadding;
  private int editorComponentYPadding;
  private Map<AllomanticMetal, TextureToggleButton> metalButtons;
  private Map<AllomanticMetal, StringWidget> metalNames;

  private IAllomancy allomancy;
  private Set<AllomanticMetal> metals;

  public AllomancyGui() {
    super(Component.translatable("cosmerecraft.gui.allomancy"));
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  protected void init() {
    if (minecraft == null || minecraft.player == null) {
      return;
    }
    allomancy = IAllomancy.of(minecraft.player);
    metals = allomancy.getAvailableMetals();

    int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
    int scaledHeight = minecraft.getWindow().getGuiScaledHeight();
    backgroundXMargin = scaledWidth / 48;
    backgroundYMargin = scaledHeight / 16;
    backgroundWidth = scaledWidth - (2 * backgroundXMargin);
    backgroundHeight = scaledHeight - (2 * backgroundYMargin);
    typeBoxXMargin = scaledWidth / 25;
    typeBoxYMargin = scaledHeight / 17;
    typeBoxXPadding = scaledWidth / 50;
    typeBoxYPadding = scaledHeight / 40;
    typeBoxWidth = (backgroundWidth / 2) - Mth.floor(1.2 * typeBoxXMargin);
    typeBoxHeight = (backgroundHeight / 2) - Mth.floor(1.2 * typeBoxYMargin);
    typeBoxHeaderHeight = typeBoxHeight / 6;
    editorXMargin = scaledWidth / 40;
    editorYMargin = scaledHeight / 40;
    editorXPadding = scaledWidth / 23;
    editorYPadding = scaledHeight / 50;
    editorWidth = typeBoxWidth / 2 - (editorXMargin * 2);
    editorHeight = typeBoxHeight / 2 - (editorYMargin * 2);
    editorComponentXPadding = scaledWidth / 50;
    editorComponentYPadding = scaledHeight / 40;

    metalButtons = metals.stream()
        .collect(toMap(identity(), this::createMetalToggleButton));
    metalNames = metals.stream()
        .collect(toMap(identity(), this::createMetalName));
  }


  @Override
  public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    renderBackground(guiGraphics);
    renderTypeBoxes(guiGraphics);
    metalButtons.forEach((metal, button) -> button.render(guiGraphics, mouseX, mouseY, partialTick));
    metalNames.forEach((metal, name) -> name.render(guiGraphics, mouseX, mouseY, partialTick));
    renderReserves(guiGraphics);
  }

  private void renderBackground(GuiGraphics guiGraphics) {
    guiGraphics.blit(
        BACKGROUND,
        backgroundXMargin, backgroundYMargin,
        backgroundWidth, backgroundHeight,
        0.0F, 0.0F,
        backgroundWidth, backgroundHeight,
        backgroundWidth, backgroundHeight);
  }

  private void renderTypeBoxes(GuiGraphics guiGraphics) {
    for (var xIndex = 0; xIndex < 2; xIndex++) {
      for (var yIndex = 0; yIndex < 2; yIndex++) {
        guiGraphics.blit(
            TYPE_BOX,
            typeBoxX(xIndex), typeBoxY(yIndex),
            typeBoxWidth, typeBoxHeight,
            0, 0,
            typeBoxWidth, typeBoxHeight,
            typeBoxWidth, typeBoxHeight);
        guiGraphics.drawString(
            font,
            AllomanticMetal.Type.values()[yIndex * 2 + xIndex].getNameAsComponent(),
            typeBoxX(xIndex) + typeBoxWidth / 25,
            typeBoxY(yIndex) + typeBoxHeight / 18,
            ColorUtils.WHITE);
      }
    }
  }

  private TextureToggleButton createMetalToggleButton(AllomanticMetal metal) {
    var xIndex = metal.getTableXIndex();
    var yIndex = metal.getTableYIndex();
    var typeBoxXIndex = xIndex / 2;
    var typeBoxYIndex = yIndex / 2;
    var metalButton = new TextureToggleButton(
        typeBoxX(typeBoxXIndex) + editorXMargin + (xIndex % 2) * (editorWidth + editorXPadding),
        typeBoxY(typeBoxYIndex) + editorYMargin + typeBoxHeaderHeight + (yIndex % 2) * (editorHeight + editorYPadding),
        32, 32,
        button -> {
          allomancy.setBurnState(metal, button.state);
          ServerBurnStateUpdateHandler.sendBurnStateUpdatesToServer(metal, button.state);
        },
        BUTTON_BACKGROUND,
        metal.texture("metal"),
        metal.texture("white"));
    metalButton.setState(allomancy.getBurnState(metal));
    return addWidget(metalButton);
  }

  private StringWidget createMetalName(AllomanticMetal metal) {
    var metalButton = metalButtons.get(metal);
    var nameX = metalButton.getRight() + editorComponentXPadding;
    var nameY = metalButton.getY();
    var metalNameComponent = metal.getNameAsComponent();
    return new StringWidget(
        nameX, nameY,
        font.width(metalNameComponent.getString()), font.lineHeight,
        metalNameComponent, font);
  }

  private void renderReserves(GuiGraphics guiGraphics) {
    metals.forEach(metal -> {
      var metalButton = metalButtons.get(metal);
      var metalName = metalNames.get(metal);
      var metalReserve = allomancy.getReserve(metal);
      guiGraphics.drawString(
          font, TickUtils.toTimeFormat(metalReserve),
          metalButton.getRight() + editorComponentXPadding,
          metalName.getBottom() + editorComponentYPadding,
          ColorUtils.WHITE);
    });
  }

  private int typeBoxY(int yIndex) {
    return backgroundYMargin + typeBoxYMargin + (yIndex * (typeBoxHeight + typeBoxYPadding));
  }

  private int typeBoxX(int xIndex) {
    return backgroundXMargin + typeBoxXMargin + (xIndex * (typeBoxWidth + typeBoxXPadding));
  }

  @Override
  public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
    if (pKeyCode == 73) {
      onClose();
      return true;
    }
    return super.keyPressed(pKeyCode, pScanCode, pModifiers);
  }

  @Override
  public void onClose() {
    super.onClose();
  }

  @Override
  protected boolean shouldNarrateNavigation() {
    return false;
  }

}
