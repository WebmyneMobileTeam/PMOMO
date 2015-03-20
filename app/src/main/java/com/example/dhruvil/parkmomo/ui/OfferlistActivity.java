package com.example.dhruvil.parkmomo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.model.Offer;

import java.util.ArrayList;
import java.util.List;


public class OfferlistActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView listHomeProducts;
    private int icons[] = {R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.nine,
            R.drawable.ten};
    private String names[] = {"Buffalo & Beafsdftg fdghgfhfgjhgjghjkjgkj", "See Food", "Lamb/Mutton", "Chicken & Duck", "Pork & Turkey", "Vegetables & Fruits",
            "Dairy Product", "Rabbit & Hen", "Eggs/Ovos", "Beverage"};
    private String sDescription[] = {"There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        
        listHomeProducts = (ListView) findViewById(R.id.listHomeProducts);
        fillAndSet();
        

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("TiSCO");
//            toolbar.setLogo(R.drawable.logo);
            setSupportActionBar(toolbar);
        }

    }
    private void fillAndSet() {

        List<Offer> products = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {

            Offer product = new Offer();
            product.shortDetail = sDescription[0];
            product.imgRes = icons[i];
            product.name = names[i];
      
            products.add(product);

        }
        
        MyAppAdapter adapter = new MyAppAdapter(products,OfferlistActivity.this);
        listHomeProducts.setAdapter(adapter);

    }


    public class MyAppAdapter extends BaseAdapter {

        public class ViewHolder {
            public TextView text;
            public ImageView image;
            public TextView description;
        }

        public List<Offer> apps;
        public Context context;

        private MyAppAdapter(List<Offer> apps, Context context) {
            this.apps = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            ViewHolder viewHolder;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.offerlist_item_category, null);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.txtNameHomeProduct);
                viewHolder.image = (ImageView) rowView.findViewById(R.id.imgHomeProduct);
                viewHolder.description = (TextView) rowView.findViewById(R.id.txtDetailHomeProduct);
                viewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            Offer product = apps.get(position);
            viewHolder.description.setText(product.shortDetail);
            viewHolder.image.setImageResource(product.imgRes);
            viewHolder.text.setText(product.name);

            return rowView;
        }
    }


}
