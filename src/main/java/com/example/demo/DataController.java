package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;



@RestController
public class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);
    private final ExcelService excelService;

    @Value("${excel.file.path}")
    private String defaultFilePath;

    @Value("${excel.column.file.path}")
    private String defaultColumnFilePath;

    @Value("${table.name}")
    private String tableName;

    @Value("${table.names.column}")
    private String tableNameForColumn;

    public DataController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/export/data")
    public ResponseEntity<String> exportToExcel(@RequestParam(value = "filePath", required = false) String filePath) {
        try {
            int offset = 0;
            int chunkSize = 100000;

            filePath = (filePath != null && !filePath.isEmpty()) ? filePath : defaultFilePath;

            while (true) {
                List<Map<String, Object>> dataChunk = excelService.getDataChunkFromTable(tableName, offset, chunkSize);

                if (dataChunk.isEmpty()) {
                    break;
                }

                if (dataChunk.size() > chunkSize) {
                    return ResponseEntity.badRequest().body("Number of rows exceeds the limit of " + chunkSize);
                }

                ExcelGenerator.generateExcel(dataChunk, filePath);
                offset += chunkSize;

                logger.info("Exporting data to Excel: Offset = {}, Chunk Size = {}", offset, chunkSize);
            }

            logger.info("Excel file exported successfully to path: {}", filePath);
            return ResponseEntity.ok("Excel file exported successfully!");
        } catch (IOException e) {
            logger.error("Error exporting Excel file", e);
            return ResponseEntity.status(500).body("Error exporting Excel file.");
        }
    }

    @PostMapping("/export/data")
    public ResponseEntity<String> exportToExcel(@RequestBody Map<String, String> request,
                                                @RequestParam(value = "filePath", required = false) String filePath) {
        String columnName = request.get("columnName");

        if (columnName == null || columnName.isEmpty()) {
            logger.warn("Column name not provided in the request body");
            return ResponseEntity.badRequest().body("Column name must be provided in the request body.");
        }

        try {
            int offset = 0;
            int chunkSize = 100000;

            filePath = (filePath != null && !filePath.isEmpty()) ? filePath : defaultColumnFilePath;

            while (true) {
                List<Map<String, Object>> dataChunk = excelService.getDataForColumn(tableNameForColumn, columnName, offset, chunkSize);

                if (dataChunk.isEmpty()) {
                    break;
                }

                if (dataChunk.size() > chunkSize) {
                    return ResponseEntity.badRequest().body("Number of rows exceeds the limit of " + chunkSize);
                }

                ExcelGenerator.generateExcel(dataChunk, filePath);
                offset += chunkSize;

                logger.info("Exporting data to Excel for column '{}': Offset = {}, Chunk Size = {}", columnName, offset, chunkSize);
            }

            logger.info("Excel file exported successfully to path: {}", filePath);
            return ResponseEntity.ok("Excel file exported successfully!");
        } catch (IOException e) {
            logger.error("Error exporting Excel file", e);
            return ResponseEntity.status(500).body("Error exporting Excel file.");
        }
    }
}






















//@RestController
//@RequestMapping("/api")
//public class DataController {
//	 private static final Logger logger = LoggerFactory.getLogger(DataController.class);
//    @Autowired
//    private ExcelService excelService;
//
//    @Autowired
//    private ExportTableRepository exportTableRepository;
//
//    private final String defaultFilePath = "\"F:\\New folder\\output.xlsx\"";
//
//    @GetMapping("/export/data")
//    public ResponseEntity<String> exportToExcel(@RequestParam("customerName") String customerName,
//                                                @RequestParam(value = "filePath", required = false) String filePath) {
//        try {
//            int offset = 0;
//            int chunkSize = 100000;
//
//            filePath = (filePath != null && !filePath.isEmpty()) ? filePath : defaultFilePath;
//
//            // Create or retrieve the ExportTable entity for the given customer
//            ExportTable exportTable = exportTableRepository.findByQueueName(customerName);
//            if (exportTable == null) {
//                exportTable = new ExportTable();
//                exportTable.setQueueName(customerName);
//                exportTable.setFilePath(filePath);
//                exportTableRepository.save(exportTable);
//            }
//
//            while (true) {
//                List<Map<String, Object>> dataChunk = excelService.getDataChunkFromTable(exportTable.getQueueName(), offset, chunkSize);
//
//                if (dataChunk.isEmpty()) {
//                    break;
//                }
//
//                if (dataChunk.size() > chunkSize) {
//                    return ResponseEntity.badRequest().body("Number of rows exceeds the limit of " + chunkSize);
//                }
//
//                ExcelGenerator.generateExcel(dataChunk, exportTable.getFilePath());
//                offset += chunkSize;
//
//                logger.info("Exporting data to Excel: Offset = {}, Chunk Size = {}", offset, chunkSize);
//            }
//
//            logger.info("Excel file exported successfully to path: {}", exportTable.getFilePath());
//            return ResponseEntity.ok("Excel file exported successfully!");
//        } catch (IOException e) {
//            logger.error("Error exporting Excel file", e);
//            return ResponseEntity.status(500).body("Error exporting Excel file.");
//        }
//    }
//}

