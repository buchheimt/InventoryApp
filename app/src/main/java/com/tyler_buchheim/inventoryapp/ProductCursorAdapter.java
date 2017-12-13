package com.tyler_buchheim.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tyler_buchheim.inventoryapp.data.ProductContract.ProductEntry;

import java.net.URI;
import java.util.Locale;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //TODO Add price, quantity, and picture to list item xml and implement here

        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        int id = cursor.getInt(idColumnIndex);
        String name = cursor.getString(nameColumnIndex);
        final double price = cursor.getDouble(priceColumnIndex) / 100;
        String priceString = "$" + String.format(Locale.US, "%.2f", price);
        final int quantity = cursor.getInt(quantityColumnIndex);
        String quantityString = "x " + String.valueOf(quantity);

        nameTextView.setText(name);
        priceTextView.setText(priceString);
        quantityTextView.setText(quantityString);

        final Uri uri = Uri.parse(ProductEntry.CONTENT_URI + "/" + id);
        Button saleBtn = view.findViewById(R.id.sale);
        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);
                    int rowsAffected = context.getContentResolver().update(uri, values, null, null);
                    if (rowsAffected > 0) {
                        context.getContentResolver().notifyChange(uri, null);
                        Toast.makeText(context, context.getString(R.string.sale_success),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.sale_error),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.sale_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
