package org.iiui.shoppingapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shopping.HelperS;
import com.example.shopping.Main3Activity;
import com.example.shopping.Main4Activity;
import com.example.shopping.R;
import com.example.shopping.iteminfolist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class AdapterClass extends ArrayAdapter<iteminfolist> {
    Context context;
    HelperS helper;
    SQLiteDatabase database;
    boolean flag=true;
    SharedPreferences preferences,preferences1;
    SharedPreferences.Editor editor,editor1;
    int layoutrid;
    ArrayList<iteminfolist> items;

    TextView ty1,ty2;
    Cursor cursor;
    iteminfolist it;







    public AdapterClass(Context context, int resource, ArrayList<iteminfolist> items) {

        super(context, resource, items);
        this.context = context;
        this.layoutrid = resource;
        this.items = items;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ty1=(TextView)((Activity)context).findViewById(R.id.rps);
        ty2=(TextView)((Activity)context).findViewById(R.id.subtot);
        helper = new HelperS(context);
        database = helper.getWritableDatabase();

        //   load();

        Integer image = getItem(position).getImg();
        final String name11 = getItem(position).getNam();
        final String[] pr = {getItem(position).getPri()};
        final int[] total = {getItem(position).getTotal()};
        final String uu = getItem(position).getId1();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutrid, parent, false);




        final ImageView img = convertView.findViewById(R.id.crtimg);
        final TextView tvname = convertView.findViewById(R.id.crtnam);
        final TextView tvpr = convertView.findViewById(R.id.crtpr);
        final TextView tvimg = convertView.findViewById(R.id.idimg);
        final Button  btn1 = convertView.findViewById(R.id.dig1);
        final Button  bb1 = convertView.findViewById(R.id.minus1);
        final Button  bb3 = convertView.findViewById(R.id.plus1);
        final int[] qn = new int[1];


        img.setImageResource(image);
        tvname.setText(name11);
        tvpr.setText(pr[0]);
        btn1.setText(String.valueOf(total[0]));

/*
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ty1.findViewById(R.id.subtot).getLayoutParams());
        layoutParams.setMargins(200,50,0,20);
        LinearLayout.LayoutParams layoutParams1= new LinearLayout.LayoutParams(ty1.findViewById(R.id.rps).getLayoutParams());
        layoutParams1.setMargins(-400,50,0,20);
        */


        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = database.rawQuery("SELECT ItemID,Name,Price,Code,Quantity FROM Item_Info", null);
                while (cursor.moveToNext()) {
                    String nid = cursor.getString(0);

                    if (nid.equals(uu)) {
                        qn[0] = Integer.parseInt(cursor.getString(4));


                        break;
                    }
                }
                cursor = database.rawQuery("SELECT ItemID,Name,Price,Code,Quantity FROM Jewellery_INFO", null);

                while (cursor.moveToNext()) {
                    String nm = cursor.getString(0);
                    if (nm.equals(uu)) {
                        qn[0] = Integer.parseInt(cursor.getString(4));
                        break;
                    }

                }


                String f = btn1.getText().toString();
                total[0] = Integer.parseInt(f);
                if (total[0] >= 1) {
                    bb1.setBackgroundColor(context.getResources().getColor(R.color.white));
                    bb1.setTextColor(context.getResources().getColor(R.color.black));
                    bb1.setEnabled(true);
                    total[0] = total[0] - 1;

                    String ht = String.valueOf(total[0]);
                    btn1.setText(ht);
                }
                if (total[0] == 1) {
                    bb1.setBackgroundColor(context.getResources().getColor(R.color.black));
                    bb1.setTextColor(context.getResources().getColor(R.color.white));
                    bb1.setEnabled(true);

                }
                if(total[0]==0)
                {
                    preferences = context.getSharedPreferences("shared preferences", 0);
                    Gson gson = new Gson();
                    String uo =preferences.getString("task list", null);
                    Type type = new TypeToken<ArrayList<iteminfolist>>() {
                    }.getType();
                    Main3Activity main3Activity=new Main3Activity();
                    main3Activity.itemInfoList=gson.fromJson(uo, type);
                    //  items = gson.fromJson(uo, type);
                    for(int i=0 ; i<Main3Activity.itemInfoList.size(); i++)
                    {
                        if(Main3Activity.itemInfoList.get(i).getId1().equals(uu))
                        {
                            String Id=Main3Activity.itemInfoList.get(i).getId1();
                            helper.updateCart(Id,0,database);
                            Main3Activity.itemInfoList.remove(i);

                            break;
                        }
                    }

                    preferences = context.getSharedPreferences("shared preferences", 0);
                    editor = preferences.edit();
                    gson = new Gson();

                    String json = gson.toJson(Main3Activity.itemInfoList);
                    editor.putString("task list", json);
                    editor.apply();
                    img.setVisibility(View.INVISIBLE);
                    tvname.setVisibility(View.INVISIBLE);
                    tvpr.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.INVISIBLE);
                    bb1.setVisibility(View.INVISIBLE);
                    bb3.setVisibility(View.INVISIBLE);



                }
                if (total[0] <= qn[0]) {
                    bb3.setBackgroundColor(context.getResources().getColor(R.color.white));
                    bb3.setTextColor(context.getResources().getColor(R.color.black));
                    bb3.setEnabled(true);
                }




                preferences = context.getSharedPreferences("shared preferences", 0);
                Gson gson = new Gson();
                String uo =preferences.getString("task list", null);
                Type type = new TypeToken<ArrayList<iteminfolist>>() {
                }.getType();
                Main3Activity main3Activity=new Main3Activity();
                main3Activity.itemInfoList=gson.fromJson(uo, type);
                //  items = gson.fromJson(uo, type);
                for(int i=0 ; i<Main3Activity.itemInfoList.size(); i++)
                {
                    if(Main3Activity.itemInfoList.get(i).getId1().equals(uu))
                    {

                        //  Main3Activity.itemInfoList.get(i).setTotal(Integer.parseInt(String.valueOf(btn1.getText())));
                        //Main3Activity.itemInfoList.get(i).setTotal(4);
                        Main3Activity.itemInfoList.get(i).setTotal(Integer.parseInt(String.valueOf(total[0])));
                        ContentValues cv = new ContentValues();
                        cv.put("Total",(String.valueOf(total[0])));
                        String Id=Main3Activity.itemInfoList.get(i).getId1();

                        helper.updateCart(Id,Integer.parseInt(String.valueOf(total[0])),database);
                        Log.d("value of total",String.valueOf(total[0]));

                        break;

                    }
                }


                preferences = context.getSharedPreferences("shared preferences", 0);
                editor = preferences.edit();
                gson = new Gson();

                String json = gson.toJson(Main3Activity.itemInfoList);
                editor.putString("task list", json);
                editor.apply();


                Main3Activity main3Activity1=new Main3Activity();
                Main3Activity.subtotal=0;
                if(Main3Activity.itemInfoList.isEmpty() )
                {
                    Intent intent=new Intent(view.getContext(), Main4Activity.class);
                    context.startActivity(intent);



                }
                else {


                    for (int i = 0; i < Main3Activity.itemInfoList.size(); i++) {


                        int ttt = Main3Activity.itemInfoList.get(i).getTotal();
                        String po = Main3Activity.itemInfoList.get(i).getPri();
                        String[] arr = po.split("\\.");
                        int prc1 = Integer.parseInt(arr[1]);
                        prc1 = prc1 * ttt;
                        Main3Activity.subtotal = Main3Activity.subtotal + prc1;
                        ty1.setText(String.valueOf(Main3Activity.subtotal));
                        Log.d("Subtotal value", String.valueOf(Main3Activity.subtotal));

                    }

                }
                Main3Activity.subtotal=0;

            }
        });
        bb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cursor = database.rawQuery("SELECT ItemID,Name,Price,Code,Quantity FROM Item_Info", null);
                while (cursor.moveToNext()) {
                    String nm = cursor.getString(0);
                    if (nm.equals(uu)) {
                        qn[0] = Integer.parseInt(cursor.getString(4));
                        break;
                    }

                }
                cursor = database.rawQuery("SELECT ItemID,Name,Price,Code,Quantity FROM Jewellery_INFO", null);

                while (cursor.moveToNext()) {
                    String nm = cursor.getString(0);
                    if (nm.equals(uu)) {
                        qn[0] = Integer.parseInt(cursor.getString(4));
                        break;
                    }

                }


                String j = btn1.getText().toString();

                total[0] = Integer.parseInt(j);
                if (qn[0] > total[0]) {
                    bb3.setBackgroundColor(context.getResources().getColor(R.color.white));
                    bb3.setTextColor(context.getResources().getColor(R.color.black));
                    bb3.setEnabled(true);
                    total[0] = total[0] +1;

                    Log.d("BUTTON TEXT VALUE",String.valueOf(total[0]));
                    btn1.setText(String.valueOf(total[0]));
                }
                if (qn[0] <= total[0]) {

                    bb3.setBackgroundColor(context.getResources().getColor(R.color.black));
                    bb3.setTextColor(context.getResources().getColor(R.color.white));
                    bb3.setEnabled(false);
                }
                if (total[0] > 1) {
                    bb1.setBackgroundColor(context.getResources().getColor(R.color.white));
                    bb1.setTextColor(context.getResources().getColor(R.color.black));
                    bb1.setEnabled(true);
                }

                preferences = context.getSharedPreferences("shared preferences", 0);
                Gson gson = new Gson();
                String uo =preferences.getString("task list", null);
                Type type = new TypeToken<ArrayList<iteminfolist>>() {
                }.getType();
                Main3Activity main3Activity=new Main3Activity();
                main3Activity.itemInfoList=gson.fromJson(uo, type);
                //  items = gson.fromJson(uo, type);
                for(int i=0 ; i<Main3Activity.itemInfoList.size(); i++)
                {
                    if(Main3Activity.itemInfoList.get(i).getId1().equals(uu))
                    {
                        Log.d("value of total",String.valueOf(total[0]));
                        //  Main3Activity.itemInfoList.get(i).setTotal(Integer.parseInt(String.valueOf(btn1.getText())));
                        //Main3Activity.itemInfoList.get(i).setTotal(4);
                        Main3Activity.itemInfoList.get(i).setTotal(Integer.parseInt(String.valueOf(total[0])));


                        String Id=Main3Activity.itemInfoList.get(i).getId1();
                        helper.updateCart(Id,Integer.parseInt(String.valueOf(total[0])),database);
                        break;


                    }
                }


                preferences = context.getSharedPreferences("shared preferences", 0);
                editor = preferences.edit();
                gson = new Gson();

                String json = gson.toJson(Main3Activity.itemInfoList);
                editor.putString("task list", json);
                editor.apply();







                Main3Activity main3Activity1=new Main3Activity();
                Main3Activity.subtotal=0;
                for(int i=0 ; i<Main3Activity.itemInfoList.size() ; i++)
                {
                    int ttt=Main3Activity.itemInfoList.get(i).getTotal();
                    String po=Main3Activity.itemInfoList.get(i).getPri();
                    String[] arr=  po.split("\\.");
                    int prc1= Integer.parseInt(arr[1]);
                    prc1= prc1*ttt;
                    Main3Activity.subtotal= Main3Activity.subtotal+prc1;
                    ty1.setText(String.valueOf(Main3Activity.subtotal));
                    Log.d("Subtotal value",String.valueOf( Main3Activity.subtotal));

                }
                Main3Activity.subtotal=0;
            }


        });










        return convertView;

    }


}
