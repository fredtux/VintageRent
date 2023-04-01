package org.actions;

import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;
import org.models.*;
import org.vintage.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MainServiceTest {
    DatabaseConnection inmem = null;

    public MainServiceTest(){
        try {
            this.inmem = this.getInMemory();
            ModelInit.inmemInit();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private DatabaseConnection getInMemory(){
        DatabaseConnection result = null;
        try{
            result = new InMemory();
        } catch (Exception e) {
            result = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.INMEMORY);
        }

        return result;
    }

    @Test
    public void formatSalesTest(){
        try {
            Map<String, String> result = MainService.formatSales(1, DatabaseConnection.DatabaseType.INMEMORY);
            assertNotNull(result);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void ClientRentsTest(){
        try {
            Map<String, String> result = MainService.ClientRents(5, DatabaseConnection.DatabaseType.INMEMORY);
            assertNotNull(result);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void sendEmailTest(){
        try {
            boolean result = MainService.sendEmail("",1, "ClientReport", DatabaseConnection.DatabaseType.INMEMORY);
            assertFalse(result);

            result = MainService.sendEmail("",1, "FormatSalesReport", DatabaseConnection.DatabaseType.INMEMORY);
            assertFalse(result);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getForMountTest(){
        try{
            // Add new Mount
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            MountModel.InnerMountModel dataMount = new MountModel.InnerMountModel();
            dataMount.MountID = 1;
            dataMount.Name = "Test";

            MainService.insert(mountModel, dataMount);

            // Add 8 new cameras
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            CameraModel.InnerCameraModel dataCamera = new CameraModel.InnerCameraModel();
            dataCamera.IDCamera = 0;
            dataCamera.ModelCamera = "Test";
            dataCamera.MountID = 1;
            dataCamera.FormatID = 1;
            dataCamera.RentalPrice = 100;
            dataCamera.Price = 100;
            dataCamera.ManufacturingYear = 1970;
            dataCamera.Brand = "Test";
            dataCamera.TypeID = 1;

            for(int i = 0; i < 8; ++i){
                ++dataCamera.IDCamera;
                MainService.insert(cameraModel, dataCamera);
            }

            // Add 8 new objectives
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(objectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(objectiveModel);

            ObjectiveModel.InnerObjectiveModel objectiveData = new ObjectiveModel.InnerObjectiveModel();
            objectiveData.ObjectiveID = 0;
            objectiveData.RentalPrice = 100;
            objectiveData.Price = 100;
            objectiveData.Name = "Test";
            objectiveData.MaximumAperture = 0.0;
            objectiveData.MinimumAperture = 0.0;
            objectiveData.FocalDistance = 100;
            objectiveData.Diameter = 100;
            objectiveData.MountID = 1;

            for(int i = 0; i < 8; ++i){
                ++objectiveData.ObjectiveID;
                MainService.insert(objectiveModel, objectiveData);
            }

            // Get report results
            List< Model.AbstractInnerModel> result = MainService.getForMount(1, DatabaseConnection.DatabaseType.INMEMORY);

            // Check results
            assertNotNull(result);
            if(result.size() < 8){
                fail("Not enough data");
            }
            int objectives = 0, cameras = 0;
            for(Model.AbstractInnerModel model : result){
                if(model instanceof ObjectiveModel.InnerObjectiveModel){
                    ++objectives;
                } else if(model instanceof CameraModel.InnerCameraModel){
                    ++cameras;
                }
            }

            if(objectives < 8){
                fail("Not enough objectives");
            }
            if(cameras < 8){
                fail("Not enough cameras");
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}