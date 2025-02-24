package com.example.nerdyfactsapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// this file defines the database table SavedFacts

@Entity(tableName = "SavedFacts")
data class SavedFact (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // use zero as default to avoid error when inserting
    @ColumnInfo(name = "fact")
    var fact: String,
    @ColumnInfo(name = "category")
    var category: String
        )


// Interacts with:
// • FactDao (to insert, retrieve, and delete facts)
// • FactDatabase (defines the Room database)
// • FactsRepositoryImplementation (handles data access)
// Used by:
// • FactsRepository (manages fact storage & retrieval)
// • NerdyFactsViewModel (displays facts in UI)