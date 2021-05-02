package com.onurcankoroglu.samples.viewbinding

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.onurcankoroglu.samples.viewbinding.databinding.ActivityMainBinding
import java.io.*
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private fun loadNames() {
        try {
            val adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayList())

            openFileInput("names.dat").use { it ->
                BufferedReader(InputStreamReader(it, StandardCharsets.UTF_8))
                    .useLines { sequence -> sequence.forEach { adapter.add(it) } }
            }

            mBinding.mainActivityListViewNames.adapter = adapter
        } catch (ignore: FileNotFoundException) {
            Toast.makeText(this, "No data yet!...", Toast.LENGTH_LONG).show()
        } catch (ex: Throwable) {
            Toast.makeText(this, "Internal error occurs!...", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun onAddButtonClicked() {
        try {
            openFileOutput("names.dat", MODE_APPEND).use {
                BufferedWriter(OutputStreamWriter(it, StandardCharsets.UTF_8)).apply {
                    if (mBinding.mainActivityEditTextTextName.text.toString().isNotBlank()) {
                        this.write(mBinding.mainActivityEditTextTextName.text.toString())
                        this.newLine()
                        this.flush()
                    } else {
                        Toast.makeText(this@MainActivity, "Please enter a name.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            loadNames()
        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        mBinding.mainActivityButtonAdd.setOnClickListener { onAddButtonClicked() }
    }

    private fun initBinding() {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    private fun initialize() {
        initBinding()
        initViews()
        loadNames()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

}