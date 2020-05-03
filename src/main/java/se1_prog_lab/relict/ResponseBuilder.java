package se1_prog_lab.relict;

public interface ResponseBuilder {
    void addLineToResponse(String s);

    String getResponse();

    void clearResponse();
}
