package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiImageBox;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TextureDisplayer extends WindowParent {
	
	private EGuiButton previous, next;
	private EGuiImageBox imageBox;
	private DynamicTextureHandler handler;
	private Path path;
	private File file;
	private EArrayList<Path> paths;
	private EArrayList<File> files;
	private int curPath = 0;
	private int curFile = 0;
	private DynamicTextureHandler workingHandler;
	
	private ResourceLocation loc;
	private boolean centered = false;
	private boolean navigationDrawn = false;
	
	//-----------------------------
	//TextureDisplayer Constructors
	//-----------------------------
	
	public TextureDisplayer() { this((DynamicTextureHandler) null); }
	public TextureDisplayer(Path pathIn) { path = pathIn; }
	public TextureDisplayer(File fileIn) { file = fileIn; }
	public TextureDisplayer(DynamicTextureHandler textureIn) { handler = textureIn; }
	
	//---------------------------
	//EnhancedGuiObject Overrides
	//---------------------------
	
	@Override
	public void initGui() {
		setDimensions(250, 250);
		setResizeable(true);
		setMaximizable(true);
		setMinDims(100, 100);
		setObjectName("Texture Viewer");
		
		windowIcon = EMCResources.textureViewerIcon;
		
		parseForImage();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		int w = MathHelper.clamp_int((width - 10 - 30) / 2, 0, 100);
		
		imageBox = new EGuiImageBox(this, startX + 5, startY + 5, width - 10, navigationDrawn ? height - 24 : height - 10);
		previous = new EGuiButton(this, midX - 10 - w, endY - 18, w, 16, "Previous");
		next = new EGuiButton(this, midX + 10, endY - 18, w, 16, "Next");
		
		StaticEGuiObject.setVisible(navigationDrawn, previous, next);
		
		imageBox.setImage(loc);
		imageBox.setCenterImage(centered);
		
		addObject(imageBox, previous, next);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == previous) { previousImage(); }
		if (object == next) { nextImage(); }
	}
	
	@Override
	public void close() {
		if (handler != null) { handler.destroy(); }
		if (workingHandler != null) { workingHandler.destroy(); }
		super.close();
	}
	
	//---------------------------------
	//TextureDisplayer Internal Methods
	//---------------------------------
	
	private void parseForImage() {
		if (handler != null) {
			ResourceLocation loc = handler.getTextureLocation();
			imageBox.setImage(loc);
			imageBox.setCenterImage(false);
		}
		
		if (path != null) {
			try {
				//attempt to parse into image
				InputStream stream = Files.newInputStream(path);
				if (stream != null) {
					BufferedImage img = ImageIO.read(stream);
					stream.close();
					
					workingHandler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
					if (workingHandler != null) {
						loc = workingHandler.getTextureLocation();
						
						int w = workingHandler.getTextureWidth();
						int h = workingHandler.getTextureHeight();
						
						centered = (w == h);
					}
				}
				
				if (path.getParent() != null) {
					EArrayList<Path> unprocessed = Files.list(path.getParent()).collect(EArrayList.toEArrayList());
					paths = unprocessed.stream().filter(p -> isImage(p.toString())).collect(EArrayList.toEArrayList());
					
					if (paths.size() > 1) {
						navigationDrawn = true;
						
						int i = 0;
						for (Path p : paths) {
							if (p.equals(path)) { break; }
							i++;
						}
						if (i < paths.size()) { curPath = i; }
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		if (file != null) {
			try {
				BufferedImage img = ImageIO.read(file);
				workingHandler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
				if (workingHandler != null) {
					loc = workingHandler.getTextureLocation();
					
					int w = workingHandler.getTextureWidth();
					int h = workingHandler.getTextureHeight();
					
					centered = (w == h);
				}
				
				if (file.getParentFile() != null) {
					EArrayList<File> unprocessed = new EArrayList<File>().addA(file.getParentFile().listFiles());
					files = unprocessed.stream().filter(f -> isImage(f.getPath())).collect(EArrayList.toEArrayList());
					
					if (files.size() > 1) {
						navigationDrawn = true;
						
						int i = 0;
						for (File f : files) {
							if (f.getPath().equals(file.getPath())) { break; }
							i++;
						}
						if (i < files.size()) { curFile = i; }
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private void previousImage() {
		if (file != null) {
			if (workingHandler != null) { workingHandler.destroy(); workingHandler = null;  }
			curFile--;
			if (curFile < 0) { curFile = files.size() - 1; }
			loadImage(true);
		}
		else if (path != null) {
			if (workingHandler != null) { workingHandler.destroy(); workingHandler = null;  }
			curPath--;
			if (curPath < 0) { curPath = paths.size() - 1; }
			loadImage(false);
		}
	}
	
	private void nextImage() {
		if (file != null) {
			if (workingHandler != null) { workingHandler.destroy(); workingHandler = null; }
			curFile++;
			if (curFile == files.size()) { curFile = 0; }
			loadImage(true);
		}
		else if (path != null) {
			if (workingHandler != null) { workingHandler.destroy(); workingHandler = null;  }
			curPath++;
			if (curPath == paths.size()) { curPath = 0; }
			loadImage(false);
		}
	}
	
	private void loadImage(boolean isFile) {
		if (isFile) {
			if (files != null) {
				try {
					BufferedImage img = ImageIO.read(files.get(curFile));
					workingHandler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
					if (workingHandler != null) {
						loc = workingHandler.getTextureLocation();
						imageBox.setImage(loc);
					}
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		else if (paths != null) {	
			try {
				InputStream stream = Files.newInputStream(paths.get(curPath));
				if (stream != null) {
					BufferedImage img = ImageIO.read(stream);
					stream.close();
						
					workingHandler = new DynamicTextureHandler(Minecraft.getMinecraft().renderEngine, img);
					if (workingHandler != null) {
						loc = workingHandler.getTextureLocation();
						imageBox.setImage(loc);
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private boolean isImage(String path) {
		return path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".gif") || path.endsWith(".tga") || path.endsWith(".bmp");
	}

}
