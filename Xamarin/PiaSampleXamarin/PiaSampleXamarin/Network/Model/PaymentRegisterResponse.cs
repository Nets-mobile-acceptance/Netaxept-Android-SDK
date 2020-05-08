using System;
using System.Text;
using Newtonsoft.Json;

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

namespace PiaSampleXamarin.Model
{
    class PaymentRegisterResponse
    {

        [JsonProperty(PropertyName = "transactionId")]
        public String transactionId { get; set; }

        [JsonProperty(PropertyName = "redirectOK")]
        public String redirectOK { get; set; }

        [JsonProperty(PropertyName = "redirectCancel")]
        public String redirectCancel { get; set; }

        [JsonProperty(PropertyName = "walletUrl")]
        public String walletUrl { get; set; }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("PaymentRegisterResponse{");
            sb.Append("transactionId='").Append(transactionId).Append("'");
            sb.Append(",redirectOK='").Append(redirectOK).Append("'");
            sb.Append(",redirectCancel='").Append(redirectCancel).Append("'");
            sb.Append(",walletUrl='").Append(walletUrl).Append("'");
            sb.Append("}");
            return sb.ToString();
        }

    }
}