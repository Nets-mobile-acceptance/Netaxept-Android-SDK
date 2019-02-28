package eu.nets.pia.sample.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eu.nets.pia.sample.R;

/**
 * MIT License
 * <p>
 * Copyright (c) 2019 Nets Denmark A/S
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy  of this software
 * and associated documentation files (the "Software"), to deal  in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is  furnished to do so,
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class LanguageAdapter extends ArrayAdapter<String> {

    private List<String> mItems;


    private class ViewHolder {
        private TextView spinnerItem;
    }


    private Context context;

    public LanguageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public LanguageAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.context = context;
        this.mItems = items;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        ViewHolder viewHolder;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.spinner_language_preview_item, null);

            viewHolder = new ViewHolder();

            viewHolder.spinnerItem = (TextView) v.findViewById(R.id.language_preview);
            viewHolder.spinnerItem.setGravity(Gravity.LEFT);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        final String p = getItem(position);

        if (p != null) {

            if (viewHolder.spinnerItem != null) {
                viewHolder.spinnerItem.setText(p);
                viewHolder.spinnerItem.setGravity(Gravity.CENTER);
            }

            v.setTag(viewHolder);
        }
        return v;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ViewHolder viewHolder;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.spinner_dropdown_item, null);

            viewHolder = new ViewHolder();

            viewHolder.spinnerItem = (TextView) v.findViewById(R.id.currency_item);
            viewHolder.spinnerItem.setGravity(Gravity.CENTER);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        final String p = getItem(position);

        if (p != null) {

            if (viewHolder.spinnerItem != null) {
                viewHolder.spinnerItem.setText(p);
                viewHolder.spinnerItem.setGravity(Gravity.CENTER);
            }

            v.setTag(viewHolder);
        }

        return v;
    }

    public int getPositionForItem(String id) {
        if (mItems == null) {
            return 0;
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).equals(id)) {
                return i;
            }
        }
        return 0;
    }
}
