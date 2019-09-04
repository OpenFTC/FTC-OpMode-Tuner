package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.activity.UdpConnectionActivity;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitWriteDatagram;
import net.frogbots.ftcopmodetunercommon.networking.udp.CommandList;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitDataHandler;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitReadDataMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitWriteDataMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkCommand;

public class LynxModuleControlActivity extends UdpConnectionActivity implements HubToolkitDataHandler
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private LynxModuleMotorControlFragment motorControlFragment;
    private LynxModuleMotorControlFragment servoControlFragment;
    private LynxModuleDigitalControlFragment digitalControlFragment;
    private LynxModuleAnalogControlFragment analogControlFragment;
    private LynxModuleMonitorsFragment monitorsFragment;
    private LynxModuleExtraFragment extraFragment;

    private HubToolkitReadDatagram hubToolkitReadDatagram = new HubToolkitReadDatagram();
    private HubToolkitWriteDatagram hubToolkitWriteDatagram = new HubToolkitWriteDatagram();

    private WriteLooper writeLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_module_control);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.hub_toolkit_actionbar_color));
        toolbar.setTitle(getIntent().getStringExtra("module"));
        setSupportActionBar(toolbar);

        motorControlFragment = LynxModuleMotorControlFragment.newInstance("p2", "p2");
        servoControlFragment = LynxModuleMotorControlFragment.newInstance("p2", "p2");
        digitalControlFragment = LynxModuleDigitalControlFragment.newInstance("p2", "p2");
        analogControlFragment = LynxModuleAnalogControlFragment.newInstance("p2", "p2");
        monitorsFragment = new LynxModuleMonitorsFragment();
        extraFragment = new LynxModuleExtraFragment();

        motorControlFragment.setWriteDatagram(hubToolkitWriteDatagram);
        servoControlFragment.setWriteDatagram(hubToolkitWriteDatagram);
        digitalControlFragment.setWriteDatagram(hubToolkitWriteDatagram);
        analogControlFragment.setWriteDatagram(hubToolkitWriteDatagram);
        monitorsFragment.setWriteDatagram(hubToolkitWriteDatagram);
        extraFragment.setWriteDatagram(hubToolkitWriteDatagram);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        networkingManager.setHubToolkitDataHandler(this);
        networkingManager.sendMsg(new NetworkCommand(CommandList.START_HUBTOOLKIT_STREAM.toString()));
        writeLooper = new WriteLooper();
        writeLooper.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        networkingManager.setHubToolkitDataHandler(null);
        networkingManager.sendMsg(new NetworkCommand(CommandList.STOP_HUBTOOLKIT_STREAM.toString()));
        writeLooper.interrupt();
        writeLooper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lynx_module_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment()
        {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_lynx_module_control, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            //return LynxModuleMotorControlFragment.newInstance("p1", "p2");

            switch (position)
            {
                case 0:
                {
                    return motorControlFragment;
                }

                case 1:
                {
                    return servoControlFragment;
                }

                case 2:
                {
                    return digitalControlFragment;
                }

                case 3:
                {
                    return analogControlFragment;
                }

                case 4:
                {
                    return monitorsFragment;
                }

                case 5:
                {
                    return extraFragment;
                }
            }

            throw new IllegalArgumentException();
        }

        @Override
        public int getCount()
        {
            return 6;
        }
    }

    @Override
    public void handleHubToolkitReadData(HubToolkitReadDataMsg hubToolkitReadDataMsg)
    {
        hubToolkitReadDatagram.fromByteArray(hubToolkitReadDataMsg.getData());

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                monitorsFragment.onDataUpdate(hubToolkitReadDatagram);
            }
        });
    }

    class WriteLooper extends Thread
    {
        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted())
            {
                HubToolkitWriteDataMsg writeDataMsg = new HubToolkitWriteDataMsg();
                writeDataMsg.setData(hubToolkitWriteDatagram.encode());
                networkingManager.sendMsg(writeDataMsg);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
