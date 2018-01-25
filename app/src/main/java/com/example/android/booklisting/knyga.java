package com.example.android.booklisting;

/**
 * Created by Justas on 1/14/2018.
 */

public class knyga {
    private double Mkaina=10000000.05648;
    private String Mpavadinimas, Mautorius,Murl, MImgUrl,Mvaliuta="noCurrency";

    public knyga (String autorius, String pavadinimas, String url, String ImgUrl, String valiuta, double kaina)
    {
        Mpavadinimas=pavadinimas;
        Mautorius=autorius;
        Murl=url;
        Mkaina=kaina;
        Mvaliuta=valiuta;
        MImgUrl=ImgUrl;
    }
    public knyga (String autorius, String pavadinimas,String url, String ImgUrl)
    {
        Mpavadinimas=pavadinimas;
        Mautorius=autorius;
        Murl=url;
        MImgUrl=ImgUrl;
    }
    public String getMpavadinimas (){return Mpavadinimas;}
    public String getMautorius (){return Mautorius;}
    public String getMurl (){return Murl;}
    public String getMvaliuta () {return Mvaliuta;}
    public String getMImgUrl () {return MImgUrl;}
    public double getMkaina (){return Mkaina;}
}
