package com.example.volleyrvpractice.IngredientClasses;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleyrvpractice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragement extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IngredientFragement() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientFragement newInstance(String param1, String param2) {
        IngredientFragement fragment = new IngredientFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private String recipe_id;
    public IngredientAdapter adapter;
    private View view;
    private RecyclerView rv;
    private RequestQueue queue;
    private HashMap<String, List<String>> recipeData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recipe_id = getArguments().getString("recipe_id");
        //Log.d("IgdFragIgdData", ingredientData.toString());
        //Log.d("recipeID", recipe_id);
        recipeData = new HashMap<>();
        recipeData.put("ingredient_image_url", new ArrayList<String>());
        recipeData.put("ingredient_title", new ArrayList<String>());
        recipeData.put("ingredient_amount", new ArrayList<String>());

        view = inflater.inflate(R.layout.fragment_ingredient_fragement, container, false);
        rv = view.findViewById(R.id.ingredient_rv);

        HashMap<String,List<String>> testData = new HashMap<>();
        testData.put("ingredient_image_url", new ArrayList<String>(Arrays.asList("https://spoonacular.com/cdn/ingredients_100x100/red-pepper-flakes.jpg"
        ,"https://spoonacular.com/cdn/ingredients_100x100/red-pepper-flakes.jpg","https://spoonacular.com/cdn/ingredients_100x100/red-pepper-flakes.jpg")));
        testData.put("ingredient_title", new ArrayList<String>(Arrays.asList("title1","title2","title3")));
        testData.put("ingredient_amount", new ArrayList<String>(Arrays.asList("amount1","amoutn2","amount3")));

        adapter = new IngredientAdapter(getContext(), recipeData, recipe_id);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }else{
            rv.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        rv.setAdapter(adapter);
        queue = Volley.newRequestQueue(getContext());
        loadRecipeDetails();

        return view;
    }

    public void loadRecipeDetails(){
        String url = "https://api.spoonacular.com/recipes/"+recipe_id+"/information?includeNutrition=false&apiKey="+getResources().getString(R.string.apiKeyUsing);
        Log.d("url", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            readIngredient(response);
                            adapter.ModifyData(recipeData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }


    private void readIngredient(JSONObject response) throws JSONException {

        JSONArray array = response.getJSONArray("extendedIngredients");
        for(int i=0;i<array.length();i++){
            JSONObject ingredient = (JSONObject) array.get(i);
            String igdImageUrl = "https://spoonacular.com/cdn/ingredients_100x100/"+ingredient.getString("image");
            recipeData.get("ingredient_image_url").add(igdImageUrl);
            String igdTitle = ingredient.getString("name");
            recipeData.get("ingredient_title").add(igdTitle);
            String igdAmountUnitTemp =  ingredient.getJSONObject("measures").getJSONObject("us").getString("unitLong")+" ";
            String igdAmountUnit = ((igdAmountUnitTemp.trim().equals(""))?"unit":igdAmountUnitTemp);
            String igdAmount = convertDecimalToFraction(ingredient.getDouble("amount")) + " "+igdAmountUnit;
            recipeData.get("ingredient_amount").add(igdAmount);
        }
        Log.d("recipeData",recipeData.toString());
    }

    static private String convertDecimalToFraction(double x){
        if (x < 0){
            return "-" + convertDecimalToFraction(-x);
        }
        double tolerance = 1.0E-6;
        double h1=1; double h2=0;
        double k1=0; double k2=1;
        double b = x;
        do {
            double a = Math.floor(b);
            double aux = h1; h1 = a*h1+h2; h2 = aux;
            aux = k1; k1 = a*k1+k2; k2 = aux;
            b = 1/(b-a);
        } while (Math.abs(x-h1/k1) > x*tolerance);

        int h1result = (int) h1;
        int k1result = (int) k1;
        String result=((k1result==1)?Integer.toString(h1result):(h1result+"/"+k1result));
        return result;
    }

    private boolean isPortrait(){
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            return true;
        }else{
            return false;
        }
    }


}
