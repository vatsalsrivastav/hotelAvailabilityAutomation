package mainCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CreateExcel {
	
	/*This method is used to generate an excel file that contains the names of the
	  hotels and their respective price per night in INR.*/
	public void generateReport(Map<String, String> hotelList) throws IOException{
		
		//Creating a new Excel workbook.
		XSSFWorkbook workbook = new XSSFWorkbook(); 
		  
        // Create a new blank sheet. 
        XSSFSheet sheet = workbook.createSheet("Hotel Details"); 
        
        Map<String, Object[]> details = new LinkedHashMap<String, Object[]>();
        
        int i = 2;		//Variable to maintain index and serial number of data values.
        
        details.put("1", new Object[]{ "S.No.", "HOTEL NAME", "PRICE/NIGHT" });
        
        for(Map.Entry<String, String> m : hotelList.entrySet()){  
        	details.put(Integer.toString(i) , new Object[]{(i - 1) , m.getKey() , "Rs. " + m.getValue()});
        	i++;
        }
        
        //Creating and inserting rows for every data set.
        Set<String> keyset = details.keySet(); 
        int rowNum = 0; 
        for (String key : keyset){ 
            Row row = sheet.createRow(rowNum++); 
            Object[] objArr = details.get(key); 
            int cellnum = 0; 
            for (Object obj : objArr){ 
                Cell cell = row.createCell(cellnum++); 
                if (obj instanceof String) 
                    cell.setCellValue((String)obj); 
                else if (obj instanceof Integer) 
                    cell.setCellValue((Integer)obj); 
            } 
        }
        
        //Auto-resizing column width for all columns.
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        
        //Try-catch block to write data to generate excel file and write in it and save it.
        try { 
        	
    		//Location to store excel file.
    		String storage = System.getProperty("user.dir") + "\\Hotel_Information.xlsx";
    		
        	//Creating and storing the final excel file overwriting any existing file with same name.
    		FileOutputStream out = new FileOutputStream(new File(storage)); 
            workbook.write(out);
            out.close();
            
            //Printing confirmation message that the excel file has been generated and stored.
            System.out.println("\nHotel_Information.xlsx successfully stored at: " + storage); 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        }
        
        //Closing the created workbook.
        workbook.close();
	}
}
