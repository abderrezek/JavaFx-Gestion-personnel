package gestion.personnel.controllers;

import gestion.personnel.Utils.*;
import gestion.personnel.models.Main.*;
import gestion.personnel.reports.EmployeeItem;
import java.io.BufferedReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import win.zqxu.jrviewer.JRViewerFX;

public class MainController implements Initializable {
    
    @FXML
    private Button btnAdd, btnDeleteMultiple, btnDeleteAll, btnSelect, btnImport, btnExport,
            btnFirst, btnPrevious, btnNext, btnLast, btnPrintAll;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox pgSize;
    @FXML
    private Spinner<Integer> numeroPage; 
    @FXML
    private TableView table;
    @FXML
    private TableColumn tcId, tcSelect, tcNom, tcPrenom, tcDateNaissance, tcAge, tcLieuNaissance, tcAdresse, tcWilaya, tcGenre, tcActions;
    private ObservableList<Employee> data;
    
    private EmployeeDAO employeeDAO;
    public int ItemsPerPage, NumeroPage = 1, TotalRows, MaxNumeroPage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("gestion.personnel.controllers.MainController.initialize()");
        this.Tooltip();
        this.employeeDAO = new EmployeeDAO();
        this.ComboBoxItems();
        this.TableColumns();
        this.LoadData(this.ItemsPerPage, -1);
        this.SpinnerDefaultValue(this.MaxNumeroPage);
        
        System.out.println("aze");
        txtSearch.textProperty().addListener((Observable, oldValue, newValue) -> {
            List<Employee> empsList = this.employeeDAO.Get(newValue);
            table.getItems().clear();
            if (empsList.size() > 0) {
                data = FXCollections.observableArrayList(empsList);
                table.setItems(data);
            } else {
                table.setPlaceholder(new Label("Aucune ligne à afficher"));
            }
        });
    }
    
    private void Tooltip() {
        btnAdd.setTooltip(new Tooltip("Ajouter un nouveau employé"));
        btnDeleteAll.setTooltip(new Tooltip("Supprimer tous les employées"));
        btnExport.setTooltip(new Tooltip("exporter tous les employées"));
        btnImport.setTooltip(new Tooltip("emporter tous les employées"));
        btnSelect.setTooltip(new Tooltip("Selectionner multi employés"));
    }
    
    private void SpinnerDefaultValue(int max) {
        if (this.data != null) {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max, 1);
            valueFactory.valueProperty().addListener((obs, oldValue, newValue) -> {
                this.NumeroPage = newValue;

                this.LoadData(this.ItemsPerPage, ((newValue - 1) * this.ItemsPerPage));
            });
    //        numeroPage.setValueFactory(valueFactory);            
        }
    }
    
    private void ComboBoxItems() {
        pgSize.getItems().removeAll(pgSize.getItems());
        pgSize.getItems().addAll(10, 20, 30, 40, 50, 100);
        pgSize.getSelectionModel().select(2);
        this.ItemsPerPage = (int) pgSize.getSelectionModel().getSelectedItem();
    }
    
    private void TableColumns() {
        tcId.setCellValueFactory(new PropertyValueFactory<Employee, String>("Id"));
        tcSelect.setCellValueFactory(new PropertyValueFactory<Employee, String>("Select"));
        tcNom.setCellValueFactory(new PropertyValueFactory<Employee, String>("Nom"));
        tcPrenom.setCellValueFactory(new PropertyValueFactory<Employee, String>("Prenom"));
        tcDateNaissance.setCellValueFactory(new PropertyValueFactory<Employee, String>("DateNaissance"));
        tcAge.setCellValueFactory(new PropertyValueFactory<Employee, String>("Age"));
        tcLieuNaissance.setCellValueFactory(new PropertyValueFactory<Employee, String>("LieuNaissance"));
        tcAdresse.setCellValueFactory(new PropertyValueFactory<Employee, String>("Adresse"));
        tcWilaya.setCellValueFactory(new PropertyValueFactory<Employee, String>("Wilaya"));
        tcGenre.setCellValueFactory(new PropertyValueFactory<Employee, String>("Genre")); 
        this.addButtonsToTable();
    }
    
    private void addButtonsToTable() {
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = new Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                final TableCell<Employee, Void> cell = new TableCell<Employee, Void>() {
                    private final Button btnEdit = new Button("Modifier");
                    {
                        btnEdit.setOnAction((ActionEvent e) -> {
                            Employee data = getTableView().getItems().get(getIndex());
                            Utils.ShowNewWindow("/views/Add.fxml", MainController.this, "Modifier un employé", data);
                        });
                    }
                    private final Button btnDelete = new Button("Supprimer");
                    {
                        btnDelete.setOnAction((ActionEvent e) -> {
                            Utils.ShowAlertYesNo("Supprimer le employée", "voulez-vous vraiment supprimer l'employée ?", () ->  {
                                Employee data = getTableView().getItems().get(getIndex());
                                EmployeeDAO employeeDAO = new EmployeeDAO();
                                if (employeeDAO.Delete(data.getId())) {
                                    MainController.this.LoadData(MainController.this.ItemsPerPage, MainController.this.NumeroPage);
                                    System.out.println("yes");
                                } else {
                                    System.out.println("no");
                                }
                            });
                        });
                    }
                    private final Button btnPrint = new Button("Imprimer");
                    {
                        btnPrint.setOnAction((ActionEvent e) -> {
                            try {
                                Employee data = getTableView().getItems().get(getIndex());
                                
                                Node node = (Node) e.getSource();
                                Window window = (Stage) node.getScene().getWindow();
                                
                                PrintReport printDocument = new PrintReport(data);
                                printDocument.showReport(window);
                            } catch (Exception ex) {
                                System.out.println(".call()");
                                ex.printStackTrace();
                            }
                        });
                    }
                    HBox hbox = new HBox(btnEdit, btnDelete, btnPrint);
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            hbox.setSpacing(5);
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        };

        tcActions.setCellFactory(cellFactory);
    }
    
    public void LoadData(int psize, int offset) {
        if (offset == -1) {
            offset = (this.NumeroPage - 1) * psize;
        }
        this.TotalRows = this.employeeDAO.GetTotalItems();
        this.MaxNumeroPage = (this.TotalRows + psize - 1) / psize;
        List<Employee> empData = this.employeeDAO.GetAllPaginated(psize, offset);
        //table.getItems().clear();
        if (!empData.isEmpty()) {
            System.out.println("not empty");
            data = FXCollections.observableArrayList(empData);
            
            table.setItems(data);
            table.setDisable(false);
            btnSelect.setDisable(false);
            btnDeleteAll.setDisable(false);
            btnDeleteMultiple.setDisable(false);
            txtSearch.setDisable(false);
            btnImport.setDisable(false);
            btnExport.setDisable(false);
            btnFirst.setDisable(false);
            btnPrevious.setDisable(false);
            btnNext.setDisable(false);
            btnLast.setDisable(false);
            numeroPage.setDisable(false);
            pgSize.setDisable(false);
            btnPrintAll.setDisable(false);
        } else {
            table.setPlaceholder(new Label("Aucune ligne à afficher"));
            table.setDisable(true);
            btnSelect.setDisable(true);
            btnDeleteAll.setDisable(true);
            btnDeleteMultiple.setDisable(true);
            txtSearch.setDisable(true);
            btnImport.setDisable(true);
            btnExport.setDisable(true);
            btnFirst.setDisable(true);
            btnPrevious.setDisable(true);
            btnNext.setDisable(true);
            btnLast.setDisable(true);
            numeroPage.setDisable(true);
            pgSize.setDisable(true);
            btnPrintAll.setDisable(true);
        }
    }
    
    @FXML
    private void btnAddClick(ActionEvent e) {
        Utils.ShowNewWindow("gestion/personnel/views/Add.fxml", MainController.this, "Ajouter un nouveau employe", null);
    }
    
    @FXML
    private void btnSelectClick(ActionEvent e) {
        if (!tcSelect.visibleProperty().get()) {
            tcSelect.setVisible(true);
            btnSelect.setText("Désélectionner");
            btnDeleteMultiple.setVisible(true);
        } else {
            tcSelect.setVisible(false);
            btnSelect.setText("Sélectionner");
            btnDeleteMultiple.setVisible(false);
        }
    }
    
    @FXML
    private void btnDeleteMultipleClick(ActionEvent e) {
        Utils.ShowAlertYesNo("Supprimer les employés", "voulez-vous vraiment supprimer les employé?", () ->  {
            ObservableList<Employee> dataRemove = FXCollections.observableArrayList();
            ArrayList<Integer> listRemove = new ArrayList<Integer>();

            for (Employee emp : data) {
                if (emp.getSelect().isSelected()) {
                    dataRemove.add(emp);
                    listRemove.add(emp.getId());
                }
            }
            EmployeeDAO employeeDAO = new EmployeeDAO();
            if (employeeDAO.DeleteMany(listRemove)) {
                System.out.println("yes");
                data.removeAll(dataRemove);
            } else {
                System.out.println("nooo");
            }
        });
    }

    @FXML
    private void btnDeleteAllClick(ActionEvent e) {
        Utils.ShowAlertYesNo("Supprimer les employés", "voulez-vous vraiment supprimer tous les employé?", () ->  {
            EmployeeDAO employeDAO = new EmployeeDAO();
            if (employeDAO.DeleteAll()) {
                this.LoadData(this.ItemsPerPage, -1);
                System.out.println("yes");
            } else {
                System.out.println("no");
            }
        });
    }
    
    @FXML
    private void ComboBoxAction(ActionEvent e) {
        this.ItemsPerPage = (int) pgSize.getSelectionModel().getSelectedItem();
        
        this.LoadData(this.ItemsPerPage, ((this.NumeroPage - 1) * this.ItemsPerPage));
        this.SpinnerDefaultValue(this.MaxNumeroPage);
    }
    
    @FXML
    private void btnFirstClick(ActionEvent e) {
        this.LoadData(this.ItemsPerPage, 0);
        numeroPage.getValueFactory().setValue(1);
        this.NumeroPage = 1;
    }
    
    @FXML
    private void btnPreviousClick(ActionEvent e) {
        int prev = this.NumeroPage - 1;
        int prevOffset = (prev - 1) * this.ItemsPerPage;
        if (prevOffset < 0) {
            prev = 1;
            prevOffset = -1;
        }        
        this.LoadData(this.ItemsPerPage, prevOffset);
        numeroPage.getValueFactory().setValue(prev);
        this.NumeroPage = prev;
    }
    
    @FXML
    private void btnNextClick(ActionEvent e) {
        int next = this.NumeroPage + 1;
        int nextOffset = (next - 1) * this.ItemsPerPage;
        if (next > this.MaxNumeroPage) {
            next = this.MaxNumeroPage;
            nextOffset = (next - 1) * this.ItemsPerPage;
        }
        this.LoadData(this.ItemsPerPage, nextOffset);
        numeroPage.getValueFactory().setValue(next);
        this.NumeroPage = next;
    }
    
    @FXML
    private void btnLastClick(ActionEvent e) {
        int lastOffset = (this.MaxNumeroPage - 1) * this.ItemsPerPage;
        this.LoadData(this.ItemsPerPage, lastOffset);
        numeroPage.getValueFactory().setValue(this.MaxNumeroPage);
        this.NumeroPage = this.MaxNumeroPage;
    }
    
    @FXML
    private void btnImportClick(ActionEvent e) {
        // get stage
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        String userDir = System.getProperty("user.home");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import file");
        fileChooser.setInitialDirectory(new File(userDir + "/Desktop"));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV File", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Utils.ShowAlertYesNo("supprimer des données", "supprimer toutes les données", () -> {
                this.employeeDAO.DeleteAll();
            });
            try {
                BufferedReader csvReader = new BufferedReader(new FileReader(selectedFile)); 
                List<Employee> listEmps = new ArrayList<>();
                String row;
                Employee emp;
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                csvReader.readLine();
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");
                    emp = new Employee();
                    emp.setNom(data[0]);
                    emp.setPrenom(data[1]);
                    emp.setDateNaissance(new Date(format.parse(data[2]).getTime()));
                    emp.setAge(Integer.valueOf(data[3]));
                    emp.setLieuNaissance(data[4]);
                    emp.setAdresse(data[5]);
                    emp.setWilaya(data[6]);
                    emp.setGenre("masculin".equals(data[7]) ? Genre.Femelle.Masculin : Genre.Femelle);
                    
                    listEmps.add(emp);
                }
                if (this.employeeDAO.InsertPlusier(listEmps)) {
                    Utils.ShowAlertMsg("importer des données", "les importer terminer", Alert.AlertType.INFORMATION, () -> {});
                } else {
                    Utils.ShowAlertMsg("importer des données", "non les importer terminer", Alert.AlertType.INFORMATION, () -> {});
                }
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    private void btnExportClick(ActionEvent e) {
        // get stage
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter file");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV File", "*.csv")
        );
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            // XLSX
            // folder poi lib for xlsx
//            XSSFWorkbook workbook = new XSSFWorkbook();
//            XSSFSheet sheet = workbook.createSheet("Data Employés");
//            Object[][] datatypes = new Object[data.size() + 1][5];
//            datatypes[0][0] = "Nom & Prenom";
//            datatypes[0][1] = "Date Naissance";
//            datatypes[0][2] = "Lieu Naissance";
//            datatypes[0][3] = "Wilaya";
//            datatypes[0][4] = "Genre";
//            Employee emp;
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            int j;
//            for(int i = 0; i < data.size(); i++) {
//                emp = data.get(i);
//                j = i + 1;
//                datatypes[j][0] = emp.getNom() + " " + emp.getPrenom();
//                datatypes[j][1] = dateFormat.format(emp.getDateNaissance());
//                datatypes[j][2] = emp.getLieuNaissance();
//                datatypes[j][3] = emp.getWilaya();
//                datatypes[j][4] = emp.getGenre().name().toLowerCase();
//            }
//
//            int rowNum = 0;
//
//            for (Object[] datatype : datatypes) {
//                Row row = sheet.createRow(rowNum++);
//                int colNum = 0;
//                for (Object field : datatype) {
//                    Cell cell = row.createCell(colNum++);
//                    if (field instanceof String) {
//                        cell.setCellValue((String) field);
//                    } else if (field instanceof Integer) {
//                        cell.setCellValue((Integer) field);
//                    }
//                }
//            }
//
//            try {
//                FileOutputStream outputStream = new FileOutputStream(selectedFile);
//                workbook.write(outputStream);
//                workbook.close();
//            } catch (FileNotFoundException ex) {
//                ex.printStackTrace();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }

            // CSV
            try {
                List<Employee> listEmps = this.employeeDAO.GetAll();
                ArrayList<ArrayList<String>> rows = new ArrayList<>();
                ArrayList<String> list;
                Employee emp;
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                for(int i = 0; i < listEmps.size(); i++) {
                    list = new ArrayList<>();
                    emp = listEmps.get(i);
                    list.add(emp.getNom());
                    list.add(emp.getPrenom());
                    list.add(dateFormat.format(emp.getDateNaissance()));
                    list.add(String.valueOf(emp.getAge()));
                    list.add(emp.getLieuNaissance());
                    list.add(emp.getAdresse());
                    list.add(emp.getWilaya());
                    list.add(emp.getGenre().name().toLowerCase());
                    
                    rows.add(list);
                }
                FileWriter csvWriter = new FileWriter(selectedFile);
                csvWriter.append("nom");
                csvWriter.append(",");
                csvWriter.append("prenom");
                csvWriter.append(",");
                csvWriter.append("date naissance");
                csvWriter.append(",");
                csvWriter.append("age");
                csvWriter.append(",");
                csvWriter.append("lieu naissance");
                csvWriter.append(",");
                csvWriter.append("adresse");
                csvWriter.append(",");
                csvWriter.append("wilaya");
                csvWriter.append(",");
                csvWriter.append("genre");
                csvWriter.append("\n");
                
                for (List<String> rowData : rows) {
                    csvWriter.append(String.join(",", rowData));
                    csvWriter.append("\n");
                }

                csvWriter.flush();
                csvWriter.close();
                Utils.ShowAlertMsg("Export data", "terminer", Alert.AlertType.INFORMATION, () -> {});
            } catch (Exception ex) {
                System.out.println("controllers.MainController.btnExportClick()");
                ex.printStackTrace();
            }
        }        
    }

    @FXML
    private void btnPrintAllClick(ActionEvent e) {
        List<EmployeeItem> listItems = new ArrayList<>();
        EmployeeItem employeeItem;
        for(Employee emp : data) {
            employeeItem = new EmployeeItem();
            employeeItem.setNomPrenom(emp.getNom() + " " + emp.getPrenom());
            employeeItem.setGenre(emp.getGenre().name());
            employeeItem.setWilaya(emp.getWilaya());
            employeeItem.setLieuNaissance(emp.getLieuNaissance());
            employeeItem.setDateNaissance(emp.getDateNaissance().toString());
            
            listItems.add(employeeItem);
        }
        
        /* Convert List to JRBeanCollectionDataSource */
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listItems);
        
        /* Map to hold Jasper report Parameters */
        Map<String, Object> params = new HashMap<>();
        params.put("Items", ds);
        
        try {
            String reportSrcFile = "./src/reports/list.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
            
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        
            Node node = (Node) e.getSource();
            Window window = (Stage) node.getScene().getWindow();
            
            JRViewerFX.preview(window, print);
        } catch (Exception ex) {
            System.out.println("controllers.MainController.btnPrintAllClick()");
            ex.printStackTrace();
        }
        
    }
}