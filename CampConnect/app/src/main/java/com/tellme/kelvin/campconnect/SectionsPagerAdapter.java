package com.tellme.kelvin.campconnect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                CampFragment campFragment = new CampFragment();
                return campFragment;

            case 1:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;

            case 2:
                ChatsFragment chatsFragment = new ChatsFragment();
                return  chatsFragment;

            case 3:
                FriendsFragment friendsFragment = new FriendsFragment();
                return  friendsFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "";

            case 1:
                return "";

            case 2:
                return "";

            case 3:
                return "";

            default:
                return null;
        }

    }

    public int getIcon(int position) {
        switch(position) {
            case 0:
                return R.drawable.a5;
            case 1:
                return R.drawable.a2;
            case 2:
                return R.drawable.a4;
            case 3:
                return R.drawable.a1;

        }

        return R.drawable.a5;
    }

}
