package com.example.farmbooking;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<Upload> {

    private Activity context;
    private List<Upload> mUploads;

    public ImageAdapter (Activity context, List<Upload> mUploads) {
        super(context, R.layout.list_layout, mUploads);
        this.context = context;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.image_item, null, true);
        Upload uploadCurr = mUploads.get(position);

        CardView cardView = listViewItem.findViewById(R.id.card);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        TextView textViewRent = listViewItem.findViewById(R.id.text_view_price);
        TextView textViewsize = listViewItem.findViewById(R.id.text_view_size);
        ImageView imageView = listViewItem.findViewById(R.id.image_view_upload);
        TextView textViewType = listViewItem.findViewById(R.id.text_view_type);
        TextView textViewlandmark = listViewItem.findViewById(R.id.text_view_landmark);

        Picasso.with(context).load(uploadCurr.getImageUrl()).placeholder(R.drawable.rent_dummy).fit().centerCrop().into(imageView);

        textViewsize.setText("Size: " + uploadCurr.getmSize() + " sq. ft");
        textViewlandmark.setText("Landmark: " + uploadCurr.getmlandmark());
        textViewRent.setText("Price: " + uploadCurr.getmPrice() + "$");

        textViewType.setText("Type: " + uploadCurr.getPropertyType());


        return listViewItem;
    }

}
