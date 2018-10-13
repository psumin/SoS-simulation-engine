package misc;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class ExcelHelper {

    public static Row nextRow(Row row) {
        Sheet sheet = row.getSheet();
        int rowNum = row.getRowNum();

        Row next = sheet.getRow(rowNum + 1);
        if(next == null) {
            next = sheet.createRow(rowNum + 1);
        }
        return next;
    }

    public static Cell nextCell(Cell cell) {
        Row row = cell.getRow();
        int colNum = cell.getColumnIndex();

        Cell next = row.getCell(colNum + 1);
        if(next == null) {
            next = row.createCell(colNum + 1);
        }

        return next;
    }

    public static Row getRow(Sheet sheet, int rowNum) {
        Row row = sheet.getRow(rowNum);
        if(row == null) {
            row = sheet.createRow(rowNum);
        }
        return row;
    }

    public static Cell getCell(Row row, int colNum) {
        Cell cell = row.getCell(colNum);
        if(cell == null) {
            cell = row.createCell(colNum);
        }
        return cell;
    }

    public static Cell getCell(Sheet sheet, int rowNum, int colNum) {
        Row row = getRow(sheet, rowNum);
        return getCell(row, colNum);
    }

    public static void save(Workbook workbook, String filePath) {
        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static int maxColumn(Sheet sheet) {
        int max = 0;
        for(int i = 0; i < sheet.getLastRowNum() + 1; ++i) {
            Row row = sheet.getRow(i);
            if(row != null && row.getLastCellNum() > max) {
                max = row.getLastCellNum() + 1;
            }
        }
        return max;
    }

    private static void autoSizeAllColumn(Sheet sheet) {
        for(int i = 0; i < maxColumn(sheet); ++i) {
            sheet.autoSizeColumn(i);
        }
    }

    public static void autoSizeAllColumn(Workbook workbook) {
        Iterator<Sheet> iterator = workbook.sheetIterator();
        while(iterator.hasNext()) {
            autoSizeAllColumn(iterator.next());
        }
    }
}
