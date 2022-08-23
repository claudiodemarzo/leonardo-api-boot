package it.leonardo.leonardoapiboot.utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageUtils {

    public static byte[] encodeWebp(Mat image, int quality) {
        MatOfInt parameters = new MatOfInt(Imgcodecs.IMWRITE_WEBP_QUALITY, quality);
        MatOfByte output = new MatOfByte();
        if (Imgcodecs.imencode(".webp", image, output, parameters))
            return output.toArray();
        else
            throw new IllegalStateException("Failed to encode the image as webp with quality " + quality);
    }
}
