package com.testfragment.vladdolgusin.datumtesttask.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.testfragment.vladdolgusin.datumtesttask.R
import com.testfragment.vladdolgusin.datumtesttask.app.fragment.list.ListFragment
import com.testfragment.vladdolgusin.datumtesttask.app.fragment.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*






class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var firstFragment: Fragment
    private lateinit var secondFragment: Fragment
    private var itemMenu = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation_view.setOnNavigationItemSelectedListener(this)
        mFragmentManager = supportFragmentManager

        if (savedInstanceState == null){
            firstFragment = MapFragment.newInstance(null, null)
            secondFragment = ListFragment.newInstance(null, null)
        }else{
            if ( mFragmentManager.findFragmentByTag("map") != null) {
                firstFragment =  mFragmentManager.findFragmentByTag("map") as Fragment
            } else{
                firstFragment = MapFragment.newInstance(null, null)
            }
            if ( mFragmentManager.findFragmentByTag("list") != null) {
                secondFragment =  mFragmentManager.findFragmentByTag("list") as Fragment
            } else {
                secondFragment = ListFragment.newInstance(null, null)
            }
            itemMenu = savedInstanceState.getInt("count")
        }

        when (itemMenu) {

            R.id.navigation_home -> {
                loadFragment(firstFragment, "map")
                true
            }
            R.id.navigation_dashboard -> {
                loadFragment(secondFragment, "list")
                true
            }
            else -> {
                loadFragment(firstFragment, "map")
            }
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        itemMenu = p0.itemId
        Log.i("MAP", "$itemMenu")
        return when(p0.itemId) {
            R.id.navigation_home -> {
                loadFragment(firstFragment, "map")
                true
            }
            R.id.navigation_dashboard -> {
                loadFragment(secondFragment, "list")
                true
            }
            else -> {
                false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        changeVisability()
        if (mFragmentManager.findFragmentByTag(tag) != null) {
            mFragmentManager.beginTransaction().show(fragment).commit()
        } else {
            mFragmentManager.beginTransaction().add(R.id.fl_content,fragment, tag).addToBackStack(tag).commit()
        }
    }

    private fun changeVisability() {
        val listFragments = mFragmentManager.fragments
        listFragments.forEach {
            mFragmentManager.beginTransaction().hide(it).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mFragmentManager.backStackEntryCount > 0) {
            bottom_navigation_view.selectedItemId = bottom_navigation_view.menu[mFragmentManager.backStackEntryCount - 1] .itemId
            mFragmentManager.beginTransaction().show(mFragmentManager.fragments.last()).commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", itemMenu)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        itemMenu = savedInstanceState.getInt("count")

    }

}
