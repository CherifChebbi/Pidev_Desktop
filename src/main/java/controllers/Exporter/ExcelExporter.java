package controllers.Exporter;

import java.io.FileOutputStream;
import java.io.IOException;
import javafx.collections.ObservableList;
import models.Pays;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {

    public static void export(ObservableList<Pays> data) {
        String filePath = "C:\\Users\\cheri\\Downloads\\exported_Pays.xlsx"; // Specify the file path

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Exported Data");

        // Create headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nom", "Description", "Langue", "Continent", "Nombre de Villes", "Latitude", "Longitude"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Populate data
        int rowCount = 1;
        for (Pays pays : data) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(pays.getId_pays());
            row.createCell(1).setCellValue(pays.getNom_pays());
            row.createCell(2).setCellValue(pays.getDesc_pays());
            row.createCell(3).setCellValue(pays.getLangue());
            row.createCell(4).setCellValue(pays.getContinent());
            row.createCell(5).setCellValue(pays.getNb_villes());
            row.createCell(6).setCellValue(pays.getLatitude());
            row.createCell(7).setCellValue(pays.getLongitude());
        }

        // Write to file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            System.out.println("Data exported to Excel successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
