package org.models;

public interface LinkModelToDatabase<T extends ModelList, U extends Model.AbstractInnerModel> {
    public T getData() throws Exception;
    public void updateData(T row) throws Exception;
    public void deleteRow(T row) throws Exception;
    public void throwIntoCsv() throws Exception;
    public void insertRow(U row) throws Exception;
}
