/*
* Copyright (C) 2013 linuxonandroid.org
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.zpwebsites.linuxonandroid.opensource;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

public class Install_Arch extends BaseActivity {

	TabHost nTabHost;
    ViewPager  nViewPager;
    TabsAdapter nTabsAdapter;
    Fragment nFragment1;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	mTitleRes = R.string.archlinux_guide;
        super.onCreate(savedInstanceState);
        setSlidingActionBarEnabled(false);
        
        setContentView(R.layout.fragment_tabs_pager);
                
        nTabHost = (TabHost)findViewById(android.R.id.tabhost);
        nTabHost.setup();

        nViewPager = (ViewPager)findViewById(R.id.pager);

        nTabsAdapter = new TabsAdapter(this, nTabHost, nViewPager);

        nTabsAdapter.addTab(nTabHost.newTabSpec("archlinux1").setIndicator("Page 1"),
                Install_Archlinux_1.class, null);
        nTabsAdapter.addTab(nTabHost.newTabSpec("archlinux2").setIndicator("Page 2"),
        		Install_Archlinux_2.class, null);
        nTabsAdapter.addTab(nTabHost.newTabSpec("archlinux3").setIndicator("Page 3"),
        		Install_Archlinux_3.class, null);
        nTabsAdapter.addTab(nTabHost.newTabSpec("archlinux4").setIndicator("Page 4"),
        		Install_Archlinux_4.class, null);

        if (savedInstanceState != null) {
            nTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", nTabHost.getCurrentTabTag());
    }
   
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context nContext;
        private final TabHost nTabHost;
        private final ViewPager nViewPager;
        private final ArrayList<TabInfo> nTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            @SuppressWarnings("unused")
			private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context nContext;

            public DummyTabFactory(Context context) {
                nContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(nContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            nContext = activity;
            nTabHost = tabHost;
            nViewPager = pager;
            nTabHost.setOnTabChangedListener(this);
            nViewPager.setAdapter(this);
            nViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(nContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            nTabs.add(info);
            nTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return nTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = nTabs.get(position);
            return Fragment.instantiate(nContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = nTabHost.getCurrentTab();
            nViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = nTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            nTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
        
    }
    
}
