package eu.nets.pia.sample.network.model;

/**
 * @author sschi
 */
public enum ProcessingOption {

    COMMIT("COMMIT"),
    VERIFY("VERIFY");

    String option;

    ProcessingOption(String option) {
        this.option = option;
    }


    @Override
    public String toString() {
        return option;
    }
}
