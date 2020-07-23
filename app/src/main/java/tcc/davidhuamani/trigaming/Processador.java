package tcc.davidhuamani.trigaming;

import android.util.Log;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 27/06/2017.
 */

    public class Processador{
        public static String getSteamURL(String search){
            try {
                return String.format("http://store.steampowered.com/search/?term=%s",
                        URLEncoder.encode(search, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
        public static String getGGateURL(String search){
            try {
                return String.format("https://br.gamersgate.com/games?prio=relevance&q=%s",
                        URLEncoder.encode(search, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
        public static List<Titulo> getSteamTitles(Document html, String resultados){
            Elements retornojogos = html.select("a.search_result_row");
            List<Titulo> listaTitulos = new ArrayList<Titulo>();
            Titulo t;
            Log.d("Numero de Resultados", "Valor: " + retornojogos.size());
            int x = getInt(resultados, retornojogos.size());
            for (int i = 0; i < x; i++) {
                t = new Titulo();
                t.setNome(retornojogos.get(i).getElementsByClass("title").text());
                t.setId(i);
                t.setPreco(retornojogos.get(i).getElementsByClass("search_price").get(0).ownText());
                t.setURLImg(retornojogos.get(i).getElementsByClass("search_capsule").select("img")
                        .first().absUrl("src"));
                t.setURLSite(retornojogos.get(i).absUrl("href"));
                t.setSiteVendedor(Site.Steam);
                listaTitulos.add(t);
            }
            return listaTitulos;
        }
        public static List<Titulo> getGGateTitles(Document html, String resultados){
            Elements retornojogos = html.select("ul.biglist li");
            List<Titulo> listaTitulos = new ArrayList<Titulo>();
            Titulo t;
            Log.d("Numero de Resultados", "Valor: " + retornojogos.size());
            int x = getInt(resultados, retornojogos.size());
            for (int i = 0; i < x; i++) {
                t = new Titulo();
                t.setNome(retornojogos.get(i).getElementsByClass("ttl").first().attr("title"));
                t.setId(i);
                t.setPreco(retornojogos.get(i).select("div[style$=\"50px;\"]").first()
                        .select("span").first().text());
                t.setURLImg(retornojogos.get(i).getElementsByClass("with_box").select("img")
                        .first().absUrl("src"));
                t.setURLSite(retornojogos.get(i).getElementsByClass("ttl").first().absUrl("href"));
                t.setSiteVendedor(Site.GGATE);
                listaTitulos.add(t);
            }
            return listaTitulos;
        }
        private static int getInt(String text, int maxSize){
            int x = 0;
            if(!text.isEmpty() && text != null) {
                try {
                    x = Integer.parseInt(text);
                } catch (NumberFormatException ne) {
                    Log.d("Erro na formatação: ", "Text: " + text);
                }
            }
            if (x > maxSize || x == 0)
                x = maxSize;
            return x;
        }
    }

