package me.miliblue.rintaro;

public class CutsomException extends Exception{

    public CutsomException(String msg) {
        super("[Rintaro Loader Exception] "+msg);
    }
}
