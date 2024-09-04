package com.example.cookbook_k12_it3_nhom2.repositories.interfaces;

public interface FirestoreCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}
