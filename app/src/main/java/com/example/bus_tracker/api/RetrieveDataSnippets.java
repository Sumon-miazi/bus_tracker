package com.example.bus_tracker.api;

import com.example.bus_tracker.utils.GetAllBuses;
import com.google.firebase.firestore.FirebaseFirestore;

public class RetrieveDataSnippets {
    private FirebaseFirestore db;

    public RetrieveDataSnippets(FirebaseFirestore db) {
        this.db = db;
    }

    @SuppressWarnings(value = "unchecked")
    public void getAllBusDetails(GetAllBuses getAllBuses) {
        db.collection("buses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                })
                .addOnFailureListener(e -> {

                });
    }
}

