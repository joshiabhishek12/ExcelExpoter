package com.example.demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExportTableRepository extends JpaRepository<ExportTable, Long> {

	ExportTable findByQueueName(String customerName);
    // Additional custom query methods can be added here if needed
}
