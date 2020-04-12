import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.DataBufferByte;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compare {
    
    public static BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight)
            throws IOException {
        // создание выходного изображения
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        
        // Скалирование входного изображения
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return outputImage;
        
    }
    
    public static int average_colors(BufferedImage inputImage) {
        byte[] pixelWeight = ((DataBufferByte) inputImage.getRaster().getDataBuffer()).getData();
        int l = pixelWeight.length;
        System.out.println(l);
        int totalsum = 0;
        int counter = 0;
        for (int i = 0; i < l; i++) {
            totalsum += pixelWeight[i];
            counter += 1;
        }
        int averageVal = totalsum / counter;
        return averageVal;
    }
    
    public static BufferedImage greyscale(BufferedImage inputImage) {
        for (int x = 0; x < inputImage.getWidth(); ++x)
            for (int y = 0; y < inputImage.getHeight(); ++y) {
                int rgb = inputImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                
                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                inputImage.setRGB(x, y, gray);
            }
        return inputImage;
    }
    
    public static String compare_bits(BufferedImage inputImage, int inputImageAvg) {
        byte[] pixelWeight = ((DataBufferByte) inputImage.getRaster().getDataBuffer()).getData();
        int l = pixelWeight.length;
        String bitResult = "";
        for (int i = 0; i < l; i++) {
            
            if (pixelWeight[i] > inputImageAvg)
                bitResult += "1";
            else
                bitResult += "0";
        }
        return bitResult;
    }
    
    public static int hammingDifference(String bitHash1, String bitHash2) {
        int result = 0;
        for (int i = 0; i < bitHash1.length(); i++)
            if (bitHash1.charAt(i) != bitHash2.charAt(i))
                result += 1;
        return result;
    }
    
    public static void main(String[] args) {
        String inputImagePath = "C:/Users/Home/Downloads/Comparison_/images/image_1.jpg";
        String inputImagePathTwo = "C:/Users/Home/Downloads/Comparison_/images/image_10.jpg";
        //String outputImagePath = "C:/Users/Home/Downloads/Comparison_/images/image_1_resize.jpg";
        //outputImagePathTwo = "C:/Users/Home/Downloads/Comparison_/images/image_2_resize.jpg";
        
        
        File inputFile = new File(inputImagePath);
        File inputFileTwo = new File(inputImagePathTwo);
        try {
            
            int scaledWidth = 8;
            int scaledHeight = 8;
            
            //Считывание изображения
            BufferedImage inputImage = ImageIO.read(inputFile);
            //Сжатие изображения
            inputImage = Compare.resize(inputImage, scaledWidth, scaledHeight);
            //Перевод изображения в градации серого
            inputImage = Compare.greyscale(inputImage);
            int inputImageAvg = Compare.average_colors(inputImage);
            //Сравнение значений цветов пикселя со средним значением -> если больше то "1", если меньше то "0"
            String bitHash1 = Compare.compare_bits(inputImage, inputImageAvg);
            
            //Для второго
            BufferedImage inputImageTwo = ImageIO.read(inputFileTwo);
            inputImageTwo = Compare.resize(inputImageTwo, scaledWidth, scaledHeight);
            inputImageTwo = Compare.greyscale(inputImageTwo);
            int inputImageAvgTwo = Compare.average_colors(inputImageTwo);
            String bitHash2 = Compare.compare_bits(inputImageTwo, inputImageAvgTwo);
            
            
            System.out.println(bitHash1);
            System.out.println(bitHash2);
            
            //побитное сравнение двух хэшей
            int hammingDistance = Compare.hammingDifference(bitHash1, bitHash2);
            
            System.out.println("Расстояние Хэмминга " + hammingDistance + ", Схожесть равна " + (192 - hammingDistance) * 100 / 192 + "%");
//            // извлечение расширения из выходного файла для сохранения в файл
//            String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
//            String formatNameTwo = outputImagePath.substring(outputImagePathTwo.lastIndexOf(".") + 1);
//
//            // запись в файл по имени
//            ImageIO.write(inputImage, formatName, new File(outputImagePath));
//            ImageIO.write(inputImageTwo, formatNameTwo, new File(outputImagePathTwo));
//            if(((192 - hammingDistance) * 100 / 192)> 80)
//            {
//                /**/
//            }
//            else
//            {
//                /**/
//            }
            
        } catch (IOException e) {
            System.out.println("Ошибка resize'а изображения");
            e.printStackTrace();
        }
    }
}
