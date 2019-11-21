package com.android.kocowi.operator.welldetails;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.users.UsersRepository;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.User;
import com.android.kocowi.model.Well;
import com.android.kocowi.operator.well_day_data.AddWellDataBottomFragment;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WellDetails extends AppCompatActivity implements WellsRepository.WellsRetrievingCallback {

    private WellDetailsPresenter presenter;
    private String operatorGc;
    private Well currentWell;
    private ArrayList<String> wellIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_details);

        presenter = new WellDetailsPresenter(Injection.provideWellDetailsUseCaseHandler());
        presenter.getCurrentOperator(new UsersRepository.UsersRetrievingCallback() {
            @Override
            public void onUserRetrievedSuccessfully(User user) {
                operatorGc = user.getGcCode();
                getTriggeredWells();
            }

            @Override
            public void onUserRetrievedFailed(String err) {
                Toast.makeText(WellDetails.this, err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTriggeredWells() {
        wellIds = getIntent().getStringArrayListExtra(WellDetails.class.getName());
        if (wellIds.size() == 1) {
            String wellCode = wellIds.get(0).split("-")[0].trim();
            presenter.getWellDetails(wellCode, this);
        } else if (wellIds.size() > 1) {
            Toast.makeText(this, "more than one well is detected", Toast.LENGTH_LONG).show();
            showListOfDetectedWells(wellIds);
        }
    }

    @Override
    public void onRetrievingWellsSuccessfully(ArrayList<Well> wellsList) {
        for (Well well : wellsList) {
            if (well.getGcCode().equalsIgnoreCase(operatorGc)) {
                currentWell = well;
                setWellDetails(well);
                Toast.makeText(this, "Show well details, " + well.getName(), Toast.LENGTH_LONG).show();
            } else {
                showWellNotBelongToOperatorDialog(well, wellIds.size() > 1);
            }
        }
    }

    @Override
    public void onRetrievingWellsFailed(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    private void setWellDetails(Well well) {
        findViewById(R.id.well_details_layout).setVisibility(View.VISIBLE);
        TextView title = findViewById(R.id.title_textview);
        title.setText(well.getName());

        TextView wellName = findViewById(R.id.well_name_textview);
        wellName.setText(String.format(Locale.US, "Well Name : %s", well.getName()));

        TextView gcCode = findViewById(R.id.gc_code_textview);
        gcCode.setText(String.format(Locale.US, "GC Code : %s", well.getGcCode()));

        TextView latitude = findViewById(R.id.well_latitude_textview);
        latitude.setText(String.format(Locale.US, "Latitude : %s", well.getLatitude()));

        TextView longitude = findViewById(R.id.well_longitude_textview);
        longitude.setText(String.format(Locale.US, "Longitude : %s", well.getLongitude()));

        TextView notes = findViewById(R.id.well_notes_textview);
        notes.setText(String.format(Locale.US, "Well Notes : %s", well.getNotes()));
    }

    private void showWellNotBelongToOperatorDialog(Well well, boolean moreThanWell) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_oil_drill_icon_22)
                .setTitle(String.format(Locale.US, "Well %s is detected", well.getName()))
                .setMessage("You are belong to another GC, So you can't see this Well details!")
                .setCancelable(false);
        if (!moreThanWell) {
            dialogBuilder.setPositiveButton("Detect Another Well", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        } else {
            dialogBuilder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    showListOfDetectedWells(wellIds);
                }
            });
        }
        dialogBuilder.show();
    }

    private void showListOfDetectedWells(final ArrayList<String> wellRequestIds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("More than well are detected");

        builder.setItems((wellRequestIds.toArray(new String[wellRequestIds.size()])), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String wellCode = wellRequestIds.get(which).split("-")[0].trim();
                presenter.getWellDetails(wellCode, WellDetails.this);
            }
        }).setPositiveButton("Detect Another Well", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setCancelable(false);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addTodayWellData(View view) {
        AddWellDataBottomFragment bottomSheetFragment = AddWellDataBottomFragment.getInstance(currentWell);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

}
