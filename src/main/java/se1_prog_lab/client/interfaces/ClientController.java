package se1_prog_lab.client.interfaces;

public interface ClientController {
    void start();

    void login(String username, String password);

    void register(String username, String password);
}
