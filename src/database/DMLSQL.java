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
    public void updateData(String tableName, String[] setColumns, Object[] setValues, Object conditionValue) throws SQLException {
        if (setColumns.length != setValues.length) {
            throw new IllegalArgumentException("Mismatched number of columns/values or conditions");
        }

        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        // Append set column=value pairs
        for (int i = 0; i < setColumns.length; i++) {
            query.append(setColumns[i]).append(" = ?");
            if (i < setColumns.length - 1) {
                query.append(", ");
            }
        }

        query.append(" WHERE id = ?");

        try (PreparedStatement statement = this.databaseManager.getConnection().prepareStatement(query.toString())) {

            // Set values for SET clause
            int parameterIndex = 1;
            for (Object setValue : setValues) {
                statement.setObject(parameterIndex++, setValue);
            }

            // Set values for WHERE clause
            statement.setObject(parameterIndex++, conditionValue);

            // Execute update query
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
        StringBuilder query = new StringBuilder("SELECT ");

        if (columns == null || columns.length == 0 || columns[0].equals("*")) {
            query.append("*"); // Select all columns
        } else {
            for (int i = 0; i < columns.length; i++) {
                query.append(columns[i]);
                if (i < columns.length - 1) {
                    query.append(", ");
                }
            }
        }

        query.append(" FROM ").append(mainTable);

        // Append JOIN clauses for each join table and condition
        if (joinTables != null && joinConditions != null && joinTables.length == joinConditions.length) {
            for (int i = 0; i < joinTables.length; i++) {
                query.append(" JOIN ").append(joinTables[i]).append(" ON ").append(joinConditions[i]);
            }
        }

        // Append WHERE clause if condition parameters are provided
        if (conditionColumns != null && conditionColumns.length > 0 && operators != null && operators.length > 0
                && conditionValues != null && conditionColumns.length == operators.length
                && operators.length == conditionValues.length) {
            query.append(" WHERE ");

            for (int i = 0; i < conditionColumns.length; i++) {
                if (i > 0) {
                    // Append logical operator (AND or OR) between conditions
                    query.append(useOr ? " OR " : " AND ");
                }
                query.append(conditionColumns[i]).append(" ").append(operators[i]).append(" ?");
            }
        }

        try (PreparedStatement statement = databaseManager.getConnection().prepareStatement(query.toString())) {

            // Set values for condition parameters in the WHERE clause
            if (conditionColumns != null && conditionColumns.length > 0 && operators != null && operators.length > 0
                    && conditionValues != null && conditionColumns.length == operators.length
                    && operators.length == conditionValues.length) {
                for (int i = 0; i < conditionValues.length; i++) {
                    statement.setObject(i + 1, conditionValues[i]);
                }
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
    public Object[] findData(String mainTable, String[] columns, String[] joinTables, String[] joinConditions, String[] conditionColumns, String[] operators, Object[] conditionValues, boolean useOr) throws SQLException {
        List<Object[]> results = selectData(mainTable, columns, joinTables, joinConditions, conditionColumns, operators, conditionValues, useOr);

        if (!results.isEmpty()) {
            return results.get(0); // Return the first result (assuming findData returns a single record)
        }

        return null; // Return null if no matching record is found
    }
}
