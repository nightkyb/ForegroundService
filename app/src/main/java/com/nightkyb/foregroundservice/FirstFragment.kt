package com.nightkyb.foregroundservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nightkyb.util.SpUtils
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            lifecycleScope.launch {
                dfdf()
            }
        }

        view.findViewById<Button>(R.id.button_send).setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            lifecycleScope.launch(Dispatchers.IO) {
                Logger.d("IO")

                withContext(Dispatchers.Default) {
                    Logger.d("Default")
                }
            }
        }

        val list1 = listOf("1", "222")
        val list2 = listOf("1", "222")

        println("list1 == list2：${list1 == list2}")

        SpUtils.getInstance().put("test", "测试sp")
        Logger.d("可以打印日志了：${SpUtils.getInstance().getString("test")}")
    }

    suspend fun dfdf() {
        withContext(Dispatchers.IO) {
        }
    }
}