package tcc.davidhuamani.trigaming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Titulo> listTitulos;
    Toolbar tb;
    ListView listGames;
    AdapterGames adapterGames;
    Context context;
    ImageView helpImg, loadImg;
    MenuItem filtrarBusca;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        helpImg = (ImageView) findViewById(R.id.imgHelp);
        loadImg = (ImageView) findViewById(R.id.imgLoading);
        listGames = (ListView) findViewById(R.id.listVTitulos);
        listTitulos = new ArrayList<Titulo>();
        adapterGames = new AdapterGames(context, listTitulos);
        listGames.setAdapter(adapterGames);
        listGames.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Titulo item = (Titulo) adapterGames.getItem(position);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getURLSite()));
                    startActivity(browserIntent);
                } catch (Exception e){
                    Log.e("Erro", "Itemclick");
                    e.printStackTrace();
                }
            }
        });
        Glide.with(this)
                .load(R.drawable.img_loading)
                .into(loadImg);
        mudarLayout(HomeState.Inicial);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        filtrarBusca = menu.findItem(R.id.action_filter);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mudarLayout(HomeState.Carregando);
                new GetData().execute(query);
                Log.d("Search exec", "Text: " + query);
                if (!searchView.isIconified())
                    searchView.setIconified(true);
                searchItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) { return true; }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) { return true; }
        };

        // Get the MenuItem for the action item
        MenuItem actionMenuItem = menu.findItem(R.id.action_search);

        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(actionMenuItem, expandListener);

        // Any other things you have to do when creating the options menu…
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.asc:
                Collections.sort(listTitulos, new FiltrarPreco());
                for (Titulo t:listTitulos) {
                    Log.d(t.getNome(), t.getPreco());
                }
                adapterGames = new AdapterGames(getApplicationContext(), listTitulos);
                listGames.setAdapter(adapterGames);
                return true;
            case R.id.desc:
                Collections.sort(listTitulos, new FiltrarPreco());
                Collections.reverse(listTitulos);
                for (Titulo t:listTitulos) {
                    Log.d(t.getNome(), t.getPreco());
                }
                adapterGames = new AdapterGames(getApplicationContext(), listTitulos);
                listGames.setAdapter(adapterGames);
                return true;
            case R.id.free:
                List<Titulo> freeList = new ArrayList<Titulo>();
                for (Titulo t: listTitulos)
                    if(t.getPrecoFlt() == 0.f)
                        freeList.add(t);
                adapterGames = new AdapterGames(getApplicationContext(), freeList);
                listGames.setAdapter(adapterGames);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, OpcoesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void mudarLayout(HomeState homeState){
        helpImg.setVisibility(View.INVISIBLE);
        loadImg.setVisibility(View.INVISIBLE);
        listGames.setVisibility(View.INVISIBLE);
        switch (homeState){
            case Inicial:
                helpImg.setVisibility(View.VISIBLE);
                break;
            case Carregando:
                loadImg.setVisibility(View.VISIBLE);
                break;
            case Feito:
                listGames.setVisibility(View.VISIBLE);
                break;
        }
    }

    public class GetData extends AsyncTask<String, Void, Void> {
        Document[] doc;
        @Override
        protected Void doInBackground(String... params) {
            try {
                doc = new Document[2];
                if(preferences.getBoolean("busca_steam",true))
                    doc[Site.Steam.getInt()] = Jsoup.connect(Processador.getSteamURL(params[0]))
                            .get();
                if(preferences.getBoolean("busca_ggate",true))
                    doc[Site.GGATE.getInt()] = Jsoup.connect(Processador.getGGateURL(params[0]))
                            .get();

            } catch (IOException e) {
                Log.d("Error", "Erro!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listTitulos = new ArrayList<Titulo>();
            if(doc[Site.Steam.getInt()] != null)
                listTitulos.addAll(Processador.getSteamTitles(doc[Site.Steam.getInt()],
                        preferences.getString("resultados_steam", "5")));
            if(doc[Site.GGATE.getInt()] != null)
                listTitulos.addAll(Processador.getGGateTitles(doc[Site.GGATE.getInt()],
                        preferences.getString("resultados_ggate", "5")));
            filtrarBusca.setVisible(true);
            adapterGames = new AdapterGames(getApplicationContext(), listTitulos);
            listGames.setAdapter(adapterGames);
            mudarLayout(HomeState.Feito);
        }
    }
    private enum HomeState { Inicial, Carregando, Feito }
}
