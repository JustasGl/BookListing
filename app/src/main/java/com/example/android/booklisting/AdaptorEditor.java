package com.example.android.booklisting;

/**
 * Created by Justas on 1/14/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdaptorEditor extends ArrayAdapter<knyga> {
    TextView kaina, valiuta, autorius, pavadinimas;
    ImageView virselis;
    private Context context;
    public AdaptorEditor(Context context, ArrayList<knyga> earthquakes) {
        super(context, 0, earthquakes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        kaina = listItemView.findViewById(R.id.kaina);
        valiuta = listItemView.findViewById(R.id.valiuta);
        autorius = listItemView.findViewById(R.id.autorius);
        pavadinimas = listItemView.findViewById(R.id.pavadinimas);
        virselis = listItemView.findViewById(R.id.paveikslelis);
        knyga vieta = getItem(position);
        if (vieta.getMvaliuta().equals("noCurrency")) {
            valiuta.setVisibility(View.GONE);
            kaina.setVisibility(View.GONE);
        } else {
            valiuta.setVisibility(View.VISIBLE);
            kaina.setVisibility(View.VISIBLE);
            kaina.setText("" + vieta.getMkaina());
            valiuta.setText(vieta.getMvaliuta());
        }
        if(vieta.getMImgUrl()=="no thumbnail")
        {virselis.setVisibility(View.GONE);}
        else
        Picasso.with(getContext()).load(vieta.getMImgUrl()).into((ImageView) listItemView.findViewById(R.id.paveikslelis));

        pavadinimas.setText(vieta.getMpavadinimas());
        if(vieta.getMautorius().equals("no author"))
        {
            autorius.setVisibility(View.GONE);
        }
        else
        autorius.setText(vieta.getMautorius());
        return listItemView;
    }

}