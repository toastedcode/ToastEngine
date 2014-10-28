package gameEngine;
   
public class Pair<A, B>
{
   Pair(
      A initFirst,
      B initSecond)
   {
      first = initFirst;
      second = initSecond;
   }

   
   A getFirst()
   {
      return (first);
   }
   
   
   B getSecond()
   {
      return (second);
   }   
   
   private A first;
   private B second;
}
