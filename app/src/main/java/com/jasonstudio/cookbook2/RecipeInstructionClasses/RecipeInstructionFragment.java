package com.jasonstudio.cookbook2.RecipeInstructionClasses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jasonstudio.cookbook2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInstructionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeInstructionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeInstructionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeInstructionFragment newInstance(String param1, String param2) {
        RecipeInstructionFragment fragment = new RecipeInstructionFragment();
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

    private JSONArray data;
    private RecyclerView outerRv;
    private RequestQueue queue;
    private InstructionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String recipeId = bundle.getString("recipe_id");
        Log.d("RecipeInFt_recipeId", recipeId);
        loadInstructions(recipeId);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        outerRv = view.findViewById(R.id.outer_rv);
        outerRv.setLayoutManager(manager);
        adapter = new InstructionAdapter(getContext(),data);
        outerRv.setAdapter(adapter);


        // Inflate the layout for this fragment
        return view;
    }

    private void loadInstructions(String recipe_id){
        data = new JSONArray();
        JSONObject temp = new JSONObject();
        try {
            temp.put("steps",new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data.put(temp);

        String url = "https://api.spoonacular.com/recipes/"+recipe_id+"/analyzedInstructions?apiKey="+getResources().getString(R.string.apiKeyUsing);
        queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                data = response;
                adapter.modifyData(data);
                //Log.d("StepsJSON", data.toString());
                //Log.d("StepsJSONInAdapt",adapter.data.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
