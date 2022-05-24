package dev.ogabek.java.manager;

public interface AuthHandler {

    public void onSuccess();
    public void onError(Exception exception);

}
