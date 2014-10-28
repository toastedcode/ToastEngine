package com.toast.game.engine.interfaces;

import java.awt.Graphics;
import java.awt.Point;

public interface Drawable
{
   public void draw(
      Graphics graphics,
      Point position,
      double scale);
   
   public int getWidth();
   
   public int getHeight();
}
