package com.toast.game.engine.resource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class Resource
{
   public static String getResourcePath() throws ResourceCreationException
   {
      String path = null;
      
      URL url = Resource.class.getResource("/resources");
      if (url != null)
      {
         path = url.getFile();
         path = path.replace("/", File.separator);
      }
      else
      {
         path = null;
         throw (new ResourceCreationException());
      }
      
      return (path); 
   }
   
   
   public static void loadResources()
   {
      
   }

   
   public static void freeResources()
   {
      resourceMap.clear();
   }
   
   
   public static void createResources(String path) throws ResourceCreationException
   {
      File[] files = new File(path).listFiles();
      
      if (files != null)
      {
         for (File file : files)
         {
            if (file.isDirectory())
            {
               createResources(file.getPath());
            }
            else
            {
               createResource(file.getPath());
            }
         }
      }
   }
   
      
   public static Resource createResource(String filename) throws ResourceCreationException
   {
      Resource resource = null;

      try
      {
         String srcFilename = filename.substring(filename.lastIndexOf('\\') + 1);
         String extension = srcFilename.substring(srcFilename.lastIndexOf('.'));
         ResourceType resourceType = ResourceType.getResourceType(extension);
         String id = srcFilename;
         
         resource = (Resource)Class.forName(resourceType.RESOURCE_CLASS).getConstructor(String.class).newInstance(id);
         resource.load(filename);
         
         resourceMap.put(id, resource);
      }
      catch (NullPointerException | InvocationTargetException | IllegalAccessException | InstantiationException | 
             NoSuchMethodException | ClassNotFoundException e)
      {
         throw (new ResourceCreationException());
      }
      
      return (resource);
   }
   
  
   public Resource(
      String id)
   {
      ID = id;
      isLoaded = false;
   }
   
   
   public boolean isLoaded()
   {
      return (isLoaded);
   }
   
   
   public abstract void load(
      String filename) throws ResourceCreationException;
     
   static final String DEFAULT_RESOURCE_PATH = "resources";
   
   static Map<String, Resource> resourceMap = new HashMap<>();
   
   public enum ResourceType
   {
      IMAGE(        new String[]{".jpg", ".png"}, "com.toast.game.engine.resource.Texture"),
      ANIMATION_MAP(new String[]{".anim"},        "com.toast.game.engine.resource.AnimationMap"),
      SFX(          new String[]{".wav"},         "com.toast.game.engine.resource.Sfx"),
      SCRIPT(       new String[]{".bsh"},         "com.toast.game.engine.resource.Script");
      
      private ResourceType(
         String[] extensions,
         String resourceClass)
      {
         EXTENSIONS = extensions;
         RESOURCE_CLASS = resourceClass;
      }
      
      public static ResourceType getResourceType(
         String extension)
      {
         ResourceType resourceType = null;
         
         for (ResourceType tempResourceType : ResourceType.values())
         {
            for (String tempExtension : tempResourceType.EXTENSIONS)
            {
               if (tempExtension.equals(extension) == true)
               {
                  resourceType = tempResourceType;
                  break;
               }
            }
         }
         
         return (resourceType);
      }
      
      final String[] EXTENSIONS;
      
      final String RESOURCE_CLASS;
   }
   
   
   public static boolean exists(String id)
   {
      return (resourceMap.containsKey(id));
   }
   
   
   public static Resource getResource(String id)
   {
      return (resourceMap.get(id));
   }
   
   
   public String getId()
   {
      return (ID);
   }
   
   private final String ID;
   
   protected boolean isLoaded;
}
