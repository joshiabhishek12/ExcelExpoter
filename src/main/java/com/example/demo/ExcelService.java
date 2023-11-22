package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExcelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDataChunkFromTable(String tableName, int offset, int limit) {
        String query = "SELECT * FROM " + tableName + " LIMIT ?, ?";

        return jdbcTemplate.queryForList(query, offset, limit);
    }

    public List<Map<String, Object>> getDataForColumn(String tableName, String columnName, int offset, int limit) {
        String query = "SELECT " + columnName + " FROM " + tableName + " LIMIT ?, ?";
        return jdbcTemplate.queryForList(query, offset, limit);
    }
}


