package com.csonyi.cosmerecraft.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ToggleButton extends Button {

  private static final OnToggle NOOP = button -> {
  };

  protected boolean state;

  public ToggleButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
    this(pX, pY, pWidth, pHeight, NOOP, pMessage);
  }

  public ToggleButton(int pX, int pY, int pWidth, int pHeight, OnToggle toggleHandler, Component pMessage) {
    super(
        pX, pY,
        pWidth, pHeight,
        pMessage,
        button -> {
          if (button instanceof ToggleButton toggleButton) {
            toggleButton.toggle();
            toggleHandler.onToggle(toggleButton);
          }
        },
        DEFAULT_NARRATION);
    this.state = false;
  }

  public void toggle() {
    state = !state;
  }

  public boolean getState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  public interface OnToggle {

    void onToggle(ToggleButton button);
  }
}
