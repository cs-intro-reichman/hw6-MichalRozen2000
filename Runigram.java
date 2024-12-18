import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;
		image = scaled(tinypic,3 ,5);

		// Tests the horizontal flipping of an image:
		//image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		for (int i = 0 ; i < numRows ; i++) { 
			for (int j = 0 ; j < numCols ; j++) {
				int r = in.readInt();
				int g = in.readInt();
				int b = in.readInt();
				image[i][j] = new Color(r, g, b);


			}
		}
	
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		for (Color[] image1 : image) {
			for (int j = 0; j < image[0].length; j++) {
				print(image1[j]);
			}
			System.out.println();
		}
}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] newImage = new Color[image.length][image[0].length];
		int N = image.length;
		int M = image[0].length;
		for (int i = 0 ; i < N ; i ++){
			for (int j = 0 ; j < M ; j++){
				newImage[i][M - j - 1] = image[i][j];
			}

		}

		return newImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color[][] newImage = new Color[image.length][image[0].length];
		int N = image.length;
		int M = image[0].length;
		for (int i = 0 ; i < N ; i ++) {
			for (int j = 0 ; j < M ; j++) {
				newImage[N - i - 1][j] = image[i][j];
			}
		}
		
		return newImage;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		double r = pixel.getRed();
		double g = pixel.getGreen();
		double b = pixel.getBlue();
		int lum  = (int)(0.299 * r + 0.587 * g + 0.114 * b);
		Color grey = new Color(lum, lum, lum);

		return grey;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
				Color[][] newImage = new Color [image.length][image[0].length];
		for (int i = 0 ; i < image.length ; i++) {
			for (int j = 0 ; j < image[0].length ; j++ ){
				newImage[i][j] = luminance(image[i][j]);
			}
		}
		return newImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		Color[][] newImage = new Color[height][width];
	   
	   int h0 = image.length;
	   int w0 = image[0].length;

	   for (int i = 0 ; i < height ; i++) {
		for (int j = 0 ; j < width ; j++ ){
			int srcI = (int)((i * (double)h0) / height);
            int srcJ = (int)((j * (double)w0) / width);
			srcI = Math.min(srcI, h0 - 1);
			srcJ = Math.min(srcJ, w0 - 1);

			newImage[i][j] = image[srcI][srcJ];
	 }
  }
return newImage;
}


	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 * 
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		
		double minusAlpha = 1 - alpha;
		int r = (int)(c1.getRed() * alpha + minusAlpha * c2.getRed());
		int g = (int)(c1.getGreen() * alpha + minusAlpha * c2.getGreen());
		int b = (int)(c1.getBlue() * alpha + minusAlpha *c2.getBlue());
		Color newColor = new Color(r, g, b);

		return newColor;
	}
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] newImage = new Color[image1.length][image1[0].length];
		for (int i = 0 ; i < image1.length ; i++) {
			for (int j = 0 ; j < image1[0].length ; j++ ){
				newImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return newImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		if (source.length != target.length || source[0].length != target[0].length){
			target = scaled(target, source[0].length, source.length);
		}
		for (int i = 0; i <= n; i++) {
			double alpha = (double)(n - i)/n;
			display(blend(source, target, alpha));
			StdDraw.pause(500);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

