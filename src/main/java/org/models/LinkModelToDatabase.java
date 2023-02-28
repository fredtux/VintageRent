package org.models;

public interface LinkModelToDatabase<T extends ModelList, U extends Model.AbstractInnerModel> {
    public T getData() throws Exception;
    public T getData(String whereClause) throws Exception;
    public T getData(String whereClause, String orderBy) throws Exception;
    public T getData(String whereClause, String orderBy, String limit) throws Exception;
    public T getData(String whereClause, String orderBy, String limit, String offset) throws Exception;
    public void updateData(T row) throws Exception;
    public void deleteRow(T row) throws Exception;
    public void throwIntoCsv() throws Exception;
    public void insertRow(U row) throws Exception;
}
