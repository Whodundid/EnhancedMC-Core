package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.resourceUtil.DynamicTextureHandler;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowImageBox;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
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
	
	private WindowButton previous, next, open;
	private WindowImageBox imageBox;
	private DynamicTextureHandler handler;
	private Path path;
	private File file;
	private EArrayList<Path> paths;
	private EArrayList<File> files;
	private int curPath = 0;
	private int curFile = 0;
	private DynamicTextureHandler workingHandler;
	private boolean stretched = false;
	
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
	
	//----------------------
	//WindowObject Overrides
	//----------------------
	
	@Override
	public void initWindow() {
		setDimensions(422, 250);
		setResizeable(true);
		setMaximizable(true);
		setMinDims(144, 100);
		setObjectName("Texture Viewer");
		
		windowIcon = EMCResources.textureViewerIcon;
		
		parseForImage();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		int w = MathHelper.clamp_int((width - 10 - (width / 6)) / 2, 50, 100);
		
		imageBox = new WindowImageBox(this, startX + 5, startY + 5, width - 10, navigationDrawn ? height - 30 : height - 10);
		previous = new WindowButton(this, midX - (width / 40) - w, imageBox.endY + 4, w, 16, "Previous");
		next = new WindowButton(this, midX + (width / 40), imageBox.endY + 4, w, 16, "Next");
		open = new WindowButton(this, midX - 8, imageBox.endY + 4, 16, 16).setTextures(EMCResources.guiFileUpButton, EMCResources.guiFileUpButtonSel);
		
		open.setHoverText("Open on Computer");
		
		if (previous.endX > (open.startX - 1)) { previous.setDimensions(open.startX - 1 - w, previous.startY, w, previous.height); }
		if (open.endX > (next.startX - 1)) { next.setDimensions(open.endX + 1, next.startY, w, next.height); }
		
		setVisible(navigationDrawn, previous, next);
		open.setVisible(file != null ? files.get(curFile) != null : false);
		
		imageBox.setImage(loc);
		imageBox.setCenterImage(centered);
		
		addObject(imageBox, previous, next, open);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == previous) { previousImage(); }
		if (object == next) { nextImage(); }
		if (object == open) { EUtil.openFile(files.get(curFile)); }
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
			loc = handler.getTextureLocation();
			centered = (handler.getTextureWidth() == handler.getTextureHeight());
		}
		
		if (path != null) {
			try {
				setObjectName(path.toString());
				
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
				setObjectName(file.getName());
				
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
					File newFile = files.get(curFile);
					setObjectName(newFile.getName());
					if (getHeader() != null) { getHeader().setTitle(newFile.getName()); }
					
					BufferedImage img = ImageIO.read(newFile);
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
				Path newPath = paths.get(curPath);
				setObjectName(newPath.getFileName().toString());
				if (getHeader() != null) { getHeader().setTitle(newPath.getFileName().toString()); }
				
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
