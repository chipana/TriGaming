package tcc.davidhuamani.trigaming;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class OpcoesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_opcoes);
    }
}
