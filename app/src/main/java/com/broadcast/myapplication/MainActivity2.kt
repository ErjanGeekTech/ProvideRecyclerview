package com.broadcast.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.broadcast.myapplication.adapter.Item
import com.broadcast.myapplication.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
    }

    private fun setupRV() {
//        binding.recyclerView.dataSource(object : AdapterDataSource {
//            override fun getItemCount(): Int {
//                TODO("Not yet implemented")
//            }
//
//            override fun getItemByPosition(position: Int): Item {
//                TODO("Not yet implemented")
//            }
//
//            override fun getItems(): List<Item> {
//                TODO("Not yet implemented")
//            }
//        })
//
//        binding.recyclerView.update()
    }
}