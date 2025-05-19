# Student Management System 🎓

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Swing](https://img.shields.io/badge/GUI-Swing-orange)
![FlatLaf](https://img.shields.io/badge/Theme-FlatLaf-yellowgreen)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1)

A modern Java Swing application for managing student records with database integration and theme support.

![Screenshot](screenshot.png) *(Replace with your actual screenshot)*

## Features ✨

- **CRUD Operations**  
  Create, Read, Update, and Delete student records
- **Bulk Actions**  
  Select multiple students for batch operations
- **Advanced Search**  
  Find students by name, course, or ID
- **Data Import/Export**  
  CSV support for easy data transfer
- **Theme Support**  
  Built-in light/dark mode with FlatLaf
- **Responsive Design**  
  Works on multiple screen sizes

## Prerequisites 📋

- Java 17 or later
- MySQL 8.0+
- Maven (for building)

## Installation 🛠️

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/student-management-system.git
   cd student-management-system
   ```

2. **Database Setup**:
   - Create MySQL database:
     ```sql
     CREATE DATABASE student_db;
     ```
   - Import schema from `database/schema.sql`

3. **Configure Database**:
   Edit `src/main/resources/db.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/student_db
   db.username=your_username
   db.password=your_password
   ```

4. **Build & Run**:
   ```bash
   mvn clean package
   java -jar target/student-management-system.jar
   ```

## Usage Guide 📖

### Main Interface
- **Add Student**: Fill form and click "Add"
- **Edit Student**: Select from table, modify fields, click "Update"
- **Delete Student**: Select and click "Delete"
- **Bulk Actions**: Check boxes and use toolbar buttons

### Search Features
- Type in search box and press Enter
- Supports partial matches (e.g., "comp" finds "Computer Science")

### Data Management
- **Export**: File → Export to CSV
- **Import**: File → Import from CSV

### Themes
Change via View → Theme menu:
- Light (Default)
- Dark
- System (matches OS theme)

## Keyboard Shortcuts ⌨️

| Shortcut       | Action               |
|----------------|----------------------|
| Ctrl+N         | Add new student      |
| Delete         | Delete selected      |
| Ctrl+F         | Focus search field   |
| Ctrl+S         | Save/Export          |
| Ctrl+O         | Import               |

## Project Structure 🗂️

```
src/
├── main/
│   ├── java/
│   │   ├── gui/            # Swing UI classes
│   │   ├── dao/            # Database operations
│   │   ├── model/          # Data models
│   │   └── Main.java       # Entry point
│   └── resources/          # Configuration files
database/
├── schema.sql              # Database schema
└── sample_data.sql         # Sample records
```

## Contributing 🤝

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License 📄

Distributed under the MIT License. See `LICENSE` for more information.

