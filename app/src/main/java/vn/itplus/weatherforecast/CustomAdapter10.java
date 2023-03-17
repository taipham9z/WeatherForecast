package vn.itplus.weatherforecast;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter10 extends BaseAdapter {
    Context context;
    ArrayList<ThoiTiet10day> arrayList;

    public CustomAdapter10(Context context, ArrayList<ThoiTiet10day> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_10_day, null);

        ThoiTiet10day thoiTiet10day = arrayList.get(i);
        TextView txtDay = view.findViewById(R.id.txtNgayThang);
        TextView txtStatus = view.findViewById(R.id.txtTrangThai);
        TextView txtTempMax = view.findViewById(R.id.txtMaxTemp) ;
        TextView txtTempMin = view.findViewById(R.id.txtMinTemp);
        ImageView imgStatus = view.findViewById(R.id.imgTrangThai);

        txtDay.setText(thoiTiet10day.getDay());
        txtStatus.setText(thoiTiet10day.getStatus());
        txtTempMax.setText(thoiTiet10day.getMaxtemp()+ "°C");
        txtTempMin.setText(thoiTiet10day.getMintemp()+ "°C");

        Picasso.get().load(thoiTiet10day.getImage()).into(imgStatus);
        return view;
    }
}
