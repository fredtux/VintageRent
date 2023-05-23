package org.database.memory;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.database.DatabaseConnection;
import org.models.*;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemory extends DatabaseConnection {
    private static InMemory instance = null;
    private Map<String, ModelList<?>> modelLists = new HashMap<>();

    public InMemory() {
        if (instance != null)
            throw new RuntimeException("OracleConnection is a singleton class. Use getInstance() instead.");
        else {
            instance = this;
            instances.add(this);
        }

        ModelList<CameraTypeModel.InnerCameraTypeModel> cameraTypes = new ModelList<>();
        ModelList<CameraModel.InnerCameraModel> cameras = new ModelList<>();
        ModelList<FormatModel.InnerFormatModel> formats = new ModelList<>();
        ModelList<MountModel.InnerMountModel> mounts = new ModelList<>();
        ModelList<ClientModel.InnerClientModel> clients = new ModelList<>();
        ModelList<ClientTypeModel.InnerClientTypeModel> clientTypes = new ModelList<>();
        ModelList<UserModel.InnerUserModel> users = new ModelList<>();
        ModelList<EmployeeModel.InnerEmployeeModel> employees = new ModelList<>();
        ModelList<SalaryModel.InnerSalaryModel> salaries = new ModelList<>();
        ModelList<ObjectiveModel.InnerObjectiveModel> objectives = new ModelList<>();
        ModelList<RentModel.InnerRentModel> rents = new ModelList<>();
        ModelList<AdministratorModel.InnerAdministratorModel> administrators = new ModelList<>();
        ModelList<SubdomainModel.InnerSubdomainModel> subdomains = new ModelList<>();
        ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> administrator_subdomains = new ModelList<>();
        ModelList<AddressModel.InnerAddressModel> addresses = new ModelList<>();

        modelLists.put("camera_type", cameraTypes);
        modelLists.put("camera", cameras);
        modelLists.put("format", formats);
        modelLists.put("mount", mounts);
        modelLists.put("client", clients);
        modelLists.put("client_type", clientTypes);
        modelLists.put("user", users);
        modelLists.put("employee", employees);
        modelLists.put("salary", salaries);
        modelLists.put("objective", objectives);
        modelLists.put("rent", rents);
        modelLists.put("administrator", administrators);
        modelLists.put("subdomain", subdomains);
        modelLists.put("administrator_subdomain", administrator_subdomains);
        modelLists.put("address", addresses);
        
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
            ModelList<CameraModel.InnerCameraModel> modelList = ((ModelList<CameraModel.InnerCameraModel>)this.modelLists.get("camera"));
            for(CameraModel.InnerCameraModel data : modelList.getList()) {
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

                    modelLists.replace("camera", modelList);

                    break;
                }
            }
        } else if(tableName.equals("camera_type")) {
            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = ((ModelList<CameraTypeModel.InnerCameraTypeModel>)this.modelLists.get("camera_type"));
            for(CameraTypeModel.InnerCameraTypeModel data : modelList.getList()) {
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

                    modelLists.replace("camera_type", modelList);

                    break;
                }
            }
        } else if(tableName.equals("client")) {
            ModelList<ClientModel.InnerClientModel> modelList = ((ModelList<ClientModel.InnerClientModel>)this.modelLists.get("client"));
            for(ClientModel.InnerClientModel data : modelList.getList()) {
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
                    modelLists.replace("client", modelList);

                    break;
                }
            }
        } else if(tableName.equals("client_type")) {
            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = ((ModelList<ClientTypeModel.InnerClientTypeModel>)this.modelLists.get("client_type"));
            for(ClientTypeModel.InnerClientTypeModel data : modelList.getList()) {
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
                    modelLists.replace("client_type", modelList);

                    break;
                }
            }
        } else if(tableName.equals("employee")) {
            ModelList<EmployeeModel.InnerEmployeeModel> modelList = ((ModelList<EmployeeModel.InnerEmployeeModel>)this.modelLists.get("employee"));
            for(EmployeeModel.InnerEmployeeModel data : modelList.getList()) {
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
                    modelLists.replace("employee", modelList);

                    break;
                }
            }
        } else if(tableName.equals("format")) {
            ModelList<FormatModel.InnerFormatModel> modelList = ((ModelList<FormatModel.InnerFormatModel>)this.modelLists.get("format"));
            for(FormatModel.InnerFormatModel data : modelList.getList()) {
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
                    modelLists.replace("format", modelList);

                    break;
                }
            }
        } else if(tableName.equals("mount")) {
            ModelList<MountModel.InnerMountModel> modelList = ((ModelList<MountModel.InnerMountModel>)this.modelLists.get("mount"));
            for(MountModel.InnerMountModel data : modelList.getList()) {
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
                    modelLists.replace("mount", modelList);

                    break;
                }
            }
        } else if(tableName.equals("objective")) {
            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = ((ModelList<ObjectiveModel.InnerObjectiveModel>)this.modelLists.get("objective"));
            for(ObjectiveModel.InnerObjectiveModel data : modelList.getList()) {
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
                    modelLists.replace("objective", modelList);

                    break;
                }
            }
        } else if(tableName.equals("rent")) {
            ModelList<RentModel.InnerRentModel> modelList = ((ModelList<RentModel.InnerRentModel>)this.modelLists.get("rent"));
            for(RentModel.InnerRentModel data : modelList.getList()) {
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
                    modelLists.replace("rent", modelList);

                    break;
                }
            }
        } else if(tableName.equals("salary")) {
            ModelList<SalaryModel.InnerSalaryModel> modelList = ((ModelList<SalaryModel.InnerSalaryModel>)this.modelLists.get("salary"));
            for(SalaryModel.InnerSalaryModel data : modelList.getList()) {
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
                    modelLists.replace("salary", modelList);

                    break;
                }
            }
        } else if(tableName.equals("user")) {
            ModelList<UserModel.InnerUserModel> modelList = ((ModelList<UserModel.InnerUserModel>)this.modelLists.get("user"));
            for(UserModel.InnerUserModel data : modelList.getList()) {
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
                    modelLists.replace("user", modelList);

                    break;
                }
            }
        } else if(tableName.equals("administrator")) {
            ModelList<AdministratorModel.InnerAdministratorModel> modelList = ((ModelList<AdministratorModel.InnerAdministratorModel>)this.modelLists.get("administrator"));
            for(AdministratorModel.InnerAdministratorModel data : modelList.getList()) {
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
                                    f.set(data, set.get(key) == "1" ? true : false);
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
                    modelLists.replace("administrator", modelList);

                    break;
                }
            }
        } else if(tableName.equals("subdomain")) {
            ModelList<SubdomainModel.InnerSubdomainModel> modelList = ((ModelList<SubdomainModel.InnerSubdomainModel>)this.modelLists.get("subdomain"));
            for(SubdomainModel.InnerSubdomainModel data : modelList.getList()) {
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
                                    f.set(data, set.get(key) == "1" ? true : false);
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
                    modelLists.replace("subdomain", modelList);

                    break;
                }
            }
        } else if(tableName.equals("administrator_subdomain")) {
            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = ((ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>)this.modelLists.get("administrator_subdomain"));
            for(AdministratorSubdomainModel.InnerAdministratorSubdomainModel data : modelList.getList()) {
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
                                    f.set(data, set.get(key) == "1" ? true : false);
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
                    modelLists.replace("administrator_subdomain", modelList);

                    break;
                }
            }
        } else if(tableName.equals("address")) {
            ModelList<AddressModel.InnerAddressModel> modelList = ((ModelList<AddressModel.InnerAddressModel>)this.modelLists.get("address"));
            for(AddressModel.InnerAddressModel data : modelList.getList()) {
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
                                    f.set(data, set.get(key) == "1" ? true : false);
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
                    modelLists.replace("address", modelList);

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
            data = this.getObjectsFromModelList((ModelList<CameraTypeModel.InnerCameraTypeModel>)this.modelLists.get("camera_type"));
        } else if (tableName == "camera") {
            headers = this.getAttributes(CameraModel.InnerCameraModel.class);
            data = this.getObjectsFromModelList((ModelList<CameraModel.InnerCameraModel>)this.modelLists.get("camera"));
        } else if (tableName == "format") {
            headers = this.getAttributes(FormatModel.InnerFormatModel.class);
            data = this.getObjectsFromModelList((ModelList<FormatModel.InnerFormatModel>)this.modelLists.get("format"));
        } else if (tableName == "mount") {
            headers = this.getAttributes(MountModel.InnerMountModel.class);
            data = this.getObjectsFromModelList((ModelList<MountModel.InnerMountModel>)this.modelLists.get("mount"));
        } else if (tableName == "client") {
            headers = this.getAttributes(ClientModel.InnerClientModel.class);
            data = this.getObjectsFromModelList((ModelList<ClientModel.InnerClientModel>)this.modelLists.get("client"));
        } else if (tableName == "client_type") {
            headers = this.getAttributes(ClientTypeModel.InnerClientTypeModel.class);
            data = this.getObjectsFromModelList((ModelList<ClientTypeModel.InnerClientTypeModel>)this.modelLists.get("client_type"));
        } else if (tableName == "user") {
            headers = this.getAttributes(UserModel.InnerUserModel.class);
            data = this.getObjectsFromModelList((ModelList<UserModel.InnerUserModel>)this.modelLists.get("user"));
        } else if (tableName == "employee") {
            headers = this.getAttributes(EmployeeModel.InnerEmployeeModel.class);
            data = this.getObjectsFromModelList((ModelList<EmployeeModel.InnerEmployeeModel>)this.modelLists.get("employee"));
        } else if (tableName == "salary") {
            headers = this.getAttributes(SalaryModel.InnerSalaryModel.class);
            data = this.getObjectsFromModelList((ModelList<SalaryModel.InnerSalaryModel>)this.modelLists.get("salary"));
        } else if (tableName == "objective") {
            headers = this.getAttributes(ObjectiveModel.InnerObjectiveModel.class);
            data = this.getObjectsFromModelList((ModelList<ObjectiveModel.InnerObjectiveModel>)this.modelLists.get("objective"));
        } else if (tableName == "rent") {
            headers = this.getAttributes(RentModel.InnerRentModel.class);
            data = this.getObjectsFromModelList((ModelList<RentModel.InnerRentModel>)this.modelLists.get("rent"));
        } else if (tableName == "administrator") {
            headers = this.getAttributes(AdministratorModel.InnerAdministratorModel.class);
            data = this.getObjectsFromModelList((ModelList<AdministratorModel.InnerAdministratorModel>)this.modelLists.get("administrator"));
        } else if (tableName == "subdomain") {
            headers = this.getAttributes(SubdomainModel.InnerSubdomainModel.class);
            data = this.getObjectsFromModelList((ModelList<SubdomainModel.InnerSubdomainModel>)this.modelLists.get("subdomain"));
        } else if (tableName == "administrator_subdomain") {
            headers = this.getAttributes(AdministratorSubdomainModel.InnerAdministratorSubdomainModel.class);
            data = this.getObjectsFromModelList((ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>)this.modelLists.get("administrator_subdomain"));
        } else if (tableName == "address") {
            headers = this.getAttributes(AddressModel.InnerAddressModel.class);
            data = this.getObjectsFromModelList((ModelList<AddressModel.InnerAddressModel>)this.modelLists.get("address"));
        }

        return this.getResultSet(headers, data);

    }

    @Override
    public void createAndInsert(String tableName, String[] columns, List<String[]> values) throws Exception {

    }

    @Override
    public void createTable(String tableName, String[] columns, String[] types) throws Exception {

    }


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
            for (CameraTypeModel.InnerCameraTypeModel c : ((ModelList<CameraTypeModel.InnerCameraTypeModel>) this.modelLists.get("camera_type")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<CameraTypeModel.InnerCameraTypeModel> list = (ModelList<CameraTypeModel.InnerCameraTypeModel>)this.modelLists.get("camera_type");
            list.getList().add(model);
            modelLists.put("camera_type", list);
        } else if (tableName.equals("camera")) {
            int id = 0;
            for (CameraModel.InnerCameraModel c : ((ModelList<CameraModel.InnerCameraModel>) this.modelLists.get("camera")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<CameraModel.InnerCameraModel> list = (ModelList<CameraModel.InnerCameraModel>)this.modelLists.get("camera");
            list.getList().add(model);
            modelLists.put("camera", list);
        } else if (tableName.equals("format")) {
            int id = 0;
            for (FormatModel.InnerFormatModel c : ((ModelList<FormatModel.InnerFormatModel>) this.modelLists.get("format")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<FormatModel.InnerFormatModel> list = (ModelList<FormatModel.InnerFormatModel>)this.modelLists.get("format");
            list.getList().add(model);
            modelLists.put("format", list);
        } else if (tableName.equals("client")) {
            int id = 0;
            for (ClientModel.InnerClientModel c : ((ModelList<ClientModel.InnerClientModel>) this.modelLists.get("client")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<ClientModel.InnerClientModel> list = (ModelList<ClientModel.InnerClientModel>)this.modelLists.get("client");
            list.getList().add(model);
            modelLists.put("client", list);

        } else if (tableName.equals("mount")) {
            int id = 0;
            for (MountModel.InnerMountModel c : ((ModelList<MountModel.InnerMountModel>) this.modelLists.get("mount")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<MountModel.InnerMountModel> list = (ModelList<MountModel.InnerMountModel>)this.modelLists.get("mount");
            list.getList().add(model);
            modelLists.put("mount", list);
        } else if (tableName.equals("client_type")) {
            int id = 0;
            for (ClientTypeModel.InnerClientTypeModel c : ((ModelList<ClientTypeModel.InnerClientTypeModel>) this.modelLists.get("client_type")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<ClientTypeModel.InnerClientTypeModel> list = (ModelList<ClientTypeModel.InnerClientTypeModel>)this.modelLists.get("client_type");
            list.getList().add(model);
            modelLists.put("client_type", list);
        } else if (tableName.equals("user")) {
            int id = 0;
            for (UserModel.InnerUserModel c : ((ModelList<UserModel.InnerUserModel>) this.modelLists.get("user")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<UserModel.InnerUserModel> list = (ModelList<UserModel.InnerUserModel>)this.modelLists.get("user");
            list.getList().add(model);
            modelLists.put("user", list);

        } else if (tableName.equals("employee")) {
            int id = 0;
            for (EmployeeModel.InnerEmployeeModel c : ((ModelList<EmployeeModel.InnerEmployeeModel>) this.modelLists.get("employee")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<EmployeeModel.InnerEmployeeModel> list = (ModelList<EmployeeModel.InnerEmployeeModel>)this.modelLists.get("employee");
            list.getList().add(model);
            modelLists.put("employee", list);

        } else if (tableName.equals("salary")) {
            int id = 0;
            for (SalaryModel.InnerSalaryModel c : ((ModelList<SalaryModel.InnerSalaryModel>) this.modelLists.get("salary")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<SalaryModel.InnerSalaryModel> list = (ModelList<SalaryModel.InnerSalaryModel>)this.modelLists.get("salary");
            list.getList().add(model);
            modelLists.put("salary", list);

        } else if (tableName.equals("objective")) {
            int id = 0;
            for (ObjectiveModel.InnerObjectiveModel c : ((ModelList<ObjectiveModel.InnerObjectiveModel>) this.modelLists.get("objective")).getList()) {
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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<ObjectiveModel.InnerObjectiveModel> list = (ModelList<ObjectiveModel.InnerObjectiveModel>)this.modelLists.get("objective");
            list.getList().add(model);
            modelLists.put("objective", list);

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
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<RentModel.InnerRentModel> list = (ModelList<RentModel.InnerRentModel>)this.modelLists.get("rent");
            list.getList().add(model);
            modelLists.put("rent", list);

        } else if (tableName.equals("administrator")) {
            int id = 0;
            for (AdministratorModel.InnerAdministratorModel c : ((ModelList<AdministratorModel.InnerAdministratorModel>) this.modelLists.get("administrator")).getList()) {
                if (c.UserID > id)
                    id = c.UserID;
            }


            AdministratorModel.InnerAdministratorModel model = new AdministratorModel.InnerAdministratorModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<AdministratorModel.InnerAdministratorModel> list = (ModelList<AdministratorModel.InnerAdministratorModel>)this.modelLists.get("administrator");
            list.getList().add(model);
            modelLists.put("administrator", list);

        } else if (tableName.equals("subdomain")) {
            int id = 0;
            for (SubdomainModel.InnerSubdomainModel c : ((ModelList<SubdomainModel.InnerSubdomainModel>) this.modelLists.get("subdomain")).getList()) {
                if (c.SubdomainID > id)
                    id = c.SubdomainID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("subdomainid")) {
                    p.second = String.valueOf(id + 1);
                    break;
                }
            }


            SubdomainModel.InnerSubdomainModel model = new SubdomainModel.InnerSubdomainModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        f.setAccessible(true);
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<SubdomainModel.InnerSubdomainModel> list = (ModelList<SubdomainModel.InnerSubdomainModel>)this.modelLists.get("subdomain");
            list.getList().add(model);
            modelLists.put("subdomain", list);

        } else if (tableName.equals("administrator_subdomain")) {
            int id = 0;
            for (AdministratorSubdomainModel.InnerAdministratorSubdomainModel c : ((ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>) this.modelLists.get("administrator_subdomain")).getList()) {
                if (c.IDAdministrator > id)
                    id = c.IDAdministrator;
            }


            AdministratorSubdomainModel.InnerAdministratorSubdomainModel model = new AdministratorSubdomainModel.InnerAdministratorSubdomainModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> list = (ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>)this.modelLists.get("administrator_subdomain");
            list.getList().add(model);
            modelLists.put("administrator_subdomain", list);

        } else if (tableName.equals("address")) {
            int id = 0;
            for (AddressModel.InnerAddressModel c : ((ModelList<AddressModel.InnerAddressModel>) this.modelLists.get("address")).getList()) {
                if (c.AddressID > id)
                    id = c.AddressID;
            }

            for (Pair<String, String> p : values) {
                if (p.first.toLowerCase().equals("addressid")) {
                    p.second = String.valueOf(id + 1);
                    break;
                }
            }


            AddressModel.InnerAddressModel model = new AddressModel.InnerAddressModel();
            for (Pair<String, String> p : values) {
                for (Field f : model.getClass().getDeclaredFields()) {
                    if (f.getName().toLowerCase().equals(p.first.toLowerCase())) {
                        if(this.processField(f, model, p))
                            break;
                    }
                }
            }

            ModelList<AddressModel.InnerAddressModel> list = (ModelList<AddressModel.InnerAddressModel>)this.modelLists.get("address");
            list.getList().add(model);
            modelLists.put("address", list);

        }


    }

    private boolean processField(Field f, Model.AbstractInnerModel model, Pair<String, String> p) throws Exception {
        f.setAccessible(true);
        // Cast p.second to f.getType()
        if (f.getType() == String.class) {
            f.set(model, p.second);
            return true;
        } else if (f.getType() == int.class) {
            f.set(model, Integer.parseInt(p.second));
            return true;
        } else if (f.getType() == double.class) {
            f.set(model, Double.parseDouble(p.second));
            return true;
        } else if (f.getType() == boolean.class) {
            f.set(model, Boolean.parseBoolean(p.second));
            return true;
        } else if (f.getType() == Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                f.set(model, sdf.parse(p.second));
            } catch (Exception e) {
                try{
                    f.set(model, Date.valueOf(p.second));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return true;
        } else if (f.getType() == LocalDateTime.class) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            f.set(model, LocalDateTime.parse(p.second, dtf));
            return true;
        }

        return false;
    }

    private void deleteFromModelList(ModelList<?> list, Map<String, String> where, String table_name) throws Exception {
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
                modelLists.replace(table_name, list);
                break;
            }
        }
    }

    @Override
    public void delete(String tableName, Map<String, String> where) throws Exception {
        List<String> headers = null;
        if (tableName == "camera_type") {
            this.deleteFromModelList((ModelList<CameraTypeModel.InnerCameraTypeModel>)this.modelLists.get("camera_type"), where, "camera_type");
        } else if (tableName == "camera") {
            this.deleteFromModelList((ModelList<CameraModel.InnerCameraModel>)this.modelLists.get("camera"), where, "camera");
        } else if (tableName == "format") {
            this.deleteFromModelList((ModelList<FormatModel.InnerFormatModel>)this.modelLists.get("format"), where, "format");
        } else if (tableName == "mount") {
            this.deleteFromModelList((ModelList<MountModel.InnerMountModel>)this.modelLists.get("mount"), where, "mount");
        } else if (tableName == "client") {
            this.deleteFromModelList((ModelList<ClientModel.InnerClientModel>)this.modelLists.get("client"), where, "client");
        } else if (tableName == "client_type") {
            this.deleteFromModelList((ModelList<ClientTypeModel.InnerClientTypeModel>)this.modelLists.get("client_type"), where, "client_type");
        } else if (tableName == "user") {
            this.deleteFromModelList((ModelList<UserModel.InnerUserModel>)this.modelLists.get("user"), where, "user");
        } else if (tableName == "employee") {
            this.deleteFromModelList((ModelList<EmployeeModel.InnerEmployeeModel>)this.modelLists.get("employee"), where, "employee");
        } else if (tableName == "salary") {
            this.deleteFromModelList((ModelList<SalaryModel.InnerSalaryModel>)this.modelLists.get("salary"), where, "salary");
        } else if (tableName == "objective") {
            this.deleteFromModelList((ModelList<ObjectiveModel.InnerObjectiveModel>)this.modelLists.get("objective"), where, "objective");
        } else if (tableName == "rent") {
            this.deleteFromModelList((ModelList<RentModel.InnerRentModel>)this.modelLists.get("rent"), where, "rent");
        } else if (tableName == "administrator") {
            this.deleteFromModelList((ModelList<AdministratorModel.InnerAdministratorModel>)this.modelLists.get("administrator"), where, "administrator");
        } else if (tableName == "subdomain") {
            this.deleteFromModelList((ModelList<SubdomainModel.InnerSubdomainModel>)this.modelLists.get("subdomain"), where, "subdomain");
        } else if (tableName == "administrator_subdomain") {
            this.deleteFromModelList((ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>)this.modelLists.get("administrator_subdomain"), where, "administrator_subdomain");
        } else if (tableName == "address") {
            this.deleteFromModelList((ModelList<AddressModel.InnerAddressModel>)this.modelLists.get("address"), where, "address");
        }
    }

    @Override
    public void drop(String tableName) throws Exception {

    }

    @Override
    public void truncate(String tableName) throws Exception {
        if (tableName == "camera_type") {
            ModelList<CameraTypeModel.InnerCameraTypeModel> model = (ModelList<CameraTypeModel.InnerCameraTypeModel>) this.modelLists.get("camera_type");
            model.getList().clear();
            this.modelLists.put("camera_type", model);
        } else if (tableName == "camera") {
            ModelList<CameraModel.InnerCameraModel> model = (ModelList<CameraModel.InnerCameraModel>) this.modelLists.get("camera");
            model.getList().clear();
            this.modelLists.put("camera", model);
        } else if (tableName == "format") {
            ModelList<FormatModel.InnerFormatModel> model = (ModelList<FormatModel.InnerFormatModel>) this.modelLists.get("format");
            model.getList().clear();
            this.modelLists.put("format", model);
        } else if (tableName == "mount") {
            ModelList<MountModel.InnerMountModel> model = (ModelList<MountModel.InnerMountModel>) this.modelLists.get("mount");
            model.getList().clear();
            this.modelLists.put("mount", model);
        } else if (tableName == "client") {
            ModelList<ClientModel.InnerClientModel> model = (ModelList<ClientModel.InnerClientModel>) this.modelLists.get("client");
            model.getList().clear();
            this.modelLists.put("client", model);
        } else if (tableName == "client_type") {
            ModelList<ClientTypeModel.InnerClientTypeModel> model = (ModelList<ClientTypeModel.InnerClientTypeModel>) this.modelLists.get("client_type");
            model.getList().clear();
            this.modelLists.put("client_type", model);
        } else if (tableName == "user") {
            ModelList<UserModel.InnerUserModel> model = (ModelList<UserModel.InnerUserModel>) this.modelLists.get("user");
            model.getList().clear();
            this.modelLists.put("user", model);
        } else if (tableName == "employee") {
            ModelList<EmployeeModel.InnerEmployeeModel> model = (ModelList<EmployeeModel.InnerEmployeeModel>) this.modelLists.get("employee");
            model.getList().clear();
            this.modelLists.put("employee", model);
        } else if (tableName == "salary") {
            ModelList<SalaryModel.InnerSalaryModel> model = (ModelList<SalaryModel.InnerSalaryModel>) this.modelLists.get("salary");
            model.getList().clear();
            this.modelLists.put("salary", model);
        } else if (tableName == "objective") {
            ModelList<ObjectiveModel.InnerObjectiveModel> model = (ModelList<ObjectiveModel.InnerObjectiveModel>) this.modelLists.get("objective");
            model.getList().clear();
            this.modelLists.put("objective", model);
        } else if (tableName == "rent") {
            ModelList<RentModel.InnerRentModel> model = (ModelList<RentModel.InnerRentModel>) this.modelLists.get("rent");
            model.getList().clear();
            this.modelLists.put("rent", model);
        } else if (tableName == "administrator") {
            ModelList<AdministratorModel.InnerAdministratorModel> model = (ModelList<AdministratorModel.InnerAdministratorModel>) this.modelLists.get("administrator");
            model.getList().clear();
            this.modelLists.put("administrator", model);
        } else if (tableName == "subdomain") {
            ModelList<SubdomainModel.InnerSubdomainModel> model = (ModelList<SubdomainModel.InnerSubdomainModel>) this.modelLists.get("subdomain");
            model.getList().clear();
            this.modelLists.put("subdomain", model);
        } else if (tableName == "administrator_subdomain") {
            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> model = (ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>) this.modelLists.get("administrator_subdomain");
            model.getList().clear();
            this.modelLists.put("administrator_subdomain", model);
        } else if (tableName == "address") {
            ModelList<AddressModel.InnerAddressModel> model = (ModelList<AddressModel.InnerAddressModel>) this.modelLists.get("address");
            model.getList().clear();
            this.modelLists.put("address", model);
        }

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
