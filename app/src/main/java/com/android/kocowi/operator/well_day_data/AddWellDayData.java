package com.android.kocowi.operator.well_day_data;

import android.os.Bundle;
import android.widget.Toast;

import com.android.kocowi.R;
import com.android.kocowi.model.Well;

import androidx.appcompat.app.AppCompatActivity;

public class AddWellDayData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_well_day_data);

        Well well = getIntent().getParcelableExtra(Well.class.getName());
        Toast.makeText(this, well.getName(), Toast.LENGTH_SHORT).show();
    }
}
