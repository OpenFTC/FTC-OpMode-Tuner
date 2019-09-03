package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.activity.UdpConnectionActivity;
import net.frogbots.ftcopmodetunercommon.networking.udp.CommandHandler;
import net.frogbots.ftcopmodetunercommon.networking.udp.CommandList;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkCommand;

import java.util.concurrent.CountDownLatch;

public class HubSelectionActivity extends UdpConnectionActivity implements CommandHandler
{
    ListView hubListView;
    ProgressBar detectingHubsProgressBar;
    SearchingThread searchingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_selection);

        getSupportActionBar().setTitle("REV Hub Toolkit");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.hub_toolkit_actionbar_color)));
        /*
         * Set the status bar color to colorPrimaryDark
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.hub_toolkit_actionbar_color));
        }

        hubListView = findViewById(R.id.hubListView);
        detectingHubsProgressBar = findViewById(R.id.detectingHubsProgressBar);

        hubListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(HubSelectionActivity.this, LynxModuleControlActivity.class);
                intent.putExtra("module", (String) adapterView.getAdapter().getItem(i));
                startActivity(intent);
                //System.out.println(adapterView.getAdapter().getItem(i));
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        networkingManager.registerCommandHandler(this);
        searchingThread = new SearchingThread();
        searchingThread.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        networkingManager.unregisterCommandHandler(this);
        searchingThread.interrupt();
    }

    @Override
    public Result handleCommand(final NetworkCommand command)
    {
        if(command.getName().equals(CommandList.QUERY_LIST_OF_LYNX_MODULES_RESP.toString()))
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    detectingHubsProgressBar.setVisibility(View.GONE);

                    String[] hubNames = command.getExtra().split(";");

                    ArrayAdapter adapter = new ArrayAdapter<String>(HubSelectionActivity.this, R.layout.lynx_module_list_item, hubNames);

                    hubListView.setAdapter(adapter);
                }
            });

            return Result.HANDLED;
        }

        return Result.NOT_HANDLED;
    }

    class SearchingThread extends Thread
    {
        private volatile boolean shouldContinue = true;

        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted() && shouldContinue)
            {
                System.out.println("Req lynx mods");

                final CountDownLatch latch = new CountDownLatch(1);

                NetworkCommand networkCommand = new NetworkCommand(CommandList.QUERY_LIST_OF_LYNX_MODULES.toString());

                networkCommand.setListener(new NetworkCommand.AckOrNackListener()
                {
                    @Override
                    public void onAck()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                shouldContinue = false;
                                latch.countDown();
                            }
                        });
                    }

                    @Override
                    public void onNack()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                latch.countDown();
                            }
                        });
                    }
                });

                networkingManager.sendMsg(networkCommand);

                try
                {
                    latch.await();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
