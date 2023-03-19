package org.database.memory;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.database.DatabaseConnection;
import org.models.*;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemory extends DatabaseConnection {
    private static InMemory instance = null;
    private ModelList<CameraTypeModel.InnerCameraTypeModel> cameraTypes = null;
    private ModelList<CameraModel.InnerCameraModel> cameras = null;
    private ModelList<FormatModel.InnerFormatModel> formats = null;
    private ModelList<MountModel.InnerMountModel> mounts = null;
    private ModelList<ClientModel.InnerClientModel> clients = null;
    private ModelList<ClientTypeModel.InnerClientTypeModel> clientTypes = null;
    private ModelList<UserModel.InnerUserModel> users = null;
    private ModelList<EmployeeModel.InnerEmployeeModel> employees = null;
    private ModelList<SalaryModel.InnerSalaryModel> salaries = null;
    private ModelList<ObjectiveModel.InnerObjectiveModel> objectives = null;

    public InMemory(String url, String username, String password, String driver, String database, String schema, String port) {
        if (instance != null)
            throw new RuntimeException("OracleConnection is a singleton class. Use getInstance() instead.");
        else {
            instance = this;
            instances.add(this);
        }

        cameraTypes = new ModelList<>();
        cameras = new ModelList<>();
        formats = new ModelList<>();
        mounts = new ModelList<>();
        clients = new ModelList<>();
        clientTypes = new ModelList<>();
        users = new ModelList<>();
        employees = new ModelList<>();
        salaries = new ModelList<>();
        objectives = new ModelList<>();
    }

    public static DatabaseConnection getInstance(DatabaseType t) throws RuntimeException {
        if (instance == null || t != DatabaseType.INMEMORY)
            throw new RuntimeException("No instance of DatabaseConnection has been created");

        for (DatabaseConnection db : instances) {
            if (db instanceof InMemory)
                return db;
        }

        throw new RuntimeException("No instance of DatabaseConnection of the specified DatabaseType has been created");
    }

    @Override
    public void makeConnectionString() {
        return;
    }

    @Override
    public void connect() throws Exception {
        return;
    }

    @Override
    public void disconnect() throws Exception {
        return;
    }

    @Override
    public ResultSet executeQuery(String query) throws Exception {
        return null;
    }

    @Override
    public boolean isInitialized() throws Exception {
        return true;
    }

    @Override
    public void init() throws Exception {
        return;
    }

    @Override
    public void update(String tableName, Map<String, String> set, Map<String, String> where) throws Exception {
        return;
    }

    public ResultSet getResultSet(List<String> headers, List<List<Object>> data) throws Exception {

        // validation
        if (headers == null || data == null) {
            throw new Exception("null parameters");
        }

//        if (headers.size() != data.get(0).size()) {
//            throw new Exception("parameters size are not equals");
//        }


        // create a mock result set
        MockResultSet mockResultSet = new MockResultSet("myResultSet");

        // add header
        for (String string : headers) {
            mockResultSet.addColumn(string.toUpperCase());
        }

        // add data
        for (List<Object> list : data) {
            mockResultSet.addRow(list);
        }

        return mockResultSet;
    }

    private List<String> getAttributes(Class<?> c) throws Exception {
        List<String> attributes = new ArrayList<>();
        for (Field f : c.getDeclaredFields()) {
            attributes.add(f.getName());
        }
        return attributes;
    }

    private List<List<Object>> getObjectsFromModelList(ModelList<?> list) throws Exception {
        List<List<Object>> data = new ArrayList<>();
        for (Object o : list.getList()) {
            List<Object> row = new ArrayList<>();
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                row.add(f.get(o));
            }
            data.add(row);
        }
        return data;
    }

    @Override
    public ResultSet getAllTableData(String tableName) throws Exception {
        List<String> headers = null;
        List<List<Object>> data = null;
        if (tableName == "camera_type") {
            headers = this.getAttributes(CameraTypeModel.InnerCameraTypeModel.class);
            data = this.getObjectsFromModelList(this.cameraTypes);
        } else if (tableName == "camera") {
            headers = this.getAttributes(CameraModel.InnerCameraModel.class);
            data = this.getObjectsFromModelList(this.cameras);
        } else if (tableName == "format") {
            headers = this.getAttributes(FormatModel.InnerFormatModel.class);
            data = this.getObjectsFromModelList(this.formats);
        } else if (tableName == "mount") {
            headers = this.getAttributes(MountModel.InnerMountModel.class);
            data = this.getObjectsFromModelList(this.mounts);
        } else if (tableName == "client") {
            headers = this.getAttributes(ClientModel.InnerClientModel.class);
            data = this.getObjectsFromModelList(this.clients);
        } else if (tableName == "client_type") {
            headers = this.getAttributes(ClientTypeModel.InnerClientTypeModel.class);
            data = this.getObjectsFromModelList(this.clients);
        } else if (tableName == "user") {
            headers = this.getAttributes(UserModel.InnerUserModel.class);
            data = this.getObjectsFromModelList(this.clients);
        } else if (tableName == "employee") {
            headers = this.getAttributes(EmployeeModel.InnerEmployeeModel.class);
            data = this.getObjectsFromModelList(this.employees);
        } else if (tableName == "salary") {
            headers = this.getAttributes(SalaryModel.InnerSalaryModel.class);
            data = this.getObjectsFromModelList(this.salaries);
        } else if (tableName == "objective") {
            headers = this.getAttributes(ObjectiveModel.InnerObjectiveModel.class);
            data = this.getObjectsFromModelList(this.objectives);
        }

        return this.getResultSet(headers, data);

    }

    @Override
    public void createAndInsert(String tableName, String[] columns, List<String[]> values) throws Exception {

    }

    @Override
    public void createTable(String tableName, String[] columns, String[] types) throws Exception {

    }

//    private List<Pair<String, String>> makeInsert(List<String> headers, List<Pair<String, String>> values) throws Exception {
//        List<Pair<String, String>> result = new ArrayList<>();
//
//        for(int i = 0; i < headers.size(); i++){
//            for(int j = 0; j < values.size(); j++){
//                if(headers.get(i).toLowerCase() == values.get(j).first.toLowerCase()){
//                    result.add(new Pair<>(headers.get(i), values.get(j).second));
//                }
//            }
//        }
//
//        return result;
//    }


    @Override
    public void insert(String tableName, List<Pair<String, String>> values) throws Exception {
        List<String> headers = null;
        if (tableName.equals("camera_type")) {
            // Get largest IDTip from cameraTypes
            int idTip = 0;
            for (CameraTypeModel.InnerCameraTypeModel c : this.cameraTypes.getList()) {
                if (c.IDTip > idTip)
                    idTip = c.IDTip;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idtip")) {
                    p.second = String.valueOf(idTip + 1);
                }
            }


            CameraTypeModel.InnerCameraTypeModel model = new CameraTypeModel.InnerCameraTypeModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        }
                    }
                }
            }

            this.cameraTypes.getList().add(model);
        } else if (tableName.equals("camera")) {
            int id = 0;
            for (CameraModel.InnerCameraModel c : this.cameras.getList()) {
                if (c.IDCamera > id)
                    id = c.IDCamera;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idcamera")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            CameraModel.InnerCameraModel model = new CameraModel.InnerCameraModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        }
                    }
                }
            }

            this.cameras.getList().add(model);
        } else if (tableName.equals("format")) {
            int id = 0;
            for (FormatModel.InnerFormatModel c : this.formats.getList()) {
                if (c.IDFormat > id)
                    id = c.IDFormat;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idformat")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            FormatModel.InnerFormatModel model = new FormatModel.InnerFormatModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        }
                    }
                }
            }

            this.formats.getList().add(model);
        } else if (tableName.equals("client")) {
            int id = 0;
            for (ClientModel.InnerClientModel c : this.clients.getList()) {
                if (c.IDUtilizator > id)
                    id = c.IDUtilizator;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idutilizator")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            ClientModel.InnerClientModel model = new ClientModel.InnerClientModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        } else if (f.getType() == Date.class) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                f.set(model, sdf.parse(p.second));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (f.getType() == LocalDateTime.class) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            f.set(model, LocalDateTime.parse(p.second, dtf));
                        }
                    }
                }
            }

            this.clients.getList().add(model);

        } else if (tableName.equals("mount")) {
            int id = 0;
            for (MountModel.InnerMountModel c : this.mounts.getList()) {
                if (c.IDMontura > id)
                    id = c.IDMontura;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idmontura")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            MountModel.InnerMountModel model = new MountModel.InnerMountModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        }
                    }
                }
            }

            this.mounts.getList().add(model);
        } else if (tableName.equals("client_type")) {
            int id = 0;
            for (ClientTypeModel.InnerClientTypeModel c : this.clientTypes.getList()) {
                if (c.IDTip > id)
                    id = c.IDTip;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idtip")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            ClientTypeModel.InnerClientTypeModel model = new ClientTypeModel.InnerClientTypeModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        }
                    }
                }
            }

            this.clientTypes.getList().add(model);
        } else if (tableName.equals("user")) {
            int id = 0;
            for (UserModel.InnerUserModel c : this.users.getList()) {
                if (c.IDUtilizator > id)
                    id = c.IDUtilizator;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idutilizator")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            UserModel.InnerUserModel model = new UserModel.InnerUserModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        } else if (f.getType() == Date.class) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                f.set(model, sdf.parse(p.second));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (f.getType() == LocalDateTime.class) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            f.set(model, LocalDateTime.parse(p.second, dtf));
                        }
                    }
                }
            }

            this.users.getList().add(model);

        } else if (tableName.equals("employee")) {
            int id = 0;
            for (EmployeeModel.InnerEmployeeModel c : this.employees.getList()) {
                if (c.IDUtilizator > id)
                    id = c.IDUtilizator;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idutilizator")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            EmployeeModel.InnerEmployeeModel model = new EmployeeModel.InnerEmployeeModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        } else if (f.getType() == Date.class) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                f.set(model, sdf.parse(p.second));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (f.getType() == LocalDateTime.class) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            f.set(model, LocalDateTime.parse(p.second, dtf));
                        }
                    }
                }
            }

            this.employees.getList().add(model);

        } else if (tableName.equals("salary")) {
            int id = 0;
            for (SalaryModel.InnerSalaryModel c : this.salaries.getList()) {
                if (c.IDSalariu > id)
                    id = c.IDSalariu;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idsalariu")) {
                    p.second = String.valueOf(id + 1);
                }
            }


            SalaryModel.InnerSalaryModel model = new SalaryModel.InnerSalaryModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                        } else if (f.getType() == Date.class) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                f.set(model, sdf.parse(p.second));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (f.getType() == LocalDateTime.class) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            f.set(model, LocalDateTime.parse(p.second, dtf));
                        }
                    }
                }
            }

            this.salaries.getList().add(model);

        } else if (tableName.equals("objective")) {
            int id = 0;
            for (ObjectiveModel.InnerObjectiveModel c : this.objectives.getList()) {
                if (c.IDObiectiv > id)
                    id = c.IDObiectiv;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("idobiectiv")) {
                    p.second = String.valueOf(id + 1);
                    break;
                }
            }


            ObjectiveModel.InnerObjectiveModel model = new ObjectiveModel.InnerObjectiveModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        // Cast p.second to f.getType()
                        if (f.getType() == String.class) {
                            f.set(model, p.second);
                            break;
                        } else if (f.getType() == int.class) {
                            f.set(model, Integer.parseInt(p.second));
                            break;
                        } else if (f.getType() == double.class) {
                            f.set(model, Double.parseDouble(p.second));
                            break;
                        } else if (f.getType() == boolean.class) {
                            f.set(model, Boolean.parseBoolean(p.second));
                            break;
                        } else if (f.getType() == Date.class) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                f.set(model, sdf.parse(p.second));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        } else if (f.getType() == LocalDateTime.class) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            f.set(model, LocalDateTime.parse(p.second, dtf));
                            break;
                        }
                    }
                }
            }

            this.objectives.getList().add(model);

        }


    }

    private void deleteFromModelList(ModelList<?> list, Map<String, String> where) throws Exception {
        for (Object o : list.getList()) {
            boolean delete = true;
            for (Map.Entry<String, String> entry : where.entrySet()) {
                for (Field f : o.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(entry.getKey().toLowerCase())) {
                        f.setAccessible(true);
                        if (!f.get(o).toString().toLowerCase().equals(entry.getValue().toLowerCase())) {
                            delete = false;
                        }
                    }
                }
            }
            if (delete) {
                list.getList().remove(o);
                break;
            }
        }
    }

    @Override
    public void delete(String tableName, Map<String, String> where) throws Exception {
        List<String> headers = null;
        if (tableName == "camera_type") {
            this.deleteFromModelList(this.cameraTypes, where);
        } else if (tableName == "camera") {
            this.deleteFromModelList(this.cameras, where);
        } else if (tableName == "format") {
            this.deleteFromModelList(this.cameras, where);
        } else if (tableName == "mount") {
            this.deleteFromModelList(this.cameras, where);
        } else if (tableName == "client") {
            this.deleteFromModelList(this.clients, where);
        } else if (tableName == "client_type") {
            this.deleteFromModelList(this.clientTypes, where);
        } else if (tableName == "user") {
            this.deleteFromModelList(this.users, where);
        } else if (tableName == "employee") {
            this.deleteFromModelList(this.employees, where);
        } else if (tableName == "salary") {
            this.deleteFromModelList(this.salaries, where);
        }else if (tableName == "objective") {
            this.deleteFromModelList(this.objectives, where);
        }
    }

    @Override
    public void drop(String tableName) throws Exception {

    }

    @Override
    public void truncate(String tableName) throws Exception {

    }

    @Override
    public void init(String[] columns) throws Exception {

    }

    @Override
    public int getNewId(String tableName, String column) throws Exception {
        return -1;
    }

    private Reader readFile(String resourcePath) throws IOException {
        return null;
    }


}
