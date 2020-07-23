package tcc.davidhuamani.trigaming;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by chipa on 11/08/2017.
 */

public class AdapterGames extends BaseAdapter {
    private final List<Titulo> titulos;
    private final Context context;
    public AdapterGames(Context context, List<Titulo> titulos) {
        this.titulos = titulos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return titulos.size();
    }

    @Override
    public Object getItem(int position) {
        return titulos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Titulo titulo = titulos.get(position);
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_layout_games, null);
        } else
            view = convertView;
        TextView nome_titulo = (TextView) view.findViewById(R.id.nome_titulo);
        TextView valor_titulo = (TextView) view.findViewById(R.id.valor_titulo);
        ImageView img_titulo = (ImageView) view.findViewById(R.id.imagem_titulo);
        ImageView img_logo = (ImageView) view.findViewById(R.id.imagem_logo);
        nome_titulo.setText(titulo.getNome());
        valor_titulo.setText(titulo.getPreco());
        if (titulo.getSiteVendedor() == Site.Steam)
            img_logo.setImageResource(R.drawable.logo_steam);
        else if(titulo.getSiteVendedor() == Site.GGATE)
            img_logo.setImageResource(R.drawable.logo_ggate);
        if(titulo.getURLImg() != null)
            new DownloadImageTask(img_titulo).execute(titulo.getURLImg());
        return view;
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
