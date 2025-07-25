package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet("/DownloadProductTemplate")
public class DownloadProductTemplateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Template");

        // Dòng tiêu đề
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Product Name");
        header.createCell(1).setCellValue("Category ID");
        header.createCell(2).setCellValue("Import Price");
        header.createCell(3).setCellValue("Selling Price");
        header.createCell(4).setCellValue("Description");
        header.createCell(5).setCellValue("Unit ID");

        // (tuỳ chọn) dòng ví dụ
        Row sample = sheet.createRow(1);
        sample.createCell(0).setCellValue("Bánh quy");
        sample.createCell(1).setCellValue("1");
        sample.createCell(2).setCellValue("5000");
        sample.createCell(3).setCellValue("7000");
        sample.createCell(4).setCellValue("Ngon, giòn, hấp dẫn");
        sample.createCell(5).setCellValue("1");

        // Thiết lập headers để tải file
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=product_template.xlsx");

        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }
}
