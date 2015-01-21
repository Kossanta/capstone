package org.coursera.symptommanager;

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
