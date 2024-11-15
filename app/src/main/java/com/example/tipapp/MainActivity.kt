package com.example.tipapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=15
class MainActivity : AppCompatActivity() {
    private lateinit var etbaseamount: EditText
    private lateinit var seekbarTip: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvtipamt: TextView
    private lateinit var tvtotalamt: TextView
    private lateinit var tvTipDescription:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        etbaseamount=findViewById(R.id.etbaseamount)
        seekbarTip=findViewById(R.id.seekbarTip)
        tvTipPercent=findViewById(R.id.tvTipPercent)
        tvtipamt=findViewById(R.id.tvtipamt)
        tvtotalamt=findViewById(R.id.tvtotalamt)
        tvTipDescription=findViewById(R.id.tvTipDescription)


        seekbarTip.progress= INITIAL_TIP_PERCENT
        tvTipPercent.text="$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekbarTip.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG,"onProgressChanged $progress")
                tvTipPercent.text="$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        etbaseamount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("RestrictedApi")
    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription=when (tipPercent){
            in 0..9-> "Poor"
            in 10..14->"Acceptable"
            in 15..19->"Good"
            in 20..24->"Great"
            else->"Amazing"
        }
        tvTipDescription.text=tipDescription
        val color=ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekbarTip.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)

        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etbaseamount.text.isEmpty()){
            tvtipamt.text=" "
            tvtotalamt.text=" "
            return
        }
        val baseAmount=etbaseamount.text.toString().toDouble()
        val tipPercent=seekbarTip.progress

        val tipAmount=baseAmount*tipPercent/100
        val totalAmount=baseAmount+tipAmount

        tvtipamt.text="%.2f".format(tipAmount)
        tvtotalamt.text="%.2f".format(totalAmount)
    }
}