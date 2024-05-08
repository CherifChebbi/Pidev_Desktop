package controllers.Exporter;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import models.Pays;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import com.itextpdf.text.pdf.BarcodeQRCode;

public class PdfExporter {

    public static void export(ObservableList<Pays> data) {
        String filePath = "C:\\Users\\cheri\\Downloads\\exported_Pays.pdf"; // Specify the file path

        Document document = new Document(PageSize.A4); // Set page size
        document.setMargins(20, 20, 50, 50); // Add margins to create a border effect
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add page border
            document.add(new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()));

            // Add logo at the top left corner
            addLogo(document);

            // Add title
            addTitle(document, "Liste des pays");

            // Add data as table
            addData(document, data);

            document.close();
            System.out.println("Data exported to PDF successfully.");
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addLogo(Document document) throws DocumentException {
        try {
            URL imageUrl = PdfExporter.class.getResource("/images/logo1.png");
            if (imageUrl != null) {
                com.itextpdf.text.Image logo = Image.getInstance(imageUrl);
                logo.scaleToFit(100, 100); // Adjust size as needed
                logo.setAlignment(Element.ALIGN_LEFT);
                document.add(logo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTitle(Document document, String title) throws DocumentException {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLDITALIC, BaseColor.RED); // Specify font
        Paragraph titleParagraph = new Paragraph(title, font); // Apply font to title
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(20); // Add some spacing after the title
        document.add(titleParagraph);
    }

    private static void addData(Document document, ObservableList<Pays> data) throws DocumentException {
        PdfPTable table = new PdfPTable(8); // Create 8-column table
        table.setWidthPercentage(100); // Set table width to 100%

        // Add headers
        String[] headers = {"ID", "Nom", "Description", "Langue", "Continent", "Nombre de Villes", "Latitude", "Longitude"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Center align header text
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // Set header background color
            table.addCell(cell);
        }

        // Add data rows
        for (Pays pays : data) {
            table.addCell(String.valueOf(pays.getId_pays()));
            table.addCell(pays.getNom_pays());
            // Generate QR code for description
            BarcodeQRCode qrCode = new BarcodeQRCode(pays.getDesc_pays(), 1000, 1000, null);
            Image qrCodeImage = qrCode.getImage();
            qrCodeImage.scaleAbsolute(50, 50); // Adjust size as needed;
            PdfPCell qrCodeCell = new PdfPCell(qrCodeImage);
            qrCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(qrCodeCell);
            //table.addCell(pays.getDesc_pays());
            table.addCell(pays.getLangue());
            table.addCell(pays.getContinent());
            table.addCell(String.valueOf(pays.getNb_villes()));
            table.addCell(String.valueOf(pays.getLatitude()));
            table.addCell(String.valueOf(pays.getLongitude()));
        }

        document.add(table); // Add table to document
    }
}
