package br.com.uol.pslibs.lojinhapagseguro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.fragments.AboutFragment;
import br.com.uol.pslibs.lojinhapagseguro.view.fragments.ProductDetailFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_READ_PHONE_STATE = 0x10;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.menu_home) LinearLayout menuHome;
    @BindView(R.id.menu_about) LinearLayout menuAbout;
    @BindView(R.id.menu_close) LinearLayout menuClose;

    private ActionBarDrawerToggle drawerToggle;
    private boolean isHome;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginha_activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupDrawerContent();

        callHomeScreen();

        new Handler().postDelayed(() -> {
            RelativeLayout splash = (RelativeLayout)findViewById(R.id.splash);
            splash.animate().alphaBy(1).alpha(0).setDuration(500);
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PSCheckout.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_READ_PHONE_STATE){
            PSCheckout.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (PSCheckout.onBackPressed(this)) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isHome) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PSCheckout.onDestroy();
    }

    public void setupDrawerContent() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        menuHome.setOnClickListener(view -> callHomeScreen());
        menuAbout.setOnClickListener(view -> callAboutScreen());
        menuClose.setOnClickListener(view -> logoff());
    }

    private void callHomeScreen() {
        menuHome.setBackgroundColor(ContextCompat.getColor(this, R.color.menu_selected));
        menuAbout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        menuClose.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ProductDetailFragment)){
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fragment_container, new ProductDetailFragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void callAboutScreen() {
        menuHome.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        menuClose.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        menuAbout.setBackgroundColor(ContextCompat.getColor(this, R.color.menu_selected));

        if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AboutFragment)){
            FragmentUtils.replaceFragmentWithBackStack(getSupportFragmentManager(), R.id.fragment_container, new AboutFragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logoff() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.setTitle("Lojinha")
                .setMessage("Deseja fazer logout da sua conta PagSeguro?")
                .setPositiveButton("Sim", (dialogInterface, i) -> {
                    PSCheckout.logout(this);
                    callHomeScreen();
                    drawerLayout.closeDrawer(GravityCompat.START);
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {alertDialog.cancel(); drawerLayout.closeDrawer(GravityCompat.START);})
                .create();
        alertDialog.show();
    }

    public void disableMenu() {
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isHome = false;
    }

    public void enableMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isHome = true;
    }
}
