package cn.kt.android

import android.view.Menu
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.kt.android.databinding.ActivityMainBinding
import com.android.common.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun init() {
        setSupportActionBar(mBind.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = mBind.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_project,
                R.id.nav_navigation
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        mBind.navView.setupWithNavController(navController)
        mBind.bottomNavView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun getLayoutID(): Int = R.layout.activity_main
}