package gestion.personnel.Utils;

import java.sql.SQLException;
import java.util.HashMap;
import javafx.stage.Window;

import gestion.personnel.models.Main.Employee;
import net.sf.jasperreports.engine.JREmptyDataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import win.zqxu.jrviewer.JRViewerFX;

public class PrintReport {
    
    private Employee employee;
    
    public PrintReport(Employee emp) {
        this.employee = emp;
    }
    
    public void showReport(Window window) throws JRException, ClassNotFoundException, SQLException {
        String reportSrcFile = "./src/reports/Detail.jrxml";
 
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        HashMap<String, Object> params = new HashMap<>();
 
        params.put("numero", String.valueOf(this.employee.getId()));
        params.put("nom", this.employee.getNom());
        params.put("prenom", this.employee.getPrenom());
        params.put("dateNaissance", "27/07/1994");
        params.put("lieuNaissance", this.employee.getLieuNaissance());
        params.put("adresse", this.employee.getAdresse());
        params.put("wilaya", this.employee.getWilaya());
        params.put("genre", this.employee.getGenre().name());
        
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        
        JRViewerFX.preview(window, print);
    }
    
}
