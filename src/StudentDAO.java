/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Software
 */
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // ==================== CRUD Operations ====================

    /**
     * Adds a new student to the database
     * @param student The student to add
     * @throws SQLException If database error occurs
     */
    public static void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, age, course) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getCourse());
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    student.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Retrieves all students from the database
     * @return List of all students
     * @throws SQLException If database error occurs
     */
    public static List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("course")
                ));
            }
        }
        return students;
    }

    /**
     * Updates an existing student
     * @param student The student with updated information
     * @throws SQLException If database error occurs
     */
    public static void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getCourse());
            pstmt.setInt(4, student.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a student from the database
     * @param id The ID of the student to delete
     * @throws SQLException If database error occurs
     */
    public static void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // ==================== Search Operation ====================

    /**
     * Searches students by keyword (matches against name, course, or ID)
     * @param keyword Search term
     * @return List of matching Student objects
     * @throws SQLException If database error occurs
     */
    public static List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> results = new ArrayList<>();
        String sql = "SELECT * FROM students " +
                     "WHERE LOWER(name) LIKE LOWER(?) " +
                     "OR LOWER(course) LIKE LOWER(?) " +
                     "OR CAST(id AS CHAR) LIKE ? " +
                     "ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + keyword + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("course")
                    ));
                }
            }
        }
        return results;
    }

    // ==================== Import/Export Operations ====================

    /**
     * Exports all students to a CSV file
     * @param file The target file
     * @throws IOException If file operation fails
     * @throws SQLException If database error occurs
     */
    public static void exportToCSV(File file) throws IOException, SQLException {
        try (PrintWriter writer = new PrintWriter(file)) {
            // Write header
            writer.println("ID,Name,Age,Course");
            
            // Write data
            for (Student student : getAllStudents()) {
                writer.printf("%d,%s,%d,%s%n",
                    student.getId(),
                    escapeCsv(student.getName()),
                    student.getAge(),
                    escapeCsv(student.getCourse()));
            }
        }
    }

    /**
     * Imports students from a CSV file
     * @param file The source file
     * @throws IOException If file operation fails
     * @throws SQLException If database error occurs
     */
    public static void importFromCSV(File file) throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = parseCsvLine(line);
                if (data.length >= 4) {
                    Student student = new Student(
                        Integer.parseInt(data[0].trim()),
                        data[1].trim(),
                        Integer.parseInt(data[2].trim()),
                        data[3].trim()
                    );
                    
                    if (studentExists(student.getId())) {
                        updateStudent(student);
                    } else {
                        addStudent(student);
                    }
                }
            }
        }
    }

    // ==================== Helper Methods ====================

    private static boolean studentExists(int id) throws SQLException {
        String query = "SELECT 1 FROM students WHERE id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder buffer = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append(c);
            }
        }
        values.add(buffer.toString());
        
        return values.toArray(new String[0]);
    }
}
