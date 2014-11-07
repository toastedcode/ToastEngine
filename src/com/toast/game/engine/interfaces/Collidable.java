package com.toast.game.engine.interfaces;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public interface Collidable
{
   public boolean checkCollision(
      Collidable collidable,
      double deltaX,
      double deltaY);
   
   public Shape getShape();
   
   public Rectangle2D.Double getAABB();
}
