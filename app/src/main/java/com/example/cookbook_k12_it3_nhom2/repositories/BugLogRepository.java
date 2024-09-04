package com.example.cookbook_k12_it3_nhom2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook_k12_it3_nhom2.models.BugLog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class BugLogRepository {
    public static void logErrorToDatabase(Exception e, String contextInfo) {
        BugLog bugLog = new BugLog();
        bugLog.setTimestamp(new Date());
        bugLog.setMessage(e.getMessage());
        bugLog.setStackTrace(Log.getStackTraceString(e));
        bugLog.setContextInfo(contextInfo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Lưu log vào Firestore
        db.collection("bugLogs")
                .add(bugLog)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("BugLog", "Bug logged with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BugLog", "Failed to log bug", e);
                    }
                });
    }
}
