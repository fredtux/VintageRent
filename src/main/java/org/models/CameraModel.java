package org.models;

import org.database.DatabaseConnection;
import org.database.oracle.OracleConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.ResultSet;

public class CameraModel extends Model implements LinkModelToDatabase<ModelList> {
    public static class InnerCameraModel implements Comparable<InnerCameraModel> {
        public String Marca;
        public String ModelCamera;
        public int AnFabricatie;
        public double Pret;
        public double PretInchiriere;
        public int IDCamera;

        public String DenumireFormat;
        public String LatimeFilm;
        public String DenumireTip;
        public String DenumireMontura;

        @Override
        public int compareTo(InnerCameraModel o) {
            return this.IDCamera - o.IDCamera;
        }
    }
    private String tableName = "CAMERE";

    private static CameraModel instance = null;
    private ModelList<InnerCameraModel> modelList = null;

    public ModelList<InnerCameraModel> getModelList() {
        return modelList;
    }

    public static CameraModel getInstance() {
        if (instance == null)
            instance = new CameraModel();

        return instance;
    }

    public DefaultTableModel getTableModel() {
        String columns[] = {"IDCamera", "Marca", "ModelCamera", "AnFabricatie", "Pret", "PretInchiriere", "DenumireFormat", "LatimeFilm", "DenumireTip", "DenumireMontura"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerCameraModel model : this.modelList.getList()){
            Object obj[] = {model.IDCamera, model.Marca, model.ModelCamera, model.AnFabricatie, model.Pret, model.PretInchiriere, model.DenumireFormat, model.LatimeFilm, model.DenumireTip, model.DenumireMontura};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public CameraModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Camera";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<InnerCameraModel>(true);

        while(rs.next()){
            InnerCameraModel model = new InnerCameraModel();
            model.Marca = rs.getString("Marca");
            model.ModelCamera = rs.getString("ModelCamera");
            model.AnFabricatie = rs.getInt("AnFabricatie");
            model.Pret = rs.getDouble("Pret");
            model.PretInchiriere = rs.getDouble("PretInchiriere");
            model.IDCamera = rs.getInt("IDCamera");
            model.DenumireTip = rs.getString("DenumireTip");
            model.DenumireFormat = rs.getString("DenumireFormat");
            model.LatimeFilm = rs.getString("LatimeFilm");
            model.DenumireMontura = rs.getString("DenumireMontura");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerCameraModel> getData() throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT IDCAMERA, MARCA, MODELCAMERA, F.DENUMIRE AS DENUMIREFORMAT, F.LATIMEFILM, T.DENUMIRE AS DENUMIRETIP, M.DENUMIRE AS DENUMIREMONTURA, ANFABRICATIE, PRET, PRETINCHIRIERE\n" +
                "FROM CAMERE\n" +
                "    INNER JOIN FORMAT F on CAMERE.IDFORMAT = F.IDFORMAT\n" +
                "    INNER JOIN TIPCAMERA T on CAMERE.IDTIP = T.IDTIP\n" +
                "    INNER JOIN MONTURA M on CAMERE.IDMONTURA = M.IDMONTURA");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerCameraModel> getData(String whereClause)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT IDCAMERA, MARCA, MODELCAMERA, F.DENUMIRE AS DENUMIREFORMAT, F.LATIMEFILM, T.DENUMIRE AS DENUMIRETIP, M.DENUMIRE AS DENUMIREMONTURA, ANFABRICATIE, PRET, PRETINCHIRIERE\n" +
                "FROM CAMERE\n" +
                "    INNER JOIN FORMAT F on CAMERE.IDFORMAT = F.IDFORMAT\n" +
                "    INNER JOIN TIPCAMERA T on CAMERE.IDTIP = T.IDTIP\n" +
                "    INNER JOIN MONTURA M on CAMERE.IDMONTURA = M.IDMONTURA" + " WHERE " + whereClause);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerCameraModel> getData(String whereClause, String orderBy)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT IDCAMERA, MARCA, MODELCAMERA, F.DENUMIRE AS DENUMIREFORMAT, F.LATIMEFILM, T.DENUMIRE AS DENUMIRETIP, M.DENUMIRE AS DENUMIREMONTURA, ANFABRICATIE, PRET, PRETINCHIRIERE\n" +
                "FROM CAMERE\n" +
                "    INNER JOIN FORMAT F on CAMERE.IDFORMAT = F.IDFORMAT\n" +
                "    INNER JOIN TIPCAMERA T on CAMERE.IDTIP = T.IDTIP\n" +
                "    INNER JOIN MONTURA M on CAMERE.IDMONTURA = M.IDMONTURA" + " WHERE " + whereClause + " ORDER BY " + orderBy);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerCameraModel> getData(String whereClause, String orderBy, String limit)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT IDCAMERA, MARCA, MODELCAMERA, F.DENUMIRE AS DENUMIREFORMAT, F.LATIMEFILM, T.DENUMIRE AS DENUMIRETIP, M.DENUMIRE AS DENUMIREMONTURA, ANFABRICATIE, PRET, PRETINCHIRIERE\n" +
                "FROM CAMERE\n" +
                "    INNER JOIN FORMAT F on CAMERE.IDFORMAT = F.IDFORMAT\n" +
                "    INNER JOIN TIPCAMERA T on CAMERE.IDTIP = T.IDTIP\n" +
                "    INNER JOIN MONTURA M on CAMERE.IDMONTURA = M.IDMONTURA" + " WHERE " + whereClause + " ORDER BY " + orderBy + " FETCH NEXT " + limit + " ROWS ONLY");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerCameraModel> getData(String whereClause, String orderBy, String limit, String offset)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT IDCAMERA, MARCA, MODELCAMERA, F.DENUMIRE AS DENUMIREFORMAT, F.LATIMEFILM, T.DENUMIRE AS DENUMIRETIP, M.DENUMIRE AS DENUMIREMONTURA, ANFABRICATIE, PRET, PRETINCHIRIERE\n" +
                "FROM CAMERE\n" +
                "    INNER JOIN FORMAT F on CAMERE.IDFORMAT = F.IDFORMAT\n" +
                "    INNER JOIN TIPCAMERA T on CAMERE.IDTIP = T.IDTIP\n" +
                "    INNER JOIN MONTURA M on CAMERE.IDMONTURA = M.IDMONTURA"+ " WHERE " + whereClause + " ORDER BY " + orderBy + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public void updateData(ModelList row) throws Exception {
        ModelList<InnerCameraModel> oneRow = (ModelList<InnerCameraModel>) row;

        DatabaseConnection db = OracleConnection.getInstance();
        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }
}
