package com.example;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Merge 
{
    public static void main( String[] args )
    {

        String inputFolder = selectFolder();
        
        try {
            mergeFolder(inputFolder);
            System.out.println("Merged successfully, saved output in your downloads folder!");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void mergeFolder(String inputFolderPath) throws Exception {
        File folder = new File(inputFolderPath);

        // Ensure the folder path exists and is a directory
        if (!folder.exists() || !folder.isDirectory()) {
            throw new Exception("Given folder directory was not found or did not contain any files!");
        }

        // Store all .pdf files in an array of File objects
        File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        if (pdfFiles == null || pdfFiles.length == 0) {
            throw new Exception("No pdf files found within the given folder destination!");
        }

        String outputPath = getOutputPath();

        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(outputPath);

        for (File pdfFile : pdfFiles) {
            pdfMerger.addSource(pdfFile);
        }

        pdfMerger.mergeDocuments(null);
    }

    public static String selectFolder(){
        System.out.println("Select a folder storing PDF files you wish to merge!");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION){
            File chosenFile = fileChooser.getSelectedFile();
            return chosenFile.getAbsolutePath();
        }
        return null;
    }

    // Get output path in the user's downloads folder and save file name with date/seconds/milliseconds
    public static String getOutputPath() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-ss-SSS");
        String currentTime = simpleDateFormat.format(new Date());
        String outputFileName = "merged-" + currentTime + ".pdf";

        String homePath = System.getProperty("user.home");
        String downloadsPath = homePath + File.separator + "Downloads";

        return downloadsPath + File.separator + outputFileName;
    }
}
