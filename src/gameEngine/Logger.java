package gameEngine;

public class Logger
{
   public static void logDebug(
      String className,
      String string)
   {
      System.out.print(className);
      System.out.print(": ");
      System.out.println(string);
   }
   
   
   public static void logWarning(
      String className,
      String string)
   {
      System.out.print(className);
      System.out.print(": ");
      System.out.println(string);
   }   
   
   
   public static void logSevere(
         String className,
         String string)
   {
      System.out.print(className);
      System.out.print(": ");
      System.out.println(string);
   }   
   
}
