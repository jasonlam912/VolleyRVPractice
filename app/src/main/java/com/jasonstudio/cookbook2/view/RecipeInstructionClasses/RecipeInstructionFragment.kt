package com.jasonstudio.cookbook2.view.RecipeInstructionClasses
import android.os.Bundle
import android.util.Log
import com.jasonstudio.cookbook2.view.RecipeInstructionClasses.RecipeInstructionFragment
import org.json.JSONArray
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.jasonstudio.cookbook2.R
import org.json.JSONObject
import org.json.JSONException
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.jasonstudio.cookbook2.Network.SpoonacularService
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeInstructionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeInstructionFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var data: JSONArray
    private lateinit var outerRv: RecyclerView
    private lateinit var queue: RequestQueue
    private lateinit var adapter: InstructionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        val recipeId = bundle!!.getString("recipe_id")
        Log.d("RecipeInFt_recipeId", recipeId!!)
        loadInstructions(recipeId)
        val manager = LinearLayoutManager(context)
        val view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false)
        outerRv = view.findViewById(R.id.outer_rv)
        outerRv.setLayoutManager(manager)
        adapter = InstructionAdapter(requireContext(), data!!)
        outerRv.setAdapter(adapter)


        // Inflate the layout for this fragment
        return view
    }

    private fun loadInstructions(recipe_id: String?) {
        data = JSONArray()
        val temp = JSONObject()
        try {
            temp.put("steps", JSONArray())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        data.put(temp)
        lifecycleScope.launch {
            val response = SpoonacularService.getInstance().getInstructions(recipe_id!!)
            response.body()?.let {
                val jsonStr = Gson().toJson(it)
                val jsonArray = JSONArray(jsonStr)
                data = jsonArray
                adapter.modifyData(data)
            }
        }
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
         * @return A new instance of fragment RecipeInstructionFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): RecipeInstructionFragment {
            val fragment = RecipeInstructionFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}