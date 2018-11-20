package eu.nets.pia.sample.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * MIT License
 * <p>
 * Copyright (c) 2018 Nets Denmark A/S
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

public class CustomToolbar extends LinearLayout {

    //region Declarations

    // Title
    private LinearLayout mTextTitleContainer;
    private TextView mTitle;

    // Right View
    private RelativeLayout mRightViewContainer;
    private View mRightView;

    // Left View
    private RelativeLayout mLeftViewContainer;
    private View mLeftView;
    //endregion



    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        customize();
    }

    private void customize() {
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.WHITE);

        setupLeftViewContainer();
        setupTitleContainer();
        setupRightViewContainer();
    }

    //region Title
    private void setupTitleContainer() {

        // Init Title / Subtitle Text Container
        LayoutParams textTitlesContainerParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        textTitlesContainerParams.weight = 4;

        mTextTitleContainer = new LinearLayout(getContext());
        mTextTitleContainer.setOrientation(LinearLayout.VERTICAL);
        mTextTitleContainer.setLayoutParams(textTitlesContainerParams);

        mTitle = new TextView(getContext());
        mTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mTitle.setTextSize(18f);
        mTitle.setTextColor(Color.BLACK);

        mTextTitleContainer.addView(mTitle);

        setMaxLength(mTitle, 30);
        // End Title Text Container

        addView(mTextTitleContainer);
    }

    public void setTitle(int resId) {
        mTextTitleContainer.setVisibility(VISIBLE);
        mTitle.setText(resId);
    }

    public void setTitle(CharSequence title) {
        mTextTitleContainer.setVisibility(VISIBLE);
        mTitle.setText(title);
    }

    public void setTitleTextColor(int color) {
        mTitle.setTextColor(color);
    }

    //endregion

    //region Right View
    private void setupRightViewContainer() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.rightMargin = 20;
        params.gravity = Gravity.CENTER;
        mRightViewContainer = new RelativeLayout(getContext());
        mRightViewContainer.setLayoutParams(params);

        addView(mRightViewContainer);
    }

    public void setupRightView(View view) {
        mRightView = view;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.rightMargin = 10;
        mRightView.setLayoutParams(params);

        mRightViewContainer.removeAllViews();
        mRightViewContainer.addView(mRightView);
    }

    public void removeRightView() {
        mRightViewContainer.removeAllViews();
    }
    //endregion

    //region Left View
    private void setupLeftViewContainer() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.leftMargin = 20;
        params.gravity = Gravity.CENTER;
        mLeftViewContainer = new RelativeLayout(getContext());
        mLeftViewContainer.setLayoutParams(params);

        addView(mLeftViewContainer);
    }

    public void setLeftView(View view) {
        mLeftView = view;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.height = 100;
        params.width = 100;
        mLeftView.setLayoutParams(params);

        view.setPadding(10,10,10,10);

        mLeftViewContainer.removeAllViews();
        mLeftViewContainer.addView(view);
    }

    public void removeLeftView() {
        mLeftViewContainer.removeAllViews();
    }
    //endregion

    //region Utilities
    private void setMaxLength(TextView view, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        view.setFilters(FilterArray);
    }
    //endregion

}
