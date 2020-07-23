package tcc.davidhuamani.trigaming;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;

/**
 * Created by david on 19/06/2017.
 */

    public class Titulo {
        private int Id;
        private String Nome;
        private String Preco;
        private Site SiteVendedor;
        private String URLImg;
        private String URLSite;
        private float PrecoFlt;

        public Titulo(){    }
        public int getId() {
            return Id;
        }
        public void setId(int id) {
            Id = id;
        }
        public String getNome() {
            return Nome;
        }
        public void setNome(String nome) {
            Nome = nome;
        }
        public String getPreco() {
            if (PrecoFlt == 0.f)
                return "Gratis";
            else
                return NumberFormat.getCurrencyInstance().format(PrecoFlt);
        }
        public void setPreco(String preco) {
            Preco = preco;
            String aux = preco.replaceAll("[^0-9]", "");
            if (aux.isEmpty() || aux == null)
                PrecoFlt = 0.0f;
            else{
                BigDecimal bd = BigDecimal.valueOf(Long.parseLong(aux)).movePointLeft(2);
                PrecoFlt = bd.floatValue();
            }
        }
        public String getURLImg() { return URLImg; }
        public void setURLImg(String URLImg) { this.URLImg = URLImg; }
        public String getURLSite() { return URLSite; }
        public void setURLSite(String URLSite) { this.URLSite = URLSite; }
        public Site getSiteVendedor() { return SiteVendedor; }
        public void setSiteVendedor(Site siteVendedor) { SiteVendedor = siteVendedor; }
        public float getPrecoFlt() { return PrecoFlt; }
    }
    class FiltrarPreco implements Comparator<Titulo>{
        @Override
        public int compare(Titulo o1, Titulo o2) {
            int val = 0;
            if (o1.getPrecoFlt() > o2.getPrecoFlt())
                val = 1;
            else if(o1.getPrecoFlt() < o2.getPrecoFlt())
                val = -1;
            return val;
        }
    }
enum Site {
    Steam(0), GGATE(1);
    private int intSite;
    Site(int intSite){
        this.intSite = intSite;
    }
    public int getInt(){
        return intSite;
    }
}