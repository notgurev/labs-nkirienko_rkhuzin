package se1_prog_lab.client;

public interface ModelListener {
    void addElement(Object[] fields);
    void updateElement(Long id, Object[] fields);
    void removeElement(Long id);
    void update();
}
