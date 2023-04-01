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
    private ModelList<RentModel.InnerRentModel> rents = null;

    private List<ModelList> modelLists = new ArrayList<>();

    public InMemory() {
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
        rents = new ModelList<>();

        modelLists.add(cameraTypes);
        modelLists.add(cameras);
        modelLists.add(formats);
        modelLists.add(mounts);
        modelLists.add(clients);
        modelLists.add(clientTypes);
        modelLists.add(users);
        modelLists.add(employees);
        modelLists.add(salaries);
        modelLists.add(objectives);
        modelLists.add(rents);
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
        for(String key : set.keySet()) {
            if(set.get(key).substring(0, 1).equals("'") && set.get(key).substring(set.get(key).length() - 1).equals("'")) {
                set.put(key, set.get(key).substring(1, set.get(key).length() - 1));
            }
        }

        if(tableName.equals("camera")) {
            for(CameraModel.InnerCameraModel data : cameras.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("camera_type")) {
            for(CameraTypeModel.InnerCameraTypeModel data : cameraTypes.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("client")) {
            for(ClientModel.InnerClientModel data : clients.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("client_type")) {
            for(ClientTypeModel.InnerClientTypeModel data : clientTypes.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("employee")) {
            for(EmployeeModel.InnerEmployeeModel data : employees.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("format")) {
            for(FormatModel.InnerFormatModel data : formats.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("mount")) {
            for(MountModel.InnerMountModel data : mounts.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("objective")) {
            for(ObjectiveModel.InnerObjectiveModel data : objectives.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("rent")) {
            for(RentModel.InnerRentModel data : rents.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, Date.valueOf(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("salary")) {
            for(SalaryModel.InnerSalaryModel data : salaries.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        } else if(tableName.equals("user")) {
            for(UserModel.InnerUserModel data : users.getList()) {
                int counter = 0;
                for(String key : where.keySet()) {
                    for(Field f : data.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                            if(f.get(data).toString().equals(where.get(key))) {
                                ++counter;
                                break;
                            }
                        }
                    }
                }

                if(counter == where.size()){
                    for(String key : set.keySet()) {
                        for(Field f : data.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if(f.getName().toLowerCase().replace("_", "").equals(key.toLowerCase())) {
                                if(f.getType().equals(String.class)) {
                                    f.set(data, set.get(key));
                                    break;
                                } else if(f.getType().equals(int.class)) {
                                    f.set(data, Integer.parseInt(set.get(key)));
                                    break;
                                } else if(f.getType().equals(double.class)) {
                                    f.set(data, Double.parseDouble(set.get(key)));
                                    break;
                                } else if(f.getType().equals(float.class)) {
                                    f.set(data, Float.parseFloat(set.get(key)));
                                    break;
                                } else if(f.getType().equals(boolean.class)) {
                                    f.set(data, Boolean.parseBoolean(set.get(key)));
                                    break;
                                } else if(f.getType().equals(LocalDateTime.class)) {
                                    f.set(data, LocalDateTime.parse(set.get(key), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                    break;
                                } else if(f.getType().equals(Date.class)) {
                                    f.set(data, new SimpleDateFormat("yyyy-MM-dd").parse(set.get(key)));
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }
        }
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
            attributes.add(f.getName().replace("_", ""));
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
            data = this.getObjectsFromModelList(this.clientTypes);
        } else if (tableName == "user") {
            headers = this.getAttributes(UserModel.InnerUserModel.class);
            data = this.getObjectsFromModelList(this.users);
        } else if (tableName == "employee") {
            headers = this.getAttributes(EmployeeModel.InnerEmployeeModel.class);
            data = this.getObjectsFromModelList(this.employees);
        } else if (tableName == "salary") {
            headers = this.getAttributes(SalaryModel.InnerSalaryModel.class);
            data = this.getObjectsFromModelList(this.salaries);
        } else if (tableName == "objective") {
            headers = this.getAttributes(ObjectiveModel.InnerObjectiveModel.class);
            data = this.getObjectsFromModelList(this.objectives);
        } else if (tableName == "rent") {
            headers = this.getAttributes(RentModel.InnerRentModel.class);
            data = this.getObjectsFromModelList(this.rents);
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

        for(Pair<String, String> p: values){
            if(p.second.length() >= 2 && p.second.substring(0,1).equals("'") && p.second.substring(p.second.length()-1).equals("'"))
                p.second = p.second.substring(1, p.second.length()-1);
        }

        if (tableName.equals("camera_type")) {
            // Get largest TypeID from cameraTypes
            int idTip = 0;
            for (CameraTypeModel.InnerCameraTypeModel c : this.cameraTypes.getList()) {
                if (c.TypeID > idTip)
                    idTip = c.TypeID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("typeid")) {
                    p.second = String.valueOf(idTip + 1);

                    break;
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
                    break;
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
                        }
                    }
                }
            }

            this.cameras.getList().add(model);
        } else if (tableName.equals("format")) {
            int id = 0;
            for (FormatModel.InnerFormatModel c : this.formats.getList()) {
                if (c.FormatID > id)
                    id = c.FormatID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("formatid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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
                        }
                    }
                }
            }

            this.formats.getList().add(model);
        } else if (tableName.equals("client")) {
            int id = 0;
            for (ClientModel.InnerClientModel c : this.clients.getList()) {
                if (c.UserID > id)
                    id = c.UserID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("userid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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

            this.clients.getList().add(model);

        } else if (tableName.equals("mount")) {
            int id = 0;
            for (MountModel.InnerMountModel c : this.mounts.getList()) {
                if (c.MountID > id)
                    id = c.MountID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("mountid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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
                        }
                    }
                }
            }

            this.mounts.getList().add(model);
        } else if (tableName.equals("client_type")) {
            int id = 0;
            for (ClientTypeModel.InnerClientTypeModel c : this.clientTypes.getList()) {
                if (c.TypeID > id)
                    id = c.TypeID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("typeid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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
                        }
                    }
                }
            }

            this.clientTypes.getList().add(model);
        } else if (tableName.equals("user")) {
            int id = 0;
            for (UserModel.InnerUserModel c : this.users.getList()) {
                if (c.UserID > id)
                    id = c.UserID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("userid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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

            this.users.getList().add(model);

        } else if (tableName.equals("employee")) {
            int id = 0;
            for (EmployeeModel.InnerEmployeeModel c : this.employees.getList()) {
                if (c.UserID > id)
                    id = c.UserID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("userid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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

            this.employees.getList().add(model);

        } else if (tableName.equals("salary")) {
            int id = 0;
            for (SalaryModel.InnerSalaryModel c : this.salaries.getList()) {
                if (c.SalaryID > id)
                    id = c.SalaryID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("salaryid")) {
                    p.second = String.valueOf(id + 1);
                    break;
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

            this.salaries.getList().add(model);

        } else if (tableName.equals("objective")) {
            int id = 0;
            for (ObjectiveModel.InnerObjectiveModel c : this.objectives.getList()) {
                if (c.ObjectiveID > id)
                    id = c.ObjectiveID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("objectiveid")) {
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
                            try {
                                f.set(model, Date.valueOf(p.second));
                            } catch (Exception e) {
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

        } else if (tableName.equals("rent")) {
            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("rentdate")) {
                    p.second = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    break;
                }
            }


            RentModel.InnerRentModel model = new RentModel.InnerRentModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().replace("_", "").equals(p.first.toLowerCase())) {
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
                            try {
                                f.set(model, Date.valueOf(p.second));
                            } catch (Exception e) {
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

            this.rents.getList().add(model);

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
        } else if (tableName == "objective") {
            this.deleteFromModelList(this.objectives, where);
        } else if (tableName == "rent") {
            this.deleteFromModelList(this.rents, where);
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
