package me.miliblue.rintaro;

public interface PluginBase {
    /**This method will be auto invoke by me.miliblue.rintaro loader**/
    void init();
    /**This field must be set.**/
    String name();
    /**This field must be set.**/
    String version();
}
