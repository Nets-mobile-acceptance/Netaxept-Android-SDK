package eu.nets.pia.sample.ui.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.network.model.Method;
import eu.nets.pia.sample.ui.data.DisplayedToken;
import eu.nets.pia.sample.ui.data.PaymentMethod;
import eu.nets.pia.sample.ui.data.PaymentMethodType;
import eu.nets.pia.sample.ui.fragment.FragmentCallback;

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

public class PaymentMethodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_NEW_CARD = 0;
    private final int VIEW_TOKEN_CARD = 1;
    private final int VIEW_HEADER = 2;
    private final int VIEW_OTHER_PAYMENT = 3;

    private ArrayList<PaymentMethod> items = new ArrayList<>();
    private ArrayList<Method> supportedCardSchemes = new ArrayList<Method>();
    private FragmentCallback mCallback;
    private int idGenerator = 1;
    private int screenWidth = 0;
    private Context context;

    public PaymentMethodsAdapter(Context context, ArrayList<PaymentMethod> items, ArrayList<Method> supportedCardSchemes, FragmentCallback mCallback) {
        this.items = items;
        this.supportedCardSchemes = supportedCardSchemes;
        this.mCallback = mCallback;
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_header, parent, false));
            case VIEW_TOKEN_CARD:
                return new TokenMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_item_token, parent, false));
            case VIEW_NEW_CARD:
                return new NewCardMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_item_new_card, parent, false));
            case VIEW_OTHER_PAYMENT:
                return new OtherMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_item, parent, false));
            default:
                return new OtherMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TOKEN_CARD) {
            ((TokenMethodViewHolder) holder).setupTokenCardHolder((DisplayedToken) items.get(position));
        } else if (getItemViewType(position) == VIEW_OTHER_PAYMENT) {
            ((OtherMethodViewHolder) holder).setupOtherPaymentHolder(items.get(position));
        } else if (getItemViewType(position) == VIEW_NEW_CARD) {
            ((NewCardMethodViewHolder) holder).setupNewCardHolder(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TokenMethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_scheme_logo)
        ImageView mCardLogo;
        @BindView(R.id.card_scheme_digits)
        TextView mCardSchemeDigits;


        public TokenMethodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setupTokenCardHolder(final DisplayedToken method) {
            mCardLogo.setBackground(ContextCompat.getDrawable(itemView.getContext(), method.getCardSchemeLogo()));
            mCardSchemeDigits.setText(String.format("%1$s %2$s", method.getIssuer(), method.getMaskedPan()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onPaymentMethodSelected(method);
                    }
                }
            });
        }
    }

    public class NewCardMethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.supported_schemes_layout)
        LinearLayout mSupportedSchemesLayout;

        //values in DP
        final int standardImageViewWidth = 55;
        final int standardImageViewHeight = 40;
        final int standardImageViewPadding = 10;

        public NewCardMethodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setupNewCardHolder(final PaymentMethod method) {
            mSupportedSchemesLayout.removeAllViews();

            boolean isScaleRequired = isScaleRequired(convertDpToPx(standardImageViewWidth + standardImageViewPadding));

            if (supportedCardSchemes != null) {
                for (Method cardScheme : supportedCardSchemes) {
                    //inflate imageview and set logo
                    LinearLayout schemeItem = (LinearLayout) LayoutInflater.from(itemView.getContext())
                            .inflate(R.layout.supported_scheme_imageview, null, false);
                    ImageView logo = schemeItem.findViewById(R.id.scheme);
                    logo.setId(generateViewId());

                    if (isScaleRequired) {
                        //inflate undefined scheme size layout -- and set it here based on the screen width
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logo.getLayoutParams();
                        if (lp != null) {
                            int preferredWidth = getPreferredWidth();
                            float factor = (float) preferredWidth / (float) standardImageViewWidth;
                            lp.width = convertDpToPx(preferredWidth);
                            lp.height = convertDpToPx((standardImageViewHeight * factor));
                        }
                        logo.setBackground(ContextCompat.getDrawable(itemView.getContext(), cardScheme.getLogoResId()));
                    }
                    logo.setBackground(ContextCompat.getDrawable(itemView.getContext(), cardScheme.getLogoResId()));
                    mSupportedSchemesLayout.addView(schemeItem);
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onPaymentMethodSelected(method);
                    }
                }
            });
        }
    }

    private int convertDpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private int convertPxToDp(int px) {
        return (int) (px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Returns true if all the card icons don't fit the width of the screen
     *
     * @param placeHolderSize the width of the card scheme placeholder
     * @return flag to specify if image scale is required
     */
    private boolean isScaleRequired(int placeHolderSize) {
        return placeHolderSize * supportedCardSchemes.size() >= screenWidth;
    }

    /**
     * Calculates which width the image should have in order to fit all of them on the same line
     * It also takes into consideration:
     * -each imageView has a paddingEnd - 5dp
     * -the row also have an arrow icon on the right 30dp
     * -the parent row layout has a paddingStart = 8dp and paddingEnd = 8dp
     *
     * @return the width of the card scheme to be set
     */
    private int getPreferredWidth() {
        //5* supportedCardSchemes.size() : represents each icon padding end of 5dp
        //30 represents the arrow from the end of the list item
        //16 represent parent layout padding
        int imageViewPaddingEnd = 5;
        int parentLayoutPadding = 16;
        int parentLayoutArrowIconSize = 30;
        return (convertPxToDp(screenWidth) - (imageViewPaddingEnd * supportedCardSchemes.size()) - parentLayoutArrowIconSize - parentLayoutPadding) /
                supportedCardSchemes.size();
    }

    private int generateViewId() {
        //generated a unique view id for when the view is recycled
        return idGenerator++;
    }

    public class OtherMethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_scheme_logo)
        ImageView mCardLogo;

        public OtherMethodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setupOtherPaymentHolder(final PaymentMethod method) {
            switch (method.getType()) {
                case PAY_PAL:
                    mCardLogo.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.paypal));
                    break;
                case KLARNA:
                    mCardLogo.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.klarna));
                    break;
                case VIPPS:
                    mCardLogo.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.vipps));
                    break;
                case SWISH:
                    mCardLogo.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.swish));
                    break;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onPaymentMethodSelected(method);
                    }
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        //will automatically add textview with text "Other" in the list
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == null) {
            //if type is null then it's other payments header
            return VIEW_HEADER;
        } else if (items.get(position).getType() == PaymentMethodType.TOKEN) {
            return VIEW_TOKEN_CARD;
        } else if (items.get(position).getType() == PaymentMethodType.CREDIT_CARDS) {
            return VIEW_NEW_CARD;
        } else {
            return VIEW_OTHER_PAYMENT;
        }
    }
}
