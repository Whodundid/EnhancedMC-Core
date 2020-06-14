package com.Whodundid.core.util.resourceUtil;

import com.Whodundid.core.EnhancedMC;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class EResource {
	
	private DynamicTextureHandler handler;
	private String domain;
	private String path;
	
	public EResource(String domainIn, String pathIn) {
		domain = domainIn;
		path = pathIn;
	}
	
	//-----------------
	//EResource Methods
	//-----------------
	
	public boolean register() {
		if (domain != null && path != null) {
			handler = getHandler(domain, path);
			return (handler != null);
		}
		return false;
	}
	
	public void destroy() {
		if (handler != null) {
			handler.destroy();
		}
	}
	
	//-----------------
	//EResource Getters
	//-----------------
	
	public String getFullPath() { return "/assets/" + domain + "/" + path; }
	public String getDomain() { return domain; }
	public String getPath() { return path; }
	public DynamicTextureHandler getHandler() { return handler; }
	public ResourceLocation getResource() { return (handler != null) ? handler.getTextureLocation() : null; }
	
	//-----------------
	//EResource Getters
	//-----------------
	
	public EResource setDomain(String domainIn) { domain = domainIn; return this; }
	public EResource setPath(String pathIn) { path = pathIn; return this; }
	public EResource setFullPath(String domainIn, String pathIn) { domain = domainIn; path = pathIn; return this; }
	
	//--------------------------
	//EResource Internal Methods
	//--------------------------
	
	private DynamicTextureHandler getHandler(String domain, String path) {
		BufferedImage img = getImage(domain, path);
		if (handler != null) { handler.destroy(); }
		
		if (img != null) {
			DynamicTextureHandler handler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
			return handler;
		}
		
		return null;
	}
	
	private BufferedImage getImage(String domain, String path) {
		String file = "/assets/" + domain + "/" + path;
		
		try {
			BufferedImage img = null;
			FileSystem filesystem = null;
			URL url = EnhancedMC.class.getResource(file);
			
			if (url != null) {
				URI uri = url.toURI();
				Path pathTest = null;

				if ("file".equals(uri.getScheme())) {
					pathTest = Paths.get(EnhancedMC.class.getResource(file).toURI());
				}
				else {
					filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
					pathTest = filesystem.getPath(file);
				}
				
				Optional<Path> obj = Files.walk(pathTest).findFirst();
				
				Path thePath = obj.get();
				
				if (thePath != null) {
					
					try {
						File dir = thePath.toFile();
						
						try {
							img = ImageIO.read(dir);
						}
						catch (IIOException e) {
							System.err.println(dir + " is a directory!");
						}
					}
					catch (UnsupportedOperationException e) {
						try {
							//attempt to parse into image
							InputStream stream = Files.newInputStream(thePath);
							if (stream != null) {
								img = ImageIO.read(stream);
								stream.close();
							}
						}
						catch (IIOException | FileSystemException q) {
							System.err.println(thePath + " is a directory!");
						}
					}
					
				}
			}
			else {
				System.err.println("Error: File: '" + file + "' not found!");
			}
			
			//close the file system to prevent errors!
			if (filesystem != null) { filesystem.close(); }
			
			return img;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
