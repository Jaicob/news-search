package com.jaicob.newssearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaicob.newssearch.R;
import com.jaicob.newssearch.models.BeginDateRange;
import com.jaicob.newssearch.models.SearchSetting;

import org.parceler.Parcels;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup rgSortGroup;
    private RadioGroup rgBeginDateGroup;
    private RadioButton rbtnNewest;
    private RadioButton rbtnOldest;
    private RadioButton rbtnToday;
    private RadioButton rbtnThisWeek;
    private RadioButton rbtnThisMonth;
    private RadioButton rbtnThisYear;
    private RadioButton rbtnUnset;
    SearchSetting searchSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchSettings = (SearchSetting) Parcels.unwrap(getIntent().getParcelableExtra("searchSettings"));
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_settings, menu);
        return true;
    }

    private void setupViews(){
        rgSortGroup = (RadioGroup) findViewById(R.id.rgSortGroup);
        rgBeginDateGroup = (RadioGroup) findViewById(R.id.rgBeginDateGroup);
        rbtnNewest = (RadioButton) findViewById(R.id.rbtnNewest);
        rbtnOldest = (RadioButton) findViewById(R.id.rbtnOldest);
        rbtnToday = (RadioButton) findViewById(R.id.rbtnToday);
        rbtnThisWeek = (RadioButton) findViewById(R.id.rbtnThisWeek);
        rbtnThisMonth = (RadioButton) findViewById(R.id.rbtnThisMonth);
        rbtnThisYear = (RadioButton) findViewById(R.id.rbtnThisYear);
        rbtnUnset = (RadioButton) findViewById(R.id.rbtnUnset);

        switch (searchSettings.getSort()){
            case SearchSetting.NEWEST:
                rbtnNewest.setChecked(true);
                break;
            case SearchSetting.OLDEST:
                rbtnOldest.setChecked(true);
                break;
        }

        switch (searchSettings.getBeginDateRange()){
            case TODAY:
                rbtnToday.setChecked(true);
                break;
            case THIS_WEEK:
                rbtnThisWeek.setChecked(true);
                break;
            case THIS_MONTH:
                rbtnThisMonth.setChecked(true);
                break;
            case THIS_YEAR:
                rbtnThisYear.setChecked(true);
                break;
            case UNSET:
                rbtnUnset.setChecked(true);
                break;
        }
    }

    public void onSortRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.rbtnNewest:
                if (checked){
                    searchSettings.setSort(searchSettings.NEWEST);
                }
                break;
            case R.id.rbtnOldest:
                if (checked){
                    searchSettings.setSort(searchSettings.OLDEST);
                }
        }
    }

    public void onDateRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.rbtnToday:
                if (checked){
                    searchSettings.setBeginDateRange(BeginDateRange.TODAY);
                }
                break;
            case R.id.rbtnThisWeek:
                if (checked){
                    searchSettings.setBeginDateRange(BeginDateRange.THIS_WEEK);
                }
                break;
            case R.id.rbtnThisMonth:
                if (checked){
                    searchSettings.setBeginDateRange(BeginDateRange.THIS_MONTH);
                }
                break;
            case R.id.rbtnThisYear:
                if (checked){
                    searchSettings.setBeginDateRange(BeginDateRange.THIS_YEAR);
                }
                break;
            case R.id.rbtnUnset:
                if (checked){
                    searchSettings.setBeginDateRange(BeginDateRange.UNSET);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.action_save:
                Intent result = new Intent();
                result.putExtra("searchSettings",Parcels.wrap(searchSettings));
                setResult(RESULT_OK,result);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
