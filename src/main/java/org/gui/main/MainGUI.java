package org.gui.main;

import org.database.DatabaseConnection;
import org.gui.logs.LogGUI;
import org.gui.tables.*;
import org.gui.reports.*;
import org.logger.CsvLogger;
import org.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.swing.event.*;
import javax.swing.table.TableRowSorter;

public class MainGUI { // Singleton
    private static MainGUI instance = null;

    private JPanel panel1;
    private JScrollPane jscrPane;
    private JTable tblMain;
    private JButton btnRemove;
    private JButton btnAdd;

    public Component getBtnAdd() {
        return this.btnAdd;
    }

    private enum TableType {
        CAMERA,
        RENT,
        CAMERATYPE,
        FORMAT,
        EMPLOYEE,
        USER
    }

    private TableType currentTableType = TableType.RENT;

    private DatabaseConnection.DatabaseType databaseType = DatabaseConnection.DatabaseType.ORACLE;

    public DatabaseConnection.DatabaseType getDatabaseType() {
        return databaseType;
    }
    public DatabaseConnection.DatabaseType setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        return this.databaseType = databaseType;
    }

    private CsvLogger logger = CsvLogger.getInstance();

    public MainGUI() {
        // Singleton
        if (MainGUI.instance != null)
            throw new RuntimeException("MainGUI is a singleton class. Use getInstance() instead.");
        else
            instance = this;
    }

    public static MainGUI getInstance() {
        if (instance == null)
            return new MainGUI();

        return instance;
    }

    public void initUserTable() {
        UserModel userModel = UserModel.getInstance();
        userModel.setDatabaseType(this.databaseType);
        try {
            userModel.getData();
            DefaultTableModel rm = userModel.getTableModel();

            this.tblMain = new JTable();
            this.jscrPane.setViewportView(this.tblMain);
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();

            class TableModelEvents implements TableModelListener {
                public boolean isCellEditable(int row, int column) {
                    return column != 0;
                }

                public void setValueAt(String value, int row, int column) throws Exception {
                    UserModel rm = UserModel.getInstance();
                    ModelList<UserModel.InnerUserModel> modelList = new ModelList<>();
                    UserModel.InnerUserModel irm = new UserModel.InnerUserModel();
                    DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

                    irm.IDUtilizator = Integer.parseInt(dtm.getValueAt(row, 0).toString());
                    irm.NumeUtilizator = dtm.getValueAt(row, 1).toString();
                    irm.Parola = dtm.getValueAt(row, 2).toString();
                    irm.Nume = dtm.getValueAt(row, 3).toString();
                    irm.Prenume = dtm.getValueAt(row, 4).toString();
                    Object cnp = dtm.getValueAt(row, 5);
                    irm.CNP = cnp == null ? "" : cnp.toString();
                    irm.Email = dtm.getValueAt(row, 6).toString();


                    switch (column) {
                        case 1:
                            irm.NumeUtilizator = value;
                            break;
                        case 2:
                            irm.Parola = value;
                            break;
                        case 3:
                            irm.Nume = value;
                            break;
                        case 4:
                            irm.Prenume = value;
                            break;
                        case 5:
                            irm.CNP = value;
                            break;
                        case 6:
                            irm.Email = value;
                            break;
                        default:
                            throw new Exception("Invalid column index");
                    }

                    modelList.add(irm);
                    rm.updateData(modelList);
                }

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        try {
                            setValueAt((String) tblMain.getValueAt(row, column), row, column);
                        } catch (Exception ex) {
                            System.out.println("Error in trying to update user table: " + ex.getMessage());
                        }
                    }
                }
            }

            this.tblMain.getModel().addTableModelListener(new TableModelEvents());
            this.currentTableType = TableType.USER;
        } catch (Exception e) {
            System.out.println("Error in trying to initialize camera table: " + e.getMessage());
        }
    }

    public void initEmployeeTable() {
        EmployeeModel employeeModel = EmployeeModel.getInstance();
        employeeModel.setDatabaseType(this.databaseType);
        try {
            employeeModel.getData();
            DefaultTableModel rm = employeeModel.getTableModel();

            this.tblMain = new JTable();
            this.jscrPane.setViewportView(this.tblMain);
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();

            class TableModelEvents implements TableModelListener {
                public boolean isCellEditable(int row, int column) {
                    return column != 0;
                }

                public void setValueAt(String value, int row, int column) throws Exception {
                    EmployeeModel rm = EmployeeModel.getInstance();
                    ModelList<EmployeeModel.InnerEmployeeModel> modelList = new ModelList<>();
                    EmployeeModel.InnerEmployeeModel irm = new EmployeeModel.InnerEmployeeModel();
                    DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

                    irm.IDUtilizator = Integer.parseInt(dtm.getValueAt(row, 0).toString());
                    irm.DataNasterii = LocalDateTime.parse(dtm.getValueAt(row, 1).toString());
                    irm.DataAngajarii = LocalDateTime.parse(dtm.getValueAt(row, 2).toString());
                    irm.IDManager = Integer.parseInt(dtm.getValueAt(row, 3).toString());
                    irm.IDSalariu = Integer.parseInt(dtm.getValueAt(row, 4).toString());


                    switch (column) {
                        case 2:
                            irm.DataAngajarii = LocalDateTime.parse(value);
                            break;
                        case 1:
                            irm.DataNasterii = LocalDateTime.parse(value);
                            break;
                        case 3:
                            irm.IDManager = Integer.parseInt(value);
                            break;
                        case 4:
                            irm.IDSalariu = Integer.parseInt(value);
                            break;
                        default:
                            throw new Exception("Invalid column index");
                    }

                    modelList.add(irm);
                    rm.updateData(modelList);
                }

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        try {
                            setValueAt((String) tblMain.getValueAt(row, column), row, column);
                        } catch (Exception ex) {
                            System.out.println("Error in trying to update format table: " + ex.getMessage());
                        }
                    }
                }
            }

            this.tblMain.getModel().addTableModelListener(new TableModelEvents());
            this.currentTableType = TableType.EMPLOYEE;
        } catch (Exception e) {
            System.out.println("Error in trying to initialize camera table: " + e.getMessage());
        }
    }
    public void initFormatTable() {
        FormatModel formatModel = FormatModel.getInstance();
        formatModel.setDatabaseType(this.databaseType);
        try {
            formatModel.getData();
            DefaultTableModel rm = formatModel.getTableModel();

            this.tblMain = new JTable();
            this.jscrPane.setViewportView(this.tblMain);
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();

            class TableModelEvents implements TableModelListener {
                public boolean isCellEditable(int row, int column) {
                    return column != 0;
                }

                public void setValueAt(String value, int row, int column) throws Exception {
                    FormatModel rm = FormatModel.getInstance();
                    ModelList<FormatModel.InnerFormatModel> modelList = new ModelList<>();
                    FormatModel.InnerFormatModel irm = new FormatModel.InnerFormatModel();
                    DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

                    irm.IDFormat = Integer.parseInt(dtm.getValueAt(row, 0).toString());
                    irm.Denumire = dtm.getValueAt(row, 1).toString();
                    irm.LatimeFilm = dtm.getValueAt(row, 2).toString();

                    switch (column) {
                        case 1:
                            irm.Denumire = value;
                            break;
                        case 2:
                            irm.LatimeFilm = value;
                            break;
                        default:
                            throw new Exception("Invalid column index");
                    }

                    modelList.add(irm);
                    rm.updateData(modelList);
                }

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        try {
                            setValueAt((String) tblMain.getValueAt(row, column), row, column);
                        } catch (Exception ex) {
                            System.out.println("Error in trying to update format table: " + ex.getMessage());
                        }
                    }
                }
            }

            this.tblMain.getModel().addTableModelListener(new TableModelEvents());
            this.currentTableType = TableType.FORMAT;
        } catch (Exception e) {
            System.out.println("Error in trying to initialize camera table: " + e.getMessage());
        }
    }

    public void initCameraTable() {
        CameraModel cameraModel = CameraModel.getInstance();
        cameraModel.setDatabaseType(this.databaseType);
        try {
            cameraModel.getData();
            DefaultTableModel rm = cameraModel.getTableModel();

            this.tblMain = new JTable();
            this.jscrPane.setViewportView(this.tblMain);
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();

            class TableModelEvents implements TableModelListener {
                public boolean isCellEditable(int row, int column) {
                    return column != 0;
                }

                public void setValueAt(String value, int row, int column) throws Exception {
                    CameraModel rm = CameraModel.getInstance();
                    ModelList<CameraModel.InnerCameraModel> modelList = new ModelList<CameraModel.InnerCameraModel>();
                    CameraModel.InnerCameraModel irm = new CameraModel.InnerCameraModel();
                    DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

                    irm.IDCamera = Integer.parseInt(dtm.getValueAt(row, 0).toString());
                    irm.Marca = dtm.getValueAt(row, 1).toString();
                    irm.ModelCamera = dtm.getValueAt(row, 2).toString();
                    irm.AnFabricatie = Integer.parseInt(dtm.getValueAt(row, 3).toString());
                    irm.Pret = Double.parseDouble(dtm.getValueAt(row, 4).toString());
                    irm.PretInchiriere = Double.parseDouble(dtm.getValueAt(row, 5).toString());
                    irm.DenumireFormat = dtm.getValueAt(row, 6).toString();
                    irm.LatimeFilm = dtm.getValueAt(row, 7).toString();
                    irm.DenumireTip = dtm.getValueAt(row, 8).toString();
                    irm.DenumireMontura = dtm.getValueAt(row, 9).toString();

                    switch (column) {
                        case 0:
                            irm.IDCamera = Integer.parseInt(value);
                            break;
                        case 1:
                            irm.Marca = String.valueOf(value);
                            break;
                        case 2:
                            irm.ModelCamera = String.valueOf(value);
                            break;
                        case 3:
                            irm.AnFabricatie = Integer.parseInt(value);
                            break;
                        case 4:
                            irm.Pret = Double.parseDouble(value);
                            break;
                        case 5:
                            irm.PretInchiriere = Double.parseDouble(value);
                            break;
                        case 6:
                            irm.DenumireFormat = String.valueOf(value);
                            break;
                        case 7:
                            irm.LatimeFilm = String.valueOf(value);
                            break;
                        case 8:
                            irm.DenumireFormat = String.valueOf(value);
                            break;
                        case 9:
                            irm.DenumireMontura = String.valueOf(value);
                            break;
                        default:
                            throw new Exception("Invalid column index");
                    }

                    modelList.add(irm);
                    rm.updateData(modelList);
                }

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        try {
                            setValueAt((String) tblMain.getValueAt(row, column), row, column);
                        } catch (Exception ex) {
                            System.out.println("Error in trying to update camera table: " + ex.getMessage());
                        }
                    }
                }
            }

            this.tblMain.getModel().addTableModelListener(new TableModelEvents());
            this.currentTableType = TableType.CAMERA;
        } catch (Exception e) {
            System.out.println("Error in trying to initialize camera table: " + e.getMessage());
        }
    }

    public void initRentTable() {
        RentModel rentModel = RentModel.getInstance();
        rentModel.setDatabaseType(this.databaseType);
        try {
            rentModel.getData();
            DefaultTableModel rm = rentModel.getTableModel();
            this.tblMain = new JTable();
            this.jscrPane.setViewportView(this.tblMain);
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();

            // Make table sortable
            RowSorter<DefaultTableModel> sorter = new TableRowSorter<>(rm);
            tblMain.setRowSorter(sorter);

            class TableModelEvents implements TableModelListener {
                public boolean isCellEditable(int row, int column) {
                    return column <= 5;
                }

               public void setValueAt(String value, int row, int column) throws Exception {
                    RentModel rm = RentModel.getInstance();
                    ModelList<RentModel.InnerRentModel> modelList = new ModelList<>();
                    RentModel.InnerRentModel irm = new RentModel.InnerRentModel();
                    DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

                    irm.DURATA_IN_ZILE = Integer.parseInt(dtm.getValueAt(row, 0).toString());
                    irm.ESTE_RETURNAT = Boolean.parseBoolean(dtm.getValueAt(row, 1).toString());
                    irm.PENALIZARE = Double.parseDouble(dtm.getValueAt(row, 2).toString());
                    irm.DATA_INCHIRIERE = Date.valueOf(dtm.getValueAt(row, 3).toString());
                    irm.IDCAMERA = Integer.parseInt(dtm.getValueAt(row, 4).toString());
                    irm.IDCLIENT = Integer.parseInt(dtm.getValueAt(row, 5).toString());
                    irm.IDOBIECTIV = Integer.parseInt(dtm.getValueAt(row, 6).toString());
                    irm.IDANGAJAT = Integer.parseInt(dtm.getValueAt(row, 7).toString());

                    switch (column) {
                        case 0:
                            irm.DURATA_IN_ZILE = Integer.parseInt(value);
                            break;
                        case 1:
                            irm.ESTE_RETURNAT = Boolean.parseBoolean(value);
                            break;
                        case 2:
                            irm.PENALIZARE = Double.parseDouble(value);
                            break;
                        case 3:
                            irm.DATA_INCHIRIERE = Date.valueOf(value);
                            break;
                        case 4:
                            irm.IDCAMERA = Integer.parseInt(value);
                            break;
                        case 5:
                            irm.IDCLIENT = Integer.parseInt(value);
                            break;
                        case 6:
                            irm.IDOBIECTIV = Integer.parseInt(value);
                            break;
                        case 7:
                            irm.IDANGAJAT = Integer.parseInt(value);
                            break;
                        default:
                            throw new Exception("Invalid column index");
                    }

                    modelList.add(irm);
                    rm.updateData(modelList);
                }

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        try {
                            setValueAt((String) tblMain.getValueAt(row, column), row, column);
                        } catch (Exception ex) {
                            System.out.println("Error in trying to update rent table: " + ex.getMessage());
                        }
                    }
                }
            }

            this.tblMain.getModel().addTableModelListener(new TableModelEvents());
            this.currentTableType = TableType.RENT;
        } catch (Exception e) {
            System.out.println("Error in trying to initialize rent table: " + e.getMessage());
        }
    }

    public void initCameraTypeTable() {
        CameraTypeModel cameraType = CameraTypeModel.getInstance();
        cameraType.setDatabaseType(this.databaseType);
        try {
            cameraType.getData();
            DefaultTableModel rm = cameraType.getTableModel();
            this.tblMain = new JTable();
            this.jscrPane.setViewportView(this.tblMain);
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();

            // Make table sortable
            RowSorter<DefaultTableModel> sorter = new TableRowSorter<>(rm);
            tblMain.setRowSorter(sorter);

            class TableModelEvents implements TableModelListener {
                public boolean isCellEditable(int row, int column) {
                    return column >= 1;
                }

                public void setValueAt(String value, int row, int column) throws Exception {
                    CameraTypeModel cm = CameraTypeModel.getInstance();
                    ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = new ModelList<>();
                    CameraTypeModel.InnerCameraTypeModel irm = new CameraTypeModel.InnerCameraTypeModel();
                    DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

                    irm.IDTip = Integer.parseInt(dtm.getValueAt(row, 0).toString());
                    irm.Denumire = dtm.getValueAt(row, 1).toString();


                    switch (column) {
                        case 1:
                            irm.Denumire = String.valueOf(value);
                            break;
                        default:
                            throw new Exception("Invalid column index");
                    }

                    modelList.add(irm);
                    cm.updateData(modelList);
                }

                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        try {
                            setValueAt((String) tblMain.getValueAt(row, column), row, column);
                        } catch (Exception ex) {
                            System.out.println("Error in trying to update camera type table: " + ex.getMessage());
                        }
                    }
                }
            }

            this.tblMain.getModel().addTableModelListener(new TableModelEvents());
            this.currentTableType = TableType.CAMERATYPE;
        } catch (Exception e) {
            System.out.println("Error in trying to initialize camera type table: " + e.getMessage());
        }
    }

    private void removeRowFromUserModel(int row) throws Exception{
        UserModel rm = UserModel.getInstance();
        ModelList<UserModel.InnerUserModel> modelList = new ModelList<>();
        UserModel.InnerUserModel irm = new UserModel.InnerUserModel();
        DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

        irm.IDUtilizator = Integer.parseInt(dtm.getValueAt(row, 0).toString());

        modelList.add(irm);
        rm.deleteRow(modelList);

        dtm.removeRow(row);
    }

    private void removeRowFromEmployeeModel(int row) throws Exception{
        EmployeeModel rm = EmployeeModel.getInstance();
        ModelList<EmployeeModel.InnerEmployeeModel> modelList = new ModelList<>();
        EmployeeModel.InnerEmployeeModel irm = new EmployeeModel.InnerEmployeeModel();
        DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

        irm.IDUtilizator = Integer.parseInt(dtm.getValueAt(row, 0).toString());

        modelList.add(irm);
        rm.deleteRow(modelList);

        dtm.removeRow(row);
    }
    private void removeRowFromFormatModel(int row) throws Exception{
        FormatModel rm = FormatModel.getInstance();
        ModelList<FormatModel.InnerFormatModel> modelList = new ModelList<>();
        FormatModel.InnerFormatModel irm = new FormatModel.InnerFormatModel();
        DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

        irm.IDFormat = Integer.parseInt(dtm.getValueAt(row, 0).toString());

        modelList.add(irm);
        rm.deleteRow(modelList);

        dtm.removeRow(row);
    }
    private void removeRowFromCameraTypeModel(int row) throws Exception{
        CameraTypeModel rm = CameraTypeModel.getInstance();
        ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = new ModelList<>();
        CameraTypeModel.InnerCameraTypeModel irm = new CameraTypeModel.InnerCameraTypeModel();
        DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

        irm.IDTip = Integer.parseInt(dtm.getValueAt(row, 0).toString());

        modelList.add(irm);
        rm.deleteRow(modelList);

        dtm.removeRow(row);
    }

    private void removeRowFromCameraModel(int row) throws Exception{
        CameraModel rm = CameraModel.getInstance();
        ModelList<CameraModel.InnerCameraModel> modelList = new ModelList<>();
        CameraModel.InnerCameraModel irm = new CameraModel.InnerCameraModel();
        DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

        irm.IDCamera = Integer.parseInt(dtm.getValueAt(row, 0).toString());

        modelList.add(irm);
        rm.deleteRow(modelList);

        dtm.removeRow(row);
    }

    private void removeRowFromRentModel(int row) throws Exception {
        RentModel rm = RentModel.getInstance();
        ModelList<RentModel.InnerRentModel> modelList = new ModelList<>();
        RentModel.InnerRentModel irm = new RentModel.InnerRentModel();
        DefaultTableModel dtm = ((DefaultTableModel) tblMain.getModel());

        irm.DURATA_IN_ZILE = Integer.parseInt(dtm.getValueAt(row, 0).toString());
        irm.ESTE_RETURNAT = Boolean.parseBoolean(dtm.getValueAt(row, 1).toString());
        irm.PENALIZARE = Double.parseDouble(dtm.getValueAt(row, 2).toString());
        irm.DATA_INCHIRIERE = Date.valueOf(dtm.getValueAt(row, 3).toString());
        irm.IDCAMERA = Integer.parseInt(dtm.getValueAt(row, 4).toString());
        irm.IDCLIENT = Integer.parseInt(dtm.getValueAt(row, 5).toString());
        irm.IDOBIECTIV = Integer.parseInt(dtm.getValueAt(row, 6).toString());
        irm.IDANGAJAT = Integer.parseInt(dtm.getValueAt(row, 7).toString());

        modelList.add(irm);
        rm.deleteRow(modelList);

        dtm.removeRow(row);
    }

    public void main(String[] args) {
        JFrame frame = new JFrame("Vintage Rent");
        Dimension minSize = new Dimension();
        minSize.setSize(800, 600);
        frame.setMinimumSize(minSize);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(e -> {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit the program?", "Exit Program Message Box",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                try{
                    logger.log("Exiting application by user request");
                } catch (Exception ex) {
                    System.out.println("Error logging to CSV: " + ex.getMessage());
                }
                System.exit(0);
            }
        });
        menu.add(menuItemExit);

        JMenu crud = new JMenu("CRUD");
        menuBar.add(crud);
        JMenuItem menuItemRent = new JMenuItem("Rent");
        menuItemRent.addActionListener(e -> {
            initRentTable();
            this.currentTableType = TableType.RENT;
        });
        crud.add(menuItemRent);
        JMenuItem menuItemCamera = new JMenuItem("Camera");
        menuItemCamera.addActionListener(e -> {
            initCameraTable();
            this.currentTableType = TableType.CAMERA;
        });
        crud.add(menuItemCamera);
        JMenuItem menuItemCameraType = new JMenuItem("Camera Type");
        menuItemCameraType.addActionListener(e -> {
            initCameraTypeTable();
            this.currentTableType = TableType.CAMERATYPE;
        });
        crud.add(menuItemCameraType);

        JMenuItem menuItemFormat = new JMenuItem("Camera Format");
        menuItemFormat.addActionListener(e -> {
            initFormatTable();
            this.currentTableType = TableType.FORMAT;
        });
        crud.add(menuItemFormat);

        JMenuItem menuItemEmployees = new JMenuItem("Employees");
        menuItemEmployees.addActionListener(e -> {
            initEmployeeTable();
            this.currentTableType = TableType.EMPLOYEE;
        });
        crud.add(menuItemEmployees);

        JMenuItem menuItemUsers = new JMenuItem("Users");
        menuItemUsers.addActionListener(e -> {
            initUserTable();
            this.currentTableType = TableType.USER;
        });
        crud.add(menuItemUsers);

        JMenu datasources = new JMenu("Datasources");
        menuBar.add(datasources);

        JMenuItem menuItemOracle = new JMenuItem("Oracle");
        menuItemOracle.addActionListener(e -> {
            try {
                this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
                if(this.currentTableType == TableType.CAMERA) {
                    initCameraTable();
                } else if(this.currentTableType == TableType.RENT) {
                    initRentTable();
                } else if(this.currentTableType == TableType.CAMERATYPE) {
                    initCameraTypeTable();
                } else if(this.currentTableType == TableType.FORMAT){
                    initFormatTable();
                } else if(this.currentTableType == TableType.EMPLOYEE){
                    initEmployeeTable();
                } else if(this.currentTableType == TableType.USER){
                    initUserTable();
                }

                try{
                    logger.log("Changed data source to Oracle");
                } catch (Exception ex) {
                    System.out.println("Error logging to CSV: " + ex.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        datasources.add(menuItemOracle);

        JMenuItem menuItemCsv = new JMenuItem("CSV");
        menuItemCsv.addActionListener(e -> {
            try {
                this.databaseType = DatabaseConnection.DatabaseType.CSV;
                if(this.currentTableType == TableType.CAMERA) {
                    initCameraTable();
                } else if(this.currentTableType == TableType.RENT) {
                    initRentTable();
                } else if(this.currentTableType == TableType.CAMERATYPE) {
                    initCameraTypeTable();
                } else if(this.currentTableType == TableType.FORMAT){
                    initFormatTable();
                } else if(this.currentTableType == TableType.EMPLOYEE){
                    initEmployeeTable();
                } else if(this.currentTableType == TableType.USER){
                    initUserTable();
                }

                try{
                    logger.log("Changed data source to CSV");
                } catch (Exception ex) {
                    System.out.println("Error logging to CSV: " + ex.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        datasources.add(menuItemCsv);

        JMenu reports = new JMenu("Reports");
        menuBar.add(reports);

        JMenuItem clientReport = new JMenuItem("Client report");
        clientReport.addActionListener(e -> {
            try {
                ClientReport cr = ClientReport.getInstance(frame, instance);
                cr.main();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        reports.add(clientReport);

        JMenuItem formatReport = new JMenuItem("Format report");
        formatReport.addActionListener(e -> {
            try {
                FormatSalesReport cr = FormatSalesReport.getInstance(frame, instance);
                cr.main();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        reports.add(formatReport);

        JMenu menuLog = new JMenu("Log");
        menuBar.add(menuLog);

        JMenuItem menuItemLog = new JMenuItem("Log");
        menuItemLog.addActionListener(e -> {
            try {
                LogGUI lr = LogGUI.getInstance(frame, instance);
                lr.main();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        menuLog.add(menuItemLog);

        JMenu menu2 = new JMenu("About");
        class AboutMenuListener implements MenuListener {
            @Override
            public void menuSelected(MenuEvent e) {
                JOptionPane.showMessageDialog(null, "Vintage Rent\nDeveloped by Dinu Florin-Silviu\nFor the PAO class of 2023");
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        }
        menu2.addMenuListener(new AboutMenuListener());
        menuBar.add(menu2);

        frame.setJMenuBar(menuBar);

//        this.jscrPane.setViewportView(this.tblMain);

        this.jscrPane = new JScrollPane();

        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.gridwidth = 3;
        c2.gridheight = 3;
        c2.weightx = 1;
        c2.weighty = 0.1;
        c2.anchor = GridBagConstraints.NORTH;
        c2.fill = GridBagConstraints.BOTH;
        this.panel1 = new JPanel(new GridBagLayout());
        this.panel1.add(this.jscrPane, c2);

        c2.gridx = 0;
        c2.gridy = 3;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        c2.weightx = 1;
        c2.weighty = 0.1;
        c2.anchor = GridBagConstraints.NORTH;
        c2.fill = GridBagConstraints.BOTH;
        this.btnRemove = new JButton("Remove");
        this.btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tblMain.convertRowIndexToModel(tblMain.getSelectedRow());
                if (row != -1) {
                    try {
                        if(currentTableType == TableType.RENT)
                            removeRowFromRentModel(row);
                        else if(currentTableType == TableType.CAMERA)
                            removeRowFromCameraModel(row);
                        else if(currentTableType == TableType.CAMERATYPE)
                            removeRowFromCameraTypeModel(row);
                        else if(currentTableType == TableType.FORMAT)
                            removeRowFromFormatModel(row);
                        else if(currentTableType == TableType.EMPLOYEE)
                            removeRowFromEmployeeModel(row);
                        else if(currentTableType == TableType.USER)
                            removeRowFromUserModel(row);
                    } catch (SQLException ex) {
                        JDialog dialog = new JDialog();
                        dialog.setAlwaysOnTop(true);
                        JOptionPane.showMessageDialog(dialog, "Cannot delete row because it is referenced by another table");

                        System.out.println("Error in trying to remove row from table model: " + ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error in trying to remove row from table model: " + ex.getMessage());
                    }
                }
            }
        });
        this.panel1.add(this.btnRemove, c2);

        c2.gridx = 1;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        c2.weightx = 1;
        c2.weighty = 0.1;
        c2.anchor = GridBagConstraints.NORTH;
        c2.fill = GridBagConstraints.BOTH;
        this.btnAdd = new JButton("Add");
        this.btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentTableType == TableType.RENT) {
                    RentAdd ra = RentAdd.getInstance(frame, instance);
                    ra.main();
                } else if(currentTableType == TableType.CAMERA){
                    CameraAdd ca = CameraAdd.getInstance(frame, instance);
                    ca.main();
                } else if(currentTableType == TableType.CAMERATYPE){
                    CameraTypeAdd cta = CameraTypeAdd.getInstance(frame, instance);
                    cta.main();
                } else if(currentTableType == TableType.FORMAT){
                    FormatAdd fa = FormatAdd.getInstance(frame, instance);
                    fa.main();
                } else if(currentTableType == TableType.EMPLOYEE){
                    EmployeeAdd ea = EmployeeAdd.getInstance(frame, instance);
                    ea.main();
                } else if(currentTableType == TableType.USER){
                    UserAdd ua = UserAdd.getInstance(frame, instance);
                    ua.main();
                }
            }
        });
        this.panel1.add(this.btnAdd, c2);

        frame.getContentPane().add(this.panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the program?", "Exit Program Message Box",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    try{
                        logger.log("Exiting application from main menu");
                    } catch (Exception ex) {
                        System.out.println("Error logging to CSV: " + ex.getMessage());
                    }
                    System.exit(0);
                }
            }
        });

        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if(this.currentTableType == TableType.CAMERA) {
            initCameraTable();
        } else if(this.currentTableType == TableType.RENT) {
            initRentTable();
        }

    }


}
