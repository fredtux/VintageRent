package org.models;

public interface LinkModelToDatabase<T extends ModelList> {
    public T getData() throws Exception;
    public T getData(String whereClause) throws Exception;
    public T getData(String whereClause, String orderBy) throws Exception;
    public T getData(String whereClause, String orderBy, String limit) throws Exception;
    public T getData(String whereClause, String orderBy, String limit, String offset) throws Exception;
    public void updateData(T row) throws Exception;

}
