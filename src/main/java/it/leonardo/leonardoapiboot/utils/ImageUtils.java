package it.leonardo.leonardoapiboot.utils;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ImageUtils {

    static{
        OpenCV.loadShared();
    }

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
        int squareSize = (Math.min(Math.min(height, width), 1080));

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

    public static void downloadFile(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}
