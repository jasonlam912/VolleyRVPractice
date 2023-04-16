package com.jasonstudio.cookbook2.RecipeNutritionClasses

import android.graphics.Color
import android.os.Bundle
import com.jasonstudio.cookbook2.RecipeNutritionClasses.NutritionFragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarEntry
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.jasonstudio.cookbook2.R
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.VolleyError
import com.github.mikephil.charting.components.Legend
import kotlin.Throws
import org.json.JSONArray
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [NutritionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NutritionFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var recipeId: String
    private lateinit var mainChart: HorizontalBarChart
    private lateinit var otherChart: HorizontalBarChart
    private lateinit var mainBarEntries: ArrayList<BarEntry>
    private lateinit var otherBarEntries: ArrayList<BarEntry>
    private lateinit var mainBarLabels: ArrayList<String>
    private lateinit var otherBarLabels: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)
        mainChart = view.findViewById(R.id.main_nutrition_chart)
        otherChart = view.findViewById(R.id.other_nutrition_chart)
        mainChart.clear()
        otherChart.clear()
        chartParameterSetting(otherChart, Color.GREEN)
        chartParameterSetting(mainChart, Color.YELLOW)
        val bundle = arguments
        recipeId = bundle!!.getString("recipe_id") ?: ""
        loadNutritionData(recipeId)

        // Inflate the layout for this fragment
        return view
    }

    private fun loadNutritionData(recipeId: String?) {
        val queue = Volley.newRequestQueue(context)
        val url =
            "https://api.spoonacular.com/recipes/" + recipeId + "/nutritionWidget.json?apiKey=" + resources.getString(
                R.string.apiKeyUsing
            )
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                setChartData(response)
                buildMainChart()
                buildOtherChart()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error -> error.printStackTrace() }
        queue.add(request)
    }

    @Throws(JSONException::class)
    private fun setChartData(response: JSONObject) {
        mainBarEntries = ArrayList()
        otherBarEntries = ArrayList()
        mainBarLabels = ArrayList()
        otherBarLabels = ArrayList()
        val bad = response.getJSONArray("bad")
        for (i in 0 until bad.length()) {
            val nutritionData = bad.getJSONObject(i)
            mainBarEntries!!.add(
                BarEntry(
                    i.toFloat(),
                    nutritionData.getDouble("percentOfDailyNeeds").toFloat(),
                    nutritionData.getString("amount")
                )
            )
            mainBarLabels!!.add(nutritionData.getString("title"))
        }
        val good = response.getJSONArray("good")
        for (i in 0 until good.length()) {
            val nutritionData = good.getJSONObject(i)
            otherBarEntries!!.add(
                BarEntry(
                    i.toFloat(),
                    nutritionData.getDouble("percentOfDailyNeeds").toFloat(),
                    nutritionData.getString("amount")
                )
            )
            otherBarLabels!!.add(nutritionData.getString("title"))
        }
    }

    private fun buildMainChart() {
        val dataSet = BarDataSet(mainBarEntries, "Tenses")
        dataSet.setColors(*colorSet(mainBarEntries, Color.YELLOW))
        val data = BarData(dataSet)
        data.setValueTextSize(9f)
        data.setValueTextColor(Color.WHITE)
        data.barWidth = 0.2f
        data.setValueFormatter(object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry): String {
                return java.lang.Float.toString(barEntry.y) + "% (" + barEntry.data as String + ")"
            }
        })
        mainChart!!.data = data
        mainChart!!.xAxis.valueFormatter = IndexAxisValueFormatter(mainBarLabels)
        mainChart!!.xAxis.labelCount = mainBarEntries!!.size
        mainChart!!.axisLeft.axisMaximum = maxBarLength(mainBarEntries) + 40f
        chartParameterSetting(mainChart, Color.YELLOW)
    }

    private fun buildOtherChart() {
        val dataSet = BarDataSet(otherBarEntries, "Tenses")
        dataSet.setColors(*colorSet(otherBarEntries, Color.GREEN))
        val data = BarData(dataSet)
        data.setValueTextSize(9f)
        data.setValueTextColor(Color.WHITE)
        data.barWidth = 0.2f
        data.setValueFormatter(object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry): String {
                return java.lang.Float.toString(barEntry.y) + "% (" + barEntry.data as String + ")"
            }
        })
        otherChart!!.data = data
        otherChart!!.xAxis.valueFormatter = IndexAxisValueFormatter(otherBarLabels)
        otherChart!!.xAxis.labelCount = otherBarEntries!!.size
        otherChart!!.axisLeft.axisMaximum = maxBarLength(otherBarEntries) + 40f
        chartParameterSetting(otherChart, Color.GREEN)
    }

    private fun chartParameterSetting(chart: HorizontalBarChart?, color: Int) {
        chart!!.xAxis.textColor = Color.WHITE
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawGridLines(false)
        chart.animateY(1000)
        chart.setDrawValueAboveBar(true)
        chart.axisLeft.textColor = Color.WHITE
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.legend.isEnabled = true
        val array = ArrayList<LegendEntry>()
        for (i in 0..0) {
            array.add(
                LegendEntry(
                    "% Of Daily Needs",
                    Legend.LegendForm.DEFAULT,
                    8f,
                    8f,
                    null,
                    color
                )
            )
        }
        chart.legend.textColor = color
        chart.legend.setCustom(array)
        chart.setViewPortOffsets(200f, 60f, 120f, 60f)
        chart.axisLeft.axisMinimum = 0f
        chart.invalidate()
    }

    private fun colorSet(data: ArrayList<BarEntry>?, color: Int): IntArray {
        val max = maxBarLength(data)
        val result = IntArray(data!!.size)
        for (i in data.indices) {
            val value = data[i].y
            if (color == Color.GREEN) {
                result[i] = Color.rgb(
                    (160 * (0.5 * max + value) / (1.5 * max)).toInt(),
                    (255 * (0.5 * max + value) / (1.5 * max)).toInt(),
                    (50 * (0.5 * max + value) / (1.5 * max)).toInt()
                )
            } else if (color == Color.YELLOW) {
                result[i] = Color.rgb(
                    (255 * (0.5 * max + value) / (1.5 * max)).toInt(),
                    (215 * (0.5 * max + value) / (1.5 * max)).toInt(),
                    (30 * (0.5 * max + value) / (1.5 * max)).toInt()
                )
            }
        }
        return result
    }

    private fun maxBarLength(data: ArrayList<BarEntry>?): Float {
        var max = 0f
        for (i in data!!.indices) {
            max = Math.max(max, data[i].y)
        }
        return max
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NutritionFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): NutritionFragment {
            val fragment = NutritionFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}