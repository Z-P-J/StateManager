package com.zpj.recycler.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zpj.statemanager.StateManager;

public class StateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

//        StateManager.config()
//                .setEmptyViewHolder(new CustomizedViewHolder() {
//                    @Override
//                    public void onViewCreated(View view) {
//                        addImageView(R.drawable.ic_launcher_background);
//                        addTextViewWithPadding(R.string.app_name, Color.GRAY);
//                        addTextView("Custom Empty View", Color.GRAY);
//                    }
//                });


        final LinearLayout llContent = findViewById(R.id.ll_content);
        final StateManager manager = StateManager.with(llContent)
//                .setRecyclable(true)
                .setNoNetworkView(R.layout.item_text_grid)
                .onRetry(new StateManager.Action() {
                    @Override
                    public void run(final StateManager manager) {
                        Toast.makeText(StateActivity.this, "onRetry", Toast.LENGTH_SHORT).show();
                        manager.showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        manager.showContent();
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .onLogin(new StateManager.Action() {
                    @Override
                    public void run(StateManager manager) {
                        Toast.makeText(StateActivity.this, "onLogin", Toast.LENGTH_SHORT).show();
                    }
                })
                .showLoading();

        findViewById(R.id.btn_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showContent();
            }
        });

        findViewById(R.id.btn_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showLoading();
            }
        });

        findViewById(R.id.btn_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showEmpty();
            }
        });

        findViewById(R.id.btn_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showError("sjrhugjknfdoilgknjkhglfjdbcvg-" + System.currentTimeMillis());
//                manager.showError();
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showLogin();
            }
        });

        findViewById(R.id.btn_no_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.showNoNetwork();
            }
        });

    }

}
