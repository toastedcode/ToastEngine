package com.toast.game.engine.property;

public class Health extends Property
{
   public Health()
   {
      super("health");
   }


   public int getHealth()
   {
      return (health);
   }


   public void setHealth(
      int health)
   {
      // Validate.
      health = (health >= MIN_HEALTH) ? health : MIN_HEALTH; 
      health = (health <= getMaxHealth()) ? health : getMaxHealth();
      
      this.health = health;
   }


   public int getMaxHealth()
   {
      return (maxHealth);
   }


   public void setMaxHealth(int maxHealth)
   {
      // Validate.
      maxHealth = (maxHealth >= MIN_HEALTH) ? maxHealth : MIN_HEALTH;
      
      this.maxHealth = maxHealth;
   }

   
   public void damage(
      int damage)
   {
      setHealth(health - damage);
   }


   public void restore(
      int health)
   {
      setHealth(this.health + health);
   }


   public void kill()
   {
      setHealth(0);
   }
   
   public final int MIN_HEALTH = 0;

   private int health;

   private int maxHealth;
}
