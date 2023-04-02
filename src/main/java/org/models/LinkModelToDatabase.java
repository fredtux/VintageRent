package org.models;

public interface LinkModelToDatabase<T extends ModelList, U extends Model.AbstractInnerModel> {
    public void getFilteredData(String comparator, String value, String column) throws Exception;
    public T getData() throws Exception;
    public void updateData(T row) throws Exception;
    public void deleteRow(T row) throws Exception;
    public void throwIntoCsv() throws Exception;
    public void insertRow(U row) throws Exception;
    public void truncate() throws Exception;
}
