/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import interfaces.ProgramFiles;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author wests
 */
public class ReturnPhoto {

    public static Image ReturnPhoto(String trn) {
        File folder = new File(ProgramFiles.IMAGE_DIR);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {

                String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
                //compare files
                if (filename[0].equalsIgnoreCase(trn)) {
                    try {
                        BufferedImage img = ImageIO.read(file.getAbsoluteFile());
                        return SwingFXUtils.toFXImage(img, null);
                    } catch (IOException ex) {
                        Logger.getLogger(ReturnPhoto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
        return null;
    }
}
