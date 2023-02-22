package com.example.nightnightconnor;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nightnightconnor.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Switch edit_am_pm;
    private Switch edit_on_off;
    private EditText edithours;
    private EditText editminutes;
    private Button savebutton;
    private SharedPreferences preferences;

    private int hours = 0;
    private int minutes = 0;
    private boolean am_pm = false;
    private boolean on_off = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setup
        edithours = (EditText) findViewById(R.id.editHour);
        editminutes = (EditText) findViewById(R.id.editMinutes);
        edit_am_pm = (Switch) findViewById(R.id.AM_PM);
        edit_on_off = (Switch) findViewById(R.id.on_off);
        savebutton = (Button) findViewById(R.id.savebutton);
        preferences = getSharedPreferences("nightnightconnorpreferences", Context.MODE_PRIVATE);

        //initialize
        try {
            hours = preferences.getInt("hours", 0);
        }catch ( Exception Resources$NotFoundException) {
            Log.i("tag","hours not found");
        }

        try {
            minutes = preferences.getInt("minutes", 0);
        }catch ( Exception Resources$NotFoundException) {
            Log.i("tag","minutes not found");
        }
        try {
            am_pm = preferences.getBoolean("am_pm", true);
        }catch ( Exception Resources$NotFoundException) {
            Log.i("tag","am_pm not found");
        }
        try {
            on_off = preferences.getBoolean("on_off", true);
        }catch ( Exception Resources$NotFoundException) {
            Log.i("tag","on_off not found");
        }
        edithours.setText(String.valueOf(hours));
        editminutes.setText(String.valueOf(minutes));
        edit_am_pm.setChecked(am_pm);
        edit_on_off.setChecked(on_off);

        //listeners
        edithours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.i("name", (String) charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int val = Integer.parseInt(editable.toString());
                    if(val > 12) {
                        editable.replace(0, editable.length(), "12", 0, 2);
                    } else if(val < 0) {
                        editable.replace(0, editable.length(), "0", 0, 1);
                    }
                    hours = Integer.parseInt(editable.toString());
                    Log.i("tag",hours+" hours");
                } catch (NumberFormatException ex) {
                    // Do something
                }
            }
        });
        editminutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.i("name", (String) charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int val = Integer.parseInt(editable.toString());
                    if(val > 59) {
                        editable.replace(0, editable.length(), "59", 0, 2);
                    } else if(val < 0) {
                        editable.replace(0, editable.length(), "0", 0, 1);
                    }
                    minutes = Integer.parseInt(editable.toString());
                    Log.i("tag",minutes+" minutes");
                } catch (NumberFormatException ex) {
                    // Do something
                }
            }
        });
        edit_am_pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am_pm = edit_am_pm.isChecked();
            }
        });
        edit_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on_off = edit_on_off.isChecked();
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invoker invoker = new Invoker();
                invoker.execute(new MyRunnable());
            }
        });

    }

    public class Invoker implements Executor {
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("hours",hours);
            editor.putInt("minutes",minutes);
            editor.putBoolean("am_pm",am_pm);
            editor.putBoolean("on_off",on_off);
            boolean saved = editor.commit();
            if(saved) {
                Toast.makeText(MainActivity.this,"changes saved",Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(MainActivity.this,"changes not saved",Toast.LENGTH_LONG).show();
            }
        }
    }


}