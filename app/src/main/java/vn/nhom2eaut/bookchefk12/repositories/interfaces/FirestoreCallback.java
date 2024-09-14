package vn.nhom2eaut.bookchefk12.repositories.interfaces;

public interface FirestoreCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}
