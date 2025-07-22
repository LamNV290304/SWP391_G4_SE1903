/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import DTO.SalesEmployeeStatisticDto;
import DTO.SoldProductDetailDto;

import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

import jakarta.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author duckh
 */
public class ExcelExporter {

    public static void exportCashierSummaryStatistics(
            List<SalesEmployeeStatisticDto> statistics,
            BigDecimal totalRevenue,
            int totalOrder,
            OutputStream out) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Cashier Summary");

            if (statistics == null || statistics.isEmpty()) {
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("Không có dữ liệu thống kê.");
            } else {
                // Header
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Employee ID");
                headerRow.createCell(1).setCellValue("Employee Name");
                headerRow.createCell(2).setCellValue("Total Invoice");
                headerRow.createCell(3).setCellValue("Total Amount");

                // Data
                int rowNum = 1;
                for (SalesEmployeeStatisticDto dto : statistics) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getEmployeeID());
                    row.createCell(1).setCellValue(dto.getFullName());
                    row.createCell(2).setCellValue(dto.getTotalOrders());

                    BigDecimal revenue = dto.getTotalRevenue() != null ? dto.getTotalRevenue() : BigDecimal.ZERO;
                    row.createCell(3).setCellValue(revenue.doubleValue());
                }

             
                
            }

            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Lỗi khi xuất file Excel.", e);
        }
    }

    public static void exportAllStatistics(
            List<SalesEmployeeStatisticDto> employeeStats,
            OutputStream out) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tổng hợp nhân viên");
        if (employeeStats == null || employeeStats.isEmpty()) {
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Không có dữ liệu thống kê.");
        } else {
            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("STT");
            headerRow.createCell(1).setCellValue("Mã NV");
            headerRow.createCell(2).setCellValue("Tên nhân viên");
            headerRow.createCell(3).setCellValue("Tổng doanh thu");
            headerRow.createCell(4).setCellValue("Tổng đơn hàng");
            headerRow.createCell(5).setCellValue("Doanh thu TB/Đơn");

            // Data
            int rowNum = 1;
            int index = 1;
            for (SalesEmployeeStatisticDto dto : employeeStats) {
                Row row = sheet.createRow(rowNum++);

                BigDecimal totalRevenue = dto.getTotalRevenue() != null ? dto.getTotalRevenue() : BigDecimal.ZERO;
                int totalOrders = dto.getTotalOrders();
                BigDecimal avgRevenuePerOrder = totalOrders > 0
                        ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                row.createCell(0).setCellValue(index++);
                row.createCell(1).setCellValue(dto.getEmployeeID());
                row.createCell(2).setCellValue(dto.getFullName());
                row.createCell(3).setCellValue(totalRevenue.doubleValue());
                row.createCell(4).setCellValue(totalOrders);
                row.createCell(5).setCellValue(avgRevenuePerOrder.doubleValue());
            }

            // Auto-size columns
            for (int i = 0; i <= 5; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        workbook.write(out);
        workbook.close();
    }

    public static void exportSaleProductStatistics(List<SoldProductDetailDto> details, int totalQuantity, BigDecimal totalAmount, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sold Products Summary");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Product ID", "Product Name", "Quantity Sold", "Unit Price", "Amount"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data rows
        for (int i = 0; i < details.size(); i++) {
            SoldProductDetailDto dto = details.get(i);
            Row row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue(dto.getProductID());
            row.createCell(1).setCellValue(dto.getProductName());
            row.createCell(2).setCellValue(dto.getQuantitySold());
            row.createCell(3).setCellValue(dto.getUnitPrice().doubleValue());
            row.createCell(4).setCellValue(dto.getAmount().doubleValue());
        }

        // Total row
        Row totalRow = sheet.createRow(details.size() + 1);
        totalRow.createCell(1).setCellValue("Total:");
        totalRow.createCell(2).setCellValue(totalQuantity);
        totalRow.createCell(4).setCellValue(totalAmount.doubleValue());

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        workbook.close();
    }

}
