package io.rajivrnair.barcodegenerator

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.BarcodeFormat.*
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val barcodeFormatSamples = hashMapOf(
        AZTEC to UUID.randomUUID().toString(),
        CODABAR to "31117031206375",
        CODE_39 to UUID.randomUUID().toString().substring(0, 10).toUpperCase(),
        CODE_93 to UUID.randomUUID().toString().substring(0, 12).toUpperCase(),
        CODE_128 to UUID.randomUUID().toString().substring(0, 16).toUpperCase(),
        DATA_MATRIX to "This is the matrix",
        EAN_8 to "96385074",
        EAN_13 to "4003994155486",
        ITF to "0061414100",
        PDF_417 to UUID.randomUUID().toString(),
        QR_CODE to UUID.randomUUID().toString(),
        UPC_A to "12345678901",
        UPC_E to "12345678"
    )

    val dimensions200x200 = listOf(AZTEC, DATA_MATRIX, PDF_417, QR_CODE)
    private val ignored = listOf(RSS_14, RSS_EXPANDED, MAXICODE, UPC_EAN_EXTENSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filteredBarCodes = enumValues<BarcodeFormat>()
                .asList()
                .filter { !ignored.contains(BarcodeFormat.valueOf(it.name))}

        spinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, filteredBarCodes)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = spinner.selectedItem
                Toast.makeText(this@MainActivity, "You have Selected " + selectedItem.toString(), Toast.LENGTH_SHORT).show()

                if(selectedItem is BarcodeFormat) {
                    val valueToEncode = barcodeFormatSamples[selectedItem]
                    uuid.text = valueToEncode

                    try {
                        val bitMatrix = MultiFormatWriter().encode(valueToEncode, selectedItem , 200, 200)
                        val bitmap = BarcodeEncoder().createBitmap(bitMatrix)

                        val height = if(dimensions200x200.contains(selectedItem)) 200 else 100

                        image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200,height,false))
                    } catch (e: WriterException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
