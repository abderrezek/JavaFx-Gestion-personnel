package gestion.personnel.models.Main;

import gestion.personnel.Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import gestion.personnel.Utils.Utils;

public class EmployeeDAO {
    
    private Connection cnn;
    private String TableName = "employee";
    
    public EmployeeDAO() {
        try {
            this.cnn = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {
            System.out.println("models.Main.EmployeeDAO.<init>()");
        }
    }
    
    public List<Employee> GetAllPaginated(int itemsPerPage, int offset) {
        String sql = "SELECT * FROM " + TableName + " LIMIT " + offset + ", " + itemsPerPage;
        List<Employee> empsList = new ArrayList<Employee>();
        try {
            Statement stmt = cnn.createStatement();
            ResultSet rslt = stmt.executeQuery(sql);
            Employee emp;
            while (rslt.next()) {
                emp = new Employee();
                emp.setId(rslt.getInt("id"));
                emp.setNom(rslt.getString("nom"));
                emp.setPrenom(rslt.getString("prenom"));
                emp.setDateNaissance(rslt.getDate("date_naissance"));
                emp.setAge(rslt.getInt("age"));
                emp.setLieuNaissance(rslt.getString("lieu_naissance"));
                emp.setAdresse(rslt.getString("adresse"));
                emp.setWilaya(rslt.getString("wilaya"));
                emp.setGenre(rslt.getString("genre").equals("Masculin") ? Genre.Masculin : Genre.Femelle);
                
                empsList.add(emp);
            }
            
            return empsList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.GetAllPaginated()");
            return null;
        }
    }
    
    public List<Employee> GetAll() {
        String sql = "SELECT * FROM " + TableName;
        List<Employee> empsList = new ArrayList<Employee>();
        try {
            Statement stmt = cnn.createStatement();
            ResultSet rslt = stmt.executeQuery(sql);
            Employee emp;
            while (rslt.next()) {
                emp = new Employee();
                emp.setId(rslt.getInt("id"));
                emp.setNom(rslt.getString("nom"));
                emp.setPrenom(rslt.getString("prenom"));
                emp.setDateNaissance(rslt.getDate("date_naissance"));
                emp.setAge(rslt.getInt("age"));
                emp.setLieuNaissance(rslt.getString("lieu_naissance"));
                emp.setAdresse(rslt.getString("adresse"));
                emp.setWilaya(rslt.getString("wilaya"));
                emp.setGenre(rslt.getString("genre").equals("Masculin") ? Genre.Masculin : Genre.Femelle);
                
                empsList.add(emp);
            }
            
            return empsList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.GetAll()");
            return null;
        }
    }
    
    public int GetTotalItems() {
        String sql = "SELECT COUNT(*) AS count_rows FROM " + TableName;
        try {
            Statement stmt = cnn.createStatement();
            ResultSet rslt = stmt.executeQuery(sql);
            rslt.next();
            
            return rslt.getInt("count_rows");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.GetTotalItems()");
            return -1;
        }
    }
    
    public boolean Insert(Employee emp) {
        String sql = "INSERT INTO " + TableName + " (nom, prenom, date_naissance, age, lieu_naissance, adresse, wilaya, genre) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = cnn.prepareStatement(sql);
            stmt.setString(1, emp.getNom());
            stmt.setString(2, emp.getPrenom());
            stmt.setDate(3, emp.getDateNaissance());
            stmt.setInt(4, emp.getAge());
            stmt.setString(5, emp.getLieuNaissance());
            stmt.setString(6, emp.getAdresse());
            stmt.setString(7, emp.getWilaya());
            stmt.setString(8, emp.getGenre().name());
            
            return stmt.executeUpdate() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.Insert()");
            return false;
        }
    }
    
    public boolean InsertMultiple(List<String> sqlsList) {
        try {
            Statement stmt = cnn.createStatement();
            cnn.setAutoCommit(false);
            for(String sql : sqlsList) {
                stmt.addBatch(sql);
            }
            int[] count = stmt.executeBatch();
            cnn.commit();
            return count.length == sqlsList.size();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.InsertMultiple()");
            return false;
        }
    }
    
    public boolean InsertPlusier(List<Employee> listEmps) {
        String sql = "INSERT INTO " + TableName + " (nom, prenom, date_naissance, age, lieu_naissance, adresse, wilaya, genre) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = cnn.prepareStatement(sql);
            cnn.setAutoCommit(false);
            for (Employee emp : listEmps) {
                pstmt.setString(1, emp.getNom());
                pstmt.setString(2, emp.getPrenom());
                pstmt.setDate(3, emp.getDateNaissance());
                pstmt.setInt(4, emp.getAge());
                pstmt.setString(5, emp.getLieuNaissance());
                pstmt.setString(6, emp.getAdresse());
                pstmt.setString(7, emp.getWilaya());
                pstmt.setString(8, emp.getGenre().name());
                
                pstmt.addBatch();
            }
            int[] count = pstmt.executeBatch();
            cnn.commit();
            return count.length == listEmps.size();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.InsertPlusier()");
            return false;
        }
    }
    
    public boolean Update(Employee emp) {
        String sql = "UPDATE " + TableName + " "
                + "SET nom=?, prenom=?, date_naissance=?, age=?, lieu_naissance=?, adresse=?, wilaya=?, genre=? "
                + "WHERE id=?";
        try {
            PreparedStatement stmt = cnn.prepareStatement(sql);
            stmt.setString(1, emp.getNom());
            stmt.setString(2, emp.getPrenom());
            stmt.setDate(3, emp.getDateNaissance());
            stmt.setInt(4, emp.getAge());
            stmt.setString(5, emp.getLieuNaissance());
            stmt.setString(6, emp.getAdresse());
            stmt.setString(7, emp.getWilaya());
            stmt.setString(8, emp.getGenre().name());
            stmt.setInt(9, emp.getId());
            
            return stmt.executeUpdate() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.Insert()");
            return false;
        }
    }

    public boolean Delete(int id) {
        String sql = "DELETE FROM " + TableName + " WHERE id=?";
        try {
            PreparedStatement stmt = cnn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.Delete()");
            return false;
        }
    }

    public boolean DeleteAll() {
        String sql = "DELETE FROM " + TableName;
        try {
            Statement stmt = cnn.createStatement();
            return stmt.executeUpdate(sql) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("models.Main.EmployeeDAO.DeleteAll()");
            return false;
        }
    }

    public boolean DeleteMany(List<Integer> idsList) {
        String sql = "DELETE FROM " + TableName + " WHERE id=?";
        try {
            PreparedStatement pstmt = cnn.prepareStatement(sql);
            
            cnn.setAutoCommit(false);
            for (int id : idsList) {
                pstmt.setInt(1, id);
                pstmt.addBatch();
            }
            int[] count = pstmt.executeBatch();
            cnn.commit();
            return count.length == idsList.size();
        } catch (Exception e) {
            System.out.println("models.Main.EmployeeDAO.DeleteMany()");
            e.printStackTrace();
            return false;
        }
    }

    public List<Employee> Get(String search) {
        String sql = "SELECT * FROM " + TableName + " "
                + "WHERE nom like ? OR prenom like ? OR date_naissance like ? OR age=? OR lieu_naissance like ? OR adresse like ? OR wilaya like ?";
        try {
            PreparedStatement pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1, '%' + search + '%');
            pstmt.setString(2, '%' + search + '%');
            if (Utils.isDate(search)) {
                pstmt.setDate(3, Date.valueOf(search));
            } else {
                pstmt.setDate(3, Date.valueOf("2020-01-01"));
            }
            if (Utils.isInteger(search)) {
                pstmt.setInt(4, Integer.valueOf(search));
            } else {
                pstmt.setInt(4, 0);
            }
            pstmt.setString(5, '%' + search + '%');
            pstmt.setString(6, '%' + search + '%');
            pstmt.setString(7, '%' + search + '%');
            ResultSet rslt = pstmt.executeQuery();
            
            List<Employee> empsList = new ArrayList<Employee>();
            Employee emp;
            while (rslt.next()) {
                emp = new Employee();
                emp.setId(rslt.getInt("id"));
                emp.setNom(rslt.getString("nom"));
                emp.setPrenom(rslt.getString("prenom"));
                emp.setDateNaissance(rslt.getDate("date_naissance"));
                emp.setAge(rslt.getInt("age"));
                emp.setLieuNaissance(rslt.getString("lieu_naissance"));
                emp.setAdresse(rslt.getString("adresse"));
                emp.setWilaya(rslt.getString("wilaya"));
                emp.setGenre(rslt.getString("genre").equals("Masculin") ? Genre.Masculin : Genre.Femelle);
                
                empsList.add(emp);
            }
            
            return empsList;
        } catch (Exception e) {
            System.out.println("models.Main.EmployeeDAO.Get()");
            e.printStackTrace();
            return null;
        }
    }
}
