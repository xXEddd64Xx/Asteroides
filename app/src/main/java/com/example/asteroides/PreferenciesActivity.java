package com.example.asteroides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PreferenciesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciesFragment()).commit();
        /*final EditTextPreference fragments = (EditTextPreference)findPreference("fragments");
        fragments.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        int valor;
                        try {
                            valor = Integer.parseInt((String)newValue);
                        } catch(Exception e) {
                            Toast.makeText(getApplicationContext(), "Ha de ser un número",

                            Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if (valor>=0 && valor<=9) {
                            fragments.setSummary("En quants trossos es divideix un asteroide ("+valor+")");
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Màxim de fragments 9",
                            Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                });*/
    }
}