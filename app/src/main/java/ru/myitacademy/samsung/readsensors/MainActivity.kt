package ru.myitacademy.samsung.readsensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.myitacademy.samsung.readsensors.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),SensorEventListener {
    val noSensor = "датчик отсутствует"

    lateinit var sm: SensorManager
    var tSensor:Sensor? = null //температура
    var lSensor:Sensor? = null //освещённость
    var pSensor:Sensor? = null //давление
    var hSensor:Sensor? = null //влажность
    var rvSensor: Sensor? = null //вращение
    var prSensor: Sensor? = null //приближение
 lateinit var db: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            db = DataBindingUtil.setContentView(this,R.layout.activity_main)
            sm = getSystemService(SENSOR_SERVICE) as SensorManager


       }
    override fun onResume() {
        super.onResume()

        tSensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if(tSensor !=null)
        sm.registerListener(this,tSensor,SensorManager.SENSOR_DELAY_GAME)

        lSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT)
        if(lSensor !=null)
        sm.registerListener(this,lSensor,SensorManager.SENSOR_DELAY_GAME)

        pSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE)
        if(pSensor !=null)
        sm.registerListener(this,pSensor,SensorManager.SENSOR_DELAY_GAME)

        hSensor = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        if(hSensor !=null)
        sm.registerListener(this,hSensor,SensorManager.SENSOR_DELAY_GAME)

        rvSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if(rvSensor !=null)
        sm.registerListener(this,rvSensor, SensorManager.SENSOR_DELAY_FASTEST)

       prSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if(prSensor != null)
        sm.registerListener(this,prSensor,SensorManager.SENSOR_DELAY_FASTEST)
    }
    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {

        //упражнение 4.11.1
        var h = 0f
        var t = 0f
        if (tSensor == null) db.temperature.text = "ТЕМПЕРАТУРА: " + noSensor
        else if (event!!.sensor.type == tSensor!!.type) {
            t = event.values[0]; db.temperature.text = "ТЕМПЕРАТУРА: " + t
        }
        if (lSensor == null) db.light.text = "ОСВЕЩЁННОСТЬ: " + noSensor
        else if (event!!.sensor.type == lSensor!!.type) db.light.text =
            "ОСВЕЩЁННОСТЬ: " + event.values[0]
        if (pSensor == null) db.pressure.text = "АТМОСФЕРНОЕ ДАВЛЕНИЕ: " + noSensor
        else if (event!!.sensor.type == pSensor!!.type) db.pressure.text =
            "АТМОСФЕРНОЕ ДАВЛЕНИЕ: " + event.values[0]
        if (hSensor == null) db.humidity.text = "ОТНОСИТЕЛЬНАЯ ВЛАЖНОСТЬ: " + noSensor
        else if (event!!.sensor.type == hSensor!!.type) {
            h = event.values[0]; db.humidity.text = "ОТНОСИТЕЛЬНАЯ ВЛАЖНОСТЬ: " + h
        }
        var str = ""
        if (tSensor != null && hSensor != null) {
            var dewPoint =
                243.12 * (Math.log(h / 100.0) + 17.62 * t / (243.12 + t)) / (17.62 - Math.log(h / 100.0) - 17.62 * t / (243.12 + t))
           str = "точка росы: " + dewPoint}
            else str = "нет возможности вычислить точку росы"
        (findViewById<TextView>(R.id.dewpoint)).text = str

        //упражнение 4.11.2
        if (event!!.sensor.type == rvSensor!!.type){
            (findViewById<ImageView>(R.id.droid)).setRotation (event.values[2]*10)}

        //упражнение 4.11.3
        if(event!!.sensor.type == prSensor!!.type && event.values[0] < prSensor!!.maximumRange)  {
            val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mp = MediaPlayer.create(applicationContext, alert)
            mp.start()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

  }
