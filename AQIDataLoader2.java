package srcProgramming2;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AQIDataLoader2 {

    public static void main(String[] args) {
        String fileBasePath = "/Users/surendrasetty/Documents/";
        String[] fileNames = {
                "ad_viz_plotval_data.csv",
                "ad_viz_plotval_data-2.csv",
                "ad_viz_plotval_data-4.csv",
                "ad_viz_plotval_data-5.csv",
                "ad_viz_plotval_data-6.csv",
                "ad_viz_plotval_data-7.csv",
                "ad_viz_plotval_data-8.csv"
        };
        int totalRows = 0;
        for (String fileName : fileNames) {
            try (CSVReader reader = new CSVReader(new FileReader(fileBasePath + fileName))) {
                totalRows += reader.readAll().size() - 1; // Subtract header row
            } catch (IOException | CsvException e) {
                System.err.println("Error pre-scanning file: " + fileName);
                e.printStackTrace();
            }
        }


        ArrayList<String> dailyAQI = new ArrayList<>(totalRows);
        Hashtable<Integer, String> sites = new Hashtable<>();
        Hashtable<Integer, String> aqsParameters = new Hashtable<>();

        for (String fileName : fileNames) {
            try (CSVReader reader = new CSVReader(new FileReader(fileBasePath + fileName))) {
                List<String[]> allRows = reader.readAll();
              
                for (String[] row : allRows.subList(1, allRows.size())) {
                    dailyAQI.add(String.join(",", row));
                    
                 
                    int siteId = Integer.parseInt(row[2]);
                    sites.putIfAbsent(siteId, row[7]);
                    
                    int aqsCode = Integer.parseInt(row[10]);
                    aqsParameters.putIfAbsent(aqsCode, row[11]);
                }
            } catch (IOException | CsvException | NumberFormatException e) {
                System.err.println("Error processing file: " + fileName);
                e.printStackTrace();
            }
        }

        // Display all the names of the unique data collection sites
        System.out.println("Unique data collection sites:");
        sites.values().stream().distinct().forEach(System.out::println);

        // Accept a site name from the user and report daily AQI values for each AQS parameters
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a site name to report daily AQI values:");
        String siteName = scanner.nextLine();

        int siteNameIndex = 7; 
        int aqsCodeIndex = 10; 
        dailyAQI.stream()
                .filter(row -> row.split(",")[siteNameIndex].equals(siteName))
                .forEach(row -> {
                    String[] rowData = row.split(",");
                    System.out.println("AQS Parameter: " + aqsParameters.get(Integer.parseInt(rowData[aqsCodeIndex])) 
                                       + " - Daily AQI Value: " + rowData[6]); 
                });

        scanner.close();
    }
}