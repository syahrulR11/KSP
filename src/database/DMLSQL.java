/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author syahrul
 */
public class DMLSQL {
    private DatabaseManager databaseManager;

    public DMLSQL(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Method to insert data into a table using bind parameters
    public void insertData(String tableName, String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Number of columns must match number of values");
        }

        StringBuilder columnNames = new StringBuilder();
        StringBuilder valuePlaceholders = new StringBuilder();

        for (int i = 0; i < columns.length; i++) {
            columnNames.append(columns[i]);
            valuePlaceholders.append("?");
            if (i < columns.length - 1) {
                columnNames.append(", ");
                valuePlaceholders.append(", ");
            }
        }

        String query = "INSERT INTO " + tableName + " (" + columnNames.toString() + ") VALUES (" + valuePlaceholders.toString() + ")";

        try (PreparedStatement statement = this.databaseManager.getConnection().prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            statement.executeUpdate();
        }
    }

    // Method to update data in a table using bind parameters
    public void updateData(String tableName, String setColumn, Object setValue, String conditionColumn, Object conditionValue) throws SQLException {
        String query = "UPDATE " + tableName + " SET " + setColumn + " = ? WHERE " + conditionColumn + " = ?";
        try (PreparedStatement statement = this.databaseManager.getConnection().prepareStatement(query)) {
            statement.setObject(1, setValue);
            statement.setObject(2, conditionValue);
            statement.executeUpdate();
        }
    }

    // Method to delete data from a table using bind parameters
    public void deleteData(String tableName, String conditionColumn, Object conditionValue) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";
        try (PreparedStatement statement = this.databaseManager.getConnection().prepareStatement(query)) {
            statement.setObject(1, conditionValue);
            statement.executeUpdate();
        }
    }

    // Method to perform a SELECT query with bind parameters and dynamic operators
    public List<Object[]> selectData(String mainTable, String[] columns, String[] joinTables, String[] joinConditions, String[] conditionColumns, String[] operators, Object[] conditionValues, boolean useOr) throws SQLException {
        if (conditionColumns.length != operators.length || operators.length != conditionValues.length) {
            throw new IllegalArgumentException("Number of conditions, operators, and values must be the same");
        }
    
        StringBuilder columnList = new StringBuilder();
        if (columns == null || columns.length == 0 || columns[0].equals("*")) {
            columnList.append("*"); // Select all columns
        } else {
            for (int i = 0; i < columns.length; i++) {
                columnList.append(columns[i]);
                if (i < columns.length - 1) {
                    columnList.append(", ");
                }
            }
        }
    
        StringBuilder query = new StringBuilder("SELECT ").append(columnList).append(" FROM ").append(mainTable);
    
        // Join multiple tables and conditions
        if (joinTables != null && joinConditions != null && joinTables.length == joinConditions.length) {
            for (int i = 0; i < joinTables.length; i++) {
                query.append(" JOIN ").append(joinTables[i]).append(" ON ").append(joinConditions[i]);
            }
        }
    
        query.append(" WHERE ");
    
        for (int i = 0; i < conditionColumns.length; i++) {
            if (i > 0) {
                // Append logical operator (AND or OR) between conditions
                query.append(useOr ? " OR " : " AND ");
            }
            query.append(conditionColumns[i]).append(" ").append(operators[i]).append(" ?");
        }
    
        try (PreparedStatement statement = this.databaseManager.getConnection().prepareStatement(query.toString())) {
            for (int i = 0; i < conditionValues.length; i++) {
                statement.setObject(i + 1, conditionValues[i]);
            }
    
            ResultSet resultSet = statement.executeQuery();
    
            List<Object[]> results = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[columns.length];
                for (int i = 0; i < columns.length; i++) {
                    row[i] = resultSet.getObject(columns[i]);
                }
                results.add(row);
            }
    
            return results;
        }
    }

    // Method to perform a SELECT query with bind parameters and dynamic operators
    public Object[] findData(String mainTable, String[] columns, String[] joinTables, String[] joinConditions, String[] conditionColumns, String[] operators, Object[] conditionValues) throws SQLException {
        List<Object[]> results = selectData(mainTable, columns, joinTables, joinConditions, conditionColumns, operators, conditionValues, false);
        
        if (!results.isEmpty()) {
            return results.get(0); // Return the first result (assuming findData returns a single record)
        }
        
        return null; // Return null if no matching record is found
    }
}
