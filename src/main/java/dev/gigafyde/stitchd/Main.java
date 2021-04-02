package dev.gigafyde.stitchd;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

class Main {
    public static void main(String[] args) throws Exception {
//        JDA jda = JDABuilder.createLight("token").addEventListeners(new PrivateMessageListener()).build();
//        jda.awaitReady();
        List<File> files = getFiles("images");
        rewrite(files, 4);
    }

    private static List<File> getFiles(String path) throws IOException {
        return Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    private static void rewrite(List<File> files, int pages) throws IOException {
        int pagesDone = 0;
        int imageAmount = files.size();
        int quotient = imageAmount / pages;
        int remainder = imageAmount % pages;
        int pageWidth = ImageIO.read(files.get(0)).getWidth();

        int offset = 0;
        for (int a = 0; a < pages; a++) {
            BufferedImage finalPage;
            if (a == pages - 1) {
                int pageHeight = getHeightIncludingRemainder(files, quotient, offset, remainder);
                finalPage = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);
            } else {
                int pageHeight = getHeight(files, quotient, offset);
                finalPage = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);
            }
            Graphics2D g2dColumn = finalPage.createGraphics();
            BufferedImage page = ImageIO.read(files.get(offset));
            g2dColumn.drawImage(page, 0, 0, null);
            int height = page.getHeight();
            for (int b = 1; b < quotient; b++) {
                BufferedImage page2 = ImageIO.read(files.get(b + offset));
                g2dColumn.drawImage(page2, 0, height, null);
                height += page2.getHeight();
            }
            offset += quotient;
            if (a == pages - 1) {
                for (int c = 0; c < remainder; c++) {
                    BufferedImage page2 = ImageIO.read(files.get(offset + c));
                    g2dColumn.drawImage(page2, 0, height, null);
                    height += page2.getHeight();
                }
            }
            ImageIO.write(finalPage, "jpg", new File(pagesDone + 1 + ".jpg"));
            pagesDone++;
        }
    }

    private static int getHeight(List<File> files, int pages, int offset) throws IOException {
        int height = 0;
        for (int i = 0; i < pages; i++) {
            height += ImageIO.read(files.get(i + offset)).getHeight();
        }
        return height;
    }

    private static int getHeightIncludingRemainder(List<File> files, int pages, int offset, int remainder) throws IOException {
        int height = 0;
        for (int i = 0; i < (pages + remainder); i++) {
            height += ImageIO.read(files.get(i + offset)).getHeight();
        }
        return height;
    }
}




//    private static void temp() throws IOException {
//        String source = "download.zip";
//        String destination = "temp/";
//
//        try {
//            ZipFile zipFile = new ZipFile(source);
//            zipFile.extractAll(destination);
//        } catch (ZipException e) {
//            e.printStackTrace();
//        }
//    }
