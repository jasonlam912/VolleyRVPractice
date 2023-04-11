package com.jasonstudio.cookbook2.RecipeNutritionClasses;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jasonstudio.cookbook2.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NutritionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutritionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NutritionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NutritionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NutritionFragment newInstance(String param1, String param2) {
        NutritionFragment fragment = new NutritionFragment();
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

    private String recipeId;
    private HorizontalBarChart mainChart;
    private HorizontalBarChart otherChart;
    private ArrayList<BarEntry> mainBarEntries;
    private ArrayList<BarEntry> otherBarEntries;
    private ArrayList<String> mainBarLabels;
    private ArrayList<String> otherBarLabels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        mainChart = view.findViewById(R.id.main_nutrition_chart);
        otherChart = view.findViewById(R.id.other_nutrition_chart);
        mainChart.clear();
        otherChart.clear();
        chartParameterSetting(otherChart,Color.GREEN);
        chartParameterSetting(mainChart,Color.YELLOW);
        Bundle bundle = getArguments();
        recipeId = bundle.getString("recipe_id");
        loadNutritionData(recipeId);

        // Inflate the layout for this fragment
        return view;
    }

    private void loadNutritionData(String recipeId){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.spoonacular.com/recipes/"+recipeId+"/nutritionWidget.json?apiKey="+getResources().getString(R.string.apiKeyUsing);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setChartData(response);
                    buildMainChart();
                    buildOtherChart();
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
        queue.add(request);
    }

    private void setChartData(JSONObject response) throws JSONException {
        mainBarEntries = new ArrayList<>();
        otherBarEntries = new ArrayList<>();
        mainBarLabels = new ArrayList<>();
        otherBarLabels = new ArrayList<>();
        JSONArray bad = response.getJSONArray("bad");
        for(int i=0; i<bad.length(); i++){
            JSONObject nutritionData = bad.getJSONObject(i);
            mainBarEntries.add(new BarEntry((float)i,(float)nutritionData.getDouble("percentOfDailyNeeds"),nutritionData.getString("amount")));
            mainBarLabels.add(nutritionData.getString("title"));
        }
        JSONArray good = response.getJSONArray("good");
        for(int i=0;i<good.length(); i++){
            JSONObject nutritionData = good.getJSONObject(i);
            otherBarEntries.add(new BarEntry((float)i,(float)nutritionData.getDouble("percentOfDailyNeeds"),nutritionData.getString("amount")));
            otherBarLabels.add(nutritionData.getString("title"));
        }
    }

    private void buildMainChart(){
        BarDataSet dataSet = new BarDataSet(mainBarEntries, "Tenses");
        dataSet.setColors(colorSet(mainBarEntries,Color.YELLOW));
        BarData data = new BarData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        data.setBarWidth(0.2f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return (Float.toString(barEntry.getY())+"% ("+(String)barEntry.getData()+")");
            }});
        mainChart.setData(data);
        mainChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mainBarLabels));
        mainChart.getXAxis().setLabelCount(mainBarEntries.size());
        mainChart.getAxisLeft().setAxisMaximum(maxBarLength(mainBarEntries)+40f);
        chartParameterSetting(mainChart,Color.YELLOW);
    }

    private void buildOtherChart(){
        BarDataSet dataSet = new BarDataSet(otherBarEntries, "Tenses");
        dataSet.setColors(colorSet(otherBarEntries,Color.GREEN));
        BarData data = new BarData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);
        data.setBarWidth(0.2f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return (Float.toString(barEntry.getY())+"% ("+(String)barEntry.getData()+")");
            }});
        otherChart.setData(data);
        otherChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(otherBarLabels));
        otherChart.getXAxis().setLabelCount(otherBarEntries.size());
        otherChart.getAxisLeft().setAxisMaximum(maxBarLength(otherBarEntries)+40f);
        chartParameterSetting(otherChart,Color.GREEN);
    }

    private void chartParameterSetting(HorizontalBarChart chart,int color){

        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.animateY( 1000);
        chart.setDrawValueAboveBar(true);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(true);
        ArrayList<LegendEntry> array = new ArrayList<>();
        for(int i =0; i<1; i++){
            array.add(new LegendEntry("% Of Daily Needs", Legend.LegendForm.DEFAULT,8f,8f,null,color));
        }
        chart.getLegend().setTextColor(color);
        chart.getLegend().setCustom(array);
        chart.setViewPortOffsets(200, 60, 120, 60);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.invalidate();
    }

    private int[] colorSet(ArrayList<BarEntry> data, int color){
        float max = maxBarLength(data);

        int[] result = new int[data.size()];
        for(int i=0; i < data.size();i++){
            float value = data.get(i).getY();
            if(color == Color.GREEN){
                result[i] = Color.rgb((int)(160*(0.5*max+value)/(1.5*max)),(int)(255*(0.5*max+value)/(1.5*max)),(int)(50*(0.5*max+value)/(1.5*max)));
            }else if (color == Color.YELLOW){
                result[i] = Color.rgb((int)(255*(0.5*max+value)/(1.5*max)),(int)(215*(0.5*max+value)/(1.5*max)),(int)(30*(0.5*max+value)/(1.5*max)));
            }
        }
        return result;
    }

    private float maxBarLength(ArrayList<BarEntry> data){
        float max = 0;
        for(int i=0; i < data.size();i++){
            max = Math.max(max,data.get(i).getY());
        }
        return  max;
    }
}
