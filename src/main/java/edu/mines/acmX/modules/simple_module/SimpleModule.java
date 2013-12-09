package edu.mines.acmX.modules.simple_module;


import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;

import edu.mines.acmX.exhibit.input_services.hardware.BadDeviceFunctionalityRequestException;
import processing.core.PConstants;
import processing.core.PImage;
import edu.mines.acmX.exhibit.input_services.hardware.BadFunctionalityRequestException;
import edu.mines.acmX.exhibit.input_services.hardware.devicedata.RGBImageInterface;
import edu.mines.acmX.exhibit.input_services.hardware.UnknownDriverRequest;
import edu.mines.acmX.exhibit.input_services.hardware.drivers.InvalidConfigurationFileException;
import edu.mines.acmX.exhibit.module_management.modules.ProcessingModule;
import edu.mines.acmX.exhibit.stdlib.input_processing.imaging.RGBImageUtilities;


/**
 * A simple module example to demonstrate communicating with the
 * HardwareManager all managing received events within a ProcessingModule
 *   
 * @author Matt Wesemann
 *
 */
public class SimpleModule extends ProcessingModule {

	private RGBImageInterface imageDriver;

	/*
	 * A processing module inherits the paradigms of a Processing applet. Here,
	 * we implement the setup method that acts as an entry point for our module.
	 * As such, we initialize a lot of our member variables and register for
	 * events here.
	 */
	public void setup() {
		/*
		 * To actually retrieve the driver, such that we can receive
		 * information, requires requesting it from our instance of the
		 * HardwareManager.
		 * 
		 * In this case, we want to set the size to be the same as the rgbimage
		 * we are going to receive from the driver.
		 */
		try {
			imageDriver = (RGBImageInterface) getInitialDriver("rgbimage");
			size(imageDriver.getRGBImageWidth(), imageDriver.getRGBImageHeight());
		} catch (BadFunctionalityRequestException e) {
			System.out.println("Functionality unknown (may not be supported)");
			e.printStackTrace();
		} catch (UnknownDriverRequest e) {
			System.out.println("Unknown Driver Rquest.");
			e.printStackTrace();
		} catch (InvalidConfigurationFileException e){
			System.out.println("InvalidConfigurationFileException");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("RemoteException");
			e.printStackTrace();
		} catch (BadDeviceFunctionalityRequestException e) {
			System.out.println("BadDeviceFunctionalityRequestException");
			e.printStackTrace();
		}
	}


	public void draw() {

		/*
		 * The RGBImageInterface provides getVisualData() as a method to
		 * retrieve the raw data from a driver that supports the "rgbimage"
		 * functionality.
		 * 
		 * Here we grab this ByteBuffer, convert it into a BufferedImage, and
		 * then finally into a PImage (Processing's form of an image) so that
		 * we may display it on the screeen.
		 */
		ByteBuffer rawRGBImageData = imageDriver.getVisualData();
		BufferedImage bImg = RGBImageUtilities.byteBufferToImage(
				rawRGBImageData,
				imageDriver.getRGBImageWidth(),
				imageDriver.getRGBImageHeight());
		PImage pImg = buffImageToPImage(bImg);
		image(pImg, 0, 0);
	}


	/**
	 * A utility function to convert a BufferedImage into a PImage.
	 * @param bimg BufferedImage to convert
	 * @return a PImage
	 */
	public PImage buffImageToPImage(BufferedImage bimg) {
		PImage img = new PImage(bimg.getWidth(), bimg.getHeight(), PConstants.ARGB);
		bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
		return img;
	}
}

