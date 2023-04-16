package com.jasonstudio.cookbook2.Recipe

import android.content.Context
import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe.jsonObject2RandomRecipe
import com.jasonstudio.cookbook2.Recipe.JsonData2Recipe.jsonObject2SearchRecipe
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.Glide
import com.jasonstudio.cookbook2.Network.NetworkManager
import com.jasonstudio.cookbook2.Network.CallbackListener
import com.jasonstudio.cookbook2.Network.GlideCallbackListener
import org.json.JSONObject
import com.jasonstudio.cookbook2.Network.GlideManager
import java.util.ArrayList

class RecipeViewModel : ViewModel() {
    val data: MutableLiveData<MutableList<RecipeModel>>
    private val foodImageRequestList: MutableList<CustomTarget<Bitmap>>
    var isSearch: Boolean
    var isLoading: MutableLiveData<Boolean>

    init {
        data = MutableLiveData()
        data.value = ArrayList()
        isSearch = false
        isLoading = MutableLiveData()
        isLoading.value = false
        foodImageRequestList = ArrayList()
        addPaddingItem()
        addPaddingItem()
    }

    fun resetData(ct: Context) {
        data.value = ArrayList()
        for (request in foodImageRequestList) {
            Glide.with(ct.applicationContext).clear(request)
        }
        Log.d("imagerequestListNo.:", Integer.toString(foodImageRequestList.size))
        foodImageRequestList.clear()
        addPaddingItem()
        addPaddingItem()
    }

    fun putRandomRecipe(ct: Context) {
        Log.d("size", Integer.toString(data.value!!.size))
        isLoading.value = true
        if (isSearch) {
            resetData(ct)
            isSearch = !isSearch
        }
        NetworkManager.getInstance(ct).getRandomRecipe (object : CallbackListener {
            override fun getResult(jsonObject: JSONObject) {
                deleteLastItem()
                val list = data.value!!
                //Log.d("isNull", Boolean.toString(allFRList.getValue()==null));
                val newList = jsonObject2RandomRecipe(jsonObject, ArrayList())
                list.addAll(newList)
                loadFoodImage(list, ct)
                data.value = list
                if (newList.size == 0) {
                    addEndItem()
                } else {
                    addLoadingItem()
                }
                isLoading.value = false
            }
        })
    }

    fun putSearchRecipe(ct: Context, query: String?) {
        isLoading.value = true
        if (!isSearch) {
            resetData(ct)
            isSearch = !isSearch
        }
        NetworkManager.getInstance(ct).getSearchRecipe(query!!, data.value!!.size - 2, object : CallbackListener {
            override fun getResult(jsonObject: JSONObject) {
                deleteLastItem()
                val list = data.value!!
                val newList = jsonObject2SearchRecipe(jsonObject, ArrayList())
                list.addAll(newList)
                loadFoodImage(list, ct)
                data.value = list
                if (newList.isEmpty()) {
                    addEndItem()
                } else {
                    addLoadingItem()
                }
                isLoading.value = false
            }
        })
    }

    fun putRecipe(ct: Context, query: String?) {
        if (isSearch) {
            putSearchRecipe(ct, query)
        } else {
            putRandomRecipe(ct)
        }
    }

    fun addPaddingItem() {
        val recipe = RecipeModel(null, null, null, 3, true, null)
        data.value!!.add(recipe)
    }

    fun deleteLastItem() {
        val list = data.value!!
        list.removeAt(list.size - 1)
        data.value = list
    }

    fun addLoadingItem() {
        val list = data.value!!
        list.add(RecipeModel(null, null, null, 1, true, null))
        data.value = list
    }

    fun addEndItem() {
        val list = data.value!!
        list.add(RecipeModel(null, null, null, 2, true, null))
        data.value = list
    }

    fun loadFoodImage(list: List<RecipeModel>, ct: Context) {
        for (i in list.indices) {
            //Log.d("boolean geresult", Boolean.toString(list.get(i).getStatusForDisplay().equals(0)&&list.get(i).getRecipeIamge()==null));
            if (list[i].statusForDisplay == 0 && list[i].recipeIamge == null) {
                val finalI = i
                //Log.d("url",list.get(i).getRecipeImageUrl() );
                val request = GlideManager.loadImage(
                    ct.applicationContext,
                    list[i].recipeImageUrl,
                    i,
                    object : GlideCallbackListener {
                        override fun getBitmap(resource: Bitmap, index: Int) {
                            //Log.d("getBitmap", Boolean.toString(resource==null));
                            val tempList = data.value!!
                            val temp = resource.copy(resource.config, true)
                            tempList[index].setRecipeImage(temp)
                            data.postValue(tempList)
                            //Glide.with(ct.getApplicationContext()).clear(request);
                        }

                    }
                )
                foodImageRequestList.add(request)
                //list.get(i).setRecipeImage(Glide.with(ct).asBitmap().load(list.get(i).getRecipeImageUrl()).submit(556,370).get());
            }
        }
    }
}