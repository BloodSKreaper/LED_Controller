package de.simon_dankelmann.ledcontroller;

/**
 * Created by dankelmann on 23.03.16.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ColorFragment tab1 = new ColorFragment();
                return tab1;
            case 1:
                PresetsFragment tab2 = new PresetsFragment();
                return tab2;
            case 2:
                EffectsFragment tab3 = new EffectsFragment();
                return tab3;
            default:
                EffectsFragment tabDefault = new EffectsFragment();
                return tabDefault;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
