package com.jasonstudio.cookbook2.view.IngredientClasses

import android.content.res.Configuration
import com.jasonstudio.cookbook2.Network.NetworkManager.Companion.getInstance
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.jasonstudio.cookbook2.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jasonstudio.cookbook2.view.IngredientClasses.IngredientFragement
import com.jasonstudio.cookbook2.view.IngredientClasses.IngredientAdapter
import com.android.volley.RequestQueue
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.jasonstudio.cookbook2.Network.NetworkManager
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.VolleyError
import com.jasonstudio.cookbook2.Network.SpoonacularService
import com.jasonstudio.cookbook2.model.IngredientsResponse
import kotlinx.coroutines.launch
import kotlin.Throws
import org.json.JSONArray
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [IngredientFragement.newInstance] factory method to
 * create an instance of this fragment.
 */
class IngredientFragement : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var recipe_id: String? = null
    var adapter: IngredientAdapter? = null
    lateinit var root: View
    lateinit var rv: RecyclerView
    lateinit var queue: RequestQueue
    lateinit var recipeData: HashMap<String, MutableList<String>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipe_id = requireArguments().getString("recipe_id")
        recipeData = HashMap()
        recipeData["ingredient_image_url"] = ArrayList()
        recipeData["ingredient_title"] = ArrayList()
        recipeData["ingredient_amount"] = ArrayList()
        root = inflater.inflate(R.layout.fragment_ingredient_fragement, container, false)
        rv = root.findViewById(R.id.ingredient_rv)
        val testData = HashMap<String, List<String>>()
        testData["ingredient_image_url"] = ArrayList(
            Arrays.asList(
                "https://spoonacular.com/cdn/ingredients_100x100/red-pepper-flakes.jpg",
                "https://spoonacular.com/cdn/ingredients_100x100/red-pepper-flakes.jpg",
                "https://spoonacular.com/cdn/ingredients_100x100/red-pepper-flakes.jpg"
            )
        )
        testData["ingredient_title"] = ArrayList(
            Arrays.asList(
                "title1",
                "title2",
                "title3"
            )
        )
        testData["ingredient_amount"] = ArrayList(
            Arrays.asList(
                "amount1",
                "amoutn2",
                "amount3"
            )
        )
        adapter = IngredientAdapter(context, recipeData, recipe_id)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        } else {
            rv.layoutManager = GridLayoutManager(context, 2)
        }
        rv.adapter = adapter
        queue = getInstance(requireContext()).getRequestQueue()!!
        loadRecipeDetails()
        return root
    }

    fun loadRecipeDetails() {
        lifecycleScope.launch {
            val response = SpoonacularService.getInstance().getIngredients(
                recipe_id!!
            )
            response.body()?.let {
                readIngredient(it)
                adapter!!.ModifyData(recipeData)
            }
        }
    }

    @Throws(JSONException::class)
    private fun readIngredient(response: IngredientsResponse) {
        for (ingredient in response.extendedIngredients) {
            recipeData["ingredient_image_url"]!!.add(
                "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}"
            )
            recipeData["ingredient_title"]!!.add(ingredient.name)
            val igdAmountUnitTemp = ingredient.measures.us.unitLong + " "
            val igdAmountUnit = if (igdAmountUnitTemp.trim { it <= ' ' } == "") "unit" else igdAmountUnitTemp
            val igdAmount = convertDecimalToFraction(ingredient.amount) + " " + igdAmountUnit
            recipeData["ingredient_amount"]!!.add(igdAmount)
        }
        Log.d("recipeData", recipeData.toString())
    }

    private val isPortrait: Boolean
        get() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

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
         * @return A new instance of fragment IngredientFragement.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): IngredientFragement {
            val fragment = IngredientFragement()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        private fun convertDecimalToFraction(x: Double): String {
            if (x < 0) {
                return "-" + convertDecimalToFraction(-x)
            }
            val tolerance = 1.0E-6
            var h1 = 1.0
            var h2 = 0.0
            var k1 = 0.0
            var k2 = 1.0
            var b = x
            do {
                val a = Math.floor(b)
                var aux = h1
                h1 = a * h1 + h2
                h2 = aux
                aux = k1
                k1 = a * k1 + k2
                k2 = aux
                b = 1 / (b - a)
            } while (Math.abs(x - h1 / k1) > x * tolerance)
            val h1result = h1.toInt()
            val k1result = k1.toInt()
            return if (k1result == 1) Integer.toString(h1result) else "$h1result/$k1result"
        }
    }
}