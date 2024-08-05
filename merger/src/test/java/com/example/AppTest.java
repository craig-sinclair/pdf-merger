package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;


// Warning: for testing purposes 2 PDF files shall be saved to your downloads folder!
// Assumes no pdf files titled test1 or test2 are present in downloads yet
public class AppTest 
{

    String homePath = System.getProperty("user.home");
    String downloads = homePath + File.separator + "Downloads";
    Path tempDir = Paths.get(downloads);

    @Test
    public void testMergeFolder() throws Exception {
        File pdf1 = createTempPdf(tempDir.resolve("test1.pdf"));
        File pdf2 = createTempPdf(tempDir.resolve("test2.pdf"));

        Merge.mergeFolder(tempDir.toString());

        File[] files = tempDir.toFile().listFiles((dir, name) -> name.startsWith("merged-") && name.endsWith(".pdf"));
        assertNotNull(files);
        assertNotEquals(0, files.length);

        try (PDDocument mergedDoc = PDDocument.load(files[0])) {
            assertEquals(2, mergedDoc.getNumberOfPages());
        }
    }

    @Test
    public void testMergeFolderNoPdfs() {
        Exception exception = assertThrows(Exception.class, () -> {
            Merge.mergeFolder(tempDir.toString());
        });

        String expectedMessage = "No pdf files found within the given folder destination!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testMergeFolderInvalidPath() {

        String invalidPath = "12345";
        Exception exception = assertThrows(Exception.class, () -> {
            Merge.mergeFolder(invalidPath);
        });

        String expectedMessage = "Given folder directory was not found or did not contain any files!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Helper function for creation of PDF files
    private File createTempPdf(Path path) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(path.toFile());
        }
        return path.toFile();
    }
}
