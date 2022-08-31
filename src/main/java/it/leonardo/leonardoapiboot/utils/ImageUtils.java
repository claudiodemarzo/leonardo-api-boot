package it.leonardo.leonardoapiboot.utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {

    public static byte[] encodeWebp(Mat image, int quality) {
        /*int min = Math.min(image.rows(), image.cols());
        Rect roi = new Rect((image.cols() - min) / 2, (image.rows() - min) / 2, min, min);
        Mat cropped = new Mat();
        image.submat(roi).copyTo(cropped);*/
        MatOfInt parameters = new MatOfInt(Imgcodecs.IMWRITE_WEBP_QUALITY, quality);
        MatOfByte output = new MatOfByte();
        if (Imgcodecs.imencode(".webp", image, output, parameters)) {

            return output.toArray();
        } else
            throw new IllegalStateException("Failed to encode the image as webp with quality " + quality);
    }


    public static BufferedImage cropImageSquare(BufferedImage originalImage) throws IOException {

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return originalImage;
        }

        // Compute the size of the square
        int squareSize = (Math.min(height, width));

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop

        return originalImage.getSubimage(
                xc - (squareSize / 2), // x coordinate of the upper-left corner
                yc - (squareSize / 2), // y coordinate of the upper-left corner
                squareSize,            // widht
                squareSize             // height
        );
    }
}
