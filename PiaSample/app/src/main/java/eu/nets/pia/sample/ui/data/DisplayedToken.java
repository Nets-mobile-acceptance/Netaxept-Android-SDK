package eu.nets.pia.sample.ui.data;

import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.fragment.PaymentMethodsFragment;
import eu.nets.pia.utils.StringUtils;

/**
 * MIT License
 * <p>
 * Copyright (c) 2020 Nets Denmark A/S
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

public class DisplayedToken extends PaymentMethod {

    private String tokenId;
    private String issuer;
    //date pattern assumed mm/yy
    private String expiryDate;
    private Boolean cardVerificationRequired;

    public boolean getCardVerificationRequired() {
        return cardVerificationRequired.booleanValue();
    }

    public void setCardVerificationRequired(Boolean cardVerificationRequired) {
        this.cardVerificationRequired = cardVerificationRequired;
    }

    public String getIssuer() {
        if (issuer.toLowerCase().equals(PaymentMethodsFragment.ID_DINERS.toLowerCase())) {
            return "Diners Club"; //return just "Diners Club" since "DinersClubInternational" is too large
        }
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMaskedPan() {
        return "•••• " + StringUtils.safeSubString(tokenId, tokenId.length() - 4, tokenId.length());
    }

    public int getCardSchemeLogo() {
        switch (issuer) {
            case PaymentMethodsFragment.ID_VISA:
                return R.drawable.pia_visa;
            case PaymentMethodsFragment.ID_MASTERCARD:
                return R.drawable.pia_master_card;
            case PaymentMethodsFragment.ID_AMERICAN_EXPRESS:
                return R.drawable.pia_american_express;
            case PaymentMethodsFragment.ID_DANKORT:
                return R.drawable.pia_dan_kort;
            case PaymentMethodsFragment.ID_JCB:
                return R.drawable.pia_jcb;
            case PaymentMethodsFragment.ID_MAESTRO:
                return R.drawable.pia_maestro_icon;
            case PaymentMethodsFragment.ID_DINERS:
                return R.drawable.pia_diners;
            default:
                return R.drawable.pia_visa;
        }
    }
}
