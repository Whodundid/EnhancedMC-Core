package com.Whodundid.playerInfo.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SkinContainer {
	
	private String name;
	private File skinFile;
	private File capeFile;
	private boolean isAlexSkin = false;
	private boolean hasCape = false;
	private BufferedImage skin = null;
	private BufferedImage cape = null;
	private String uuid;
	
	//--------------------------
	//SkinContainer Constructors
	//--------------------------
	
	public SkinContainer(String nameIn, File skinIn, File capeIn, boolean alexSkinIn, boolean hasCapeIn, String uuidIn) {
		name = nameIn;
		skinFile = skinIn;
		capeFile = capeIn;
		isAlexSkin = alexSkinIn;
		hasCape = hasCapeIn;
		uuid = uuidIn;
		
		parseImages();
	}
	
	//---------------------
	//SkinContainer Getters
	//---------------------
	
	public String getName() { return name; }
	public File getFile() { return skinFile; }
	public boolean isAlex() { return isAlexSkin; }
	public boolean hasCape() { return hasCape; }
	public BufferedImage getSkinImage() { return skin; }
	public BufferedImage getCapeImage() { return cape; }
	public String getUUID() { return uuid; }
	
	//------------------------------
	//SkinContainer Internal Methods
	//------------------------------
	
	private void parseImages() {
		try {
			if (skinFile.exists()) {
				BufferedImage img = ImageIO.read(skinFile);
				
				if (img.getWidth() < 64 || img.getHeight() < 64) {
					BufferedImage fix = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
					
					BufferedImage armI = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
					BufferedImage legI = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
					
					//get from original image
					int[] arm = img.getRGB(0, 16, 16, 16, null, 0, 16);
					int[] leg = img.getRGB(40, 16, 16, 16, null, 0, 16);
					
					armI.setRGB(0, 0, 16, 16, arm, 0, 16);
					legI.setRGB(0, 0, 16, 16, leg, 0, 16);
					
					Graphics2D g = fix.createGraphics();
					g.drawImage(img, 0, 0, null);
					g.drawImage(legI, 32, 48, null);
					g.drawImage(armI, 16, 48, null);
					g.dispose();
					
					skin = fix;
				}
				else {
					skin = img;
				}
			}
			
			if (capeFile.exists()) {
				BufferedImage img = ImageIO.read(capeFile);
				
				cape = img;
			}
			
			skinFile.delete();
			capeFile.delete();
		}
		catch (Exception e) { e.printStackTrace(); }
	}

}
