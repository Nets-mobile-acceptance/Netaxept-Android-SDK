package eu.nets.pia.sample.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author sschi
 */
public class ProcessingOptionRequest {

    public ProcessingOptionRequest(ProcessingOption processingOption) {
        this.processingOption = processingOption.toString();
    }

    @SerializedName("operation")
    private String processingOption;

    public String getProcessingOption() {
        return processingOption;
    }
}
