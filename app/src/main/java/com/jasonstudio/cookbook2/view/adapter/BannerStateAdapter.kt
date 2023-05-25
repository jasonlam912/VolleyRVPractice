package com.jasonstudio.cookbook2.view.adapter

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.jasonstudio.cookbook2.BaseFragment
//import com.jasonstudio.cookbook2.databinding.RowBannerBinding
//
//class BannerStateAdapter(ft: FragmentActivity): FragmentStateAdapter(ft) {
//    private val data = listOf(
//        "https://kinsta.com/wp-content/uploads/2022/03/what-is-express-js-1.jpg",
//        "https://contentstatic.techgig.com/thumb/msid-77087595,width-2000,resizemode-4/Guide-How-to-build-career-as-a-programmer-without-college-degree.jpg?336360",
//        "https://static.vecteezy.com/system/resources/previews/015/642/935/original/website-programming-and-coding-web-development-and-coding-3d-gradient-isometric-illustrations-suitable-for-ui-ux-web-mobile-banner-and-infographic-vector.jpg"
//    )
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        val bf = BannerFragment()
//        bf.url = data[position]
//        return bf
//    }
//}
//
//class BannerFragment: BaseFragment<RowBannerBinding>(RowBannerBinding::inflate) {
//    var url: String = ""
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = super.onCreateView(inflater, container, savedInstanceState)
//        binding.url = url
//        return view
//    }
//}