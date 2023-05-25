package com.jasonstudio.cookbook2.FavouriteRecipeModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Observable
import kotlinx.coroutines.launch

class FavouriteRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavouriteRecipeRepository
    val all: Observable<List<FavouriteRecipe>>

    init {
        repository = FavouriteRecipeRepository(application)
        all = repository.allFR
    }

    fun insert(recipe: FavouriteRecipe?) {
        viewModelScope.launch {
            repository.insert(recipe)
        }
    }

    fun delete(recipe: FavouriteRecipe?) {
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAllFR()
        }
    }

    fun deleteWithKey(key: Int) {
        viewModelScope.launch {
            repository.deleteWithKey(key)
        }
    }
}