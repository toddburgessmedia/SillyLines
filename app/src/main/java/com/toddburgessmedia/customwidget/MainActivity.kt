package com.toddburgessmedia.customwidget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.menu_reset) {
            customView.clearLines()
        } else if (item?.itemId == R.id.menu_animate) {
            customView.animateColours()
        } else if (item?.itemId == R.id.menu_linetype) {
            if (item?.title?.equals("Lines") == true) {
                customView.setLineType(MyCustomView.CIRCLE)
                item?.icon = getDrawable(R.drawable.ic_lines)
                item?.title = "Circles"
            } else {
                customView.setLineType(MyCustomView.LINE)
                item?.icon = getDrawable(R.drawable.ic_circle)
                item?.title = "Lines"
            }

        }


        return true
    }
}
