package eu.nets.pia.sample.network.model;

import com.google.gson.annotations.SerializedName;

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

public class ErrorResponse {

    public class Params {
        @SerializedName("message")
        private String message = null;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Params {\n");

            sb.append("  message: ").append(message).append("\n");
            sb.append("}");
            return sb.toString();
        }
    }

    @SerializedName("explanationText")
    private String explanationText = null;
    @SerializedName("params")
    private Params params = null;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ErrorResponse{");
        sb.append("explanationText='").append(explanationText).append("'");
        sb.append(",params='").append(params).append("'");
        sb.append("}");
        return sb.toString();
    }

    public String getExplanationText() {
        return explanationText;
    }

    public void setExplanationText(String explanationText) {
        this.explanationText = explanationText;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}
