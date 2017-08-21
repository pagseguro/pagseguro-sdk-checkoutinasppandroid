package br.com.uol.pslibs.lojinhapagseguro.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import br.com.uol.pslibs.lojinhapagseguro.R;

public class FragmentUtils {

    public static void addFragment(FragmentManager fragmentManager, int container, Fragment newFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, newFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentWithBackStack(FragmentManager fragmentManager, int container, Fragment newFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentTransaction.replace(container, newFragment).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment newFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, newFragment).commitAllowingStateLoss();
    }

    public static void clearBackStack(FragmentManager fragmentManager){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
