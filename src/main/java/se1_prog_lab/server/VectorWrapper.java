package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.CollectionWrapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Класс, управляющий коллекцией в памяти (оболочка).
 */
@Singleton
public class VectorWrapper implements CollectionWrapper {
    private final LocalDate initDate;
    private Vector<LabWork> labWorks = new Vector<>();

    @Inject
    public VectorWrapper() {
        initDate = LocalDate.now();
    }

    /**
     * @return дату инициализации коллекции.
     */
    public LocalDate getInitDate() {
        return initDate;
    }

    /**
     * @return размер коллекции.
     */
    public int getSize() {
        return labWorks.size();
    }

    /**
     * @return тип коллекции.
     */
    public String getCollectionType() {
        return labWorks.getClass().getSimpleName();
    }

    /**
     * Очищает коллекцию.
     */
    public void clear(List<Long> ids) {
        labWorks.removeIf((LabWork labwork) -> ids.contains(labwork.getId()));
    }

    /**
     * Добавляет элемент в коллекцию.
     *
     * @param labWork добавляемый элемент.
     */
    public void add(LabWork labWork, long id) {
        labWork.setId(id);
        labWorks.add(labWork);
    }

    /**
     * (для команды insert_at)
     * Добавляет элемент в указанную позицию.
     *
     * @param labWork новый элемент
     * @param index   позиция
     * @param id      id
     */
    public void insertAtIndex(LabWork labWork, int index, long id) {
        labWork.setId(id);
        labWorks.setSize(index);
        labWorks.add(index, labWork);
    }

    /**
     * Аналог showAll, но с фильтром: только элементы, значение поля minimalPoint которых больше заданного.
     *
     * @param minimalPoint значение поля для фильтра.
     * @return все строки содержимого всех элементов, попадающих под условие.
     */
    public List<LabWork> filterGreaterThanMinimalPoint(Integer minimalPoint) {
        return labWorks.stream()
                .filter(labWork -> (labWork.getMinimalPoint() != null && labWork.getMinimalPoint() > minimalPoint))
                .collect(Collectors.toList());
    }


    /**
     * @return сет уникальных значений поля uniqueTunedInWorks
     */
    public Set<Integer> getUniqueTunedInWorks() {
        return labWorks.stream().map(LabWork::getTunedInWorks).collect(Collectors.toSet());
    }


    /**
     * Сортирует коллекцию по умолчанию.
     * Инфа от Миши: работает, только если у юзера есть права на ВООБЩЕ ВСЕ элементы коллекции.
     *
     * @return true, если успешно; false, если нет (например, коллекция пуста или нет прав на все)
     */
    public boolean sort() {
        if (labWorks.isEmpty()) return false;
        else {
            labWorks.sort(Comparator.naturalOrder());
            return true;
        }
    }

    /**
     * Удаляет элемент по его id.
     *
     * @param id id элемента
     */
    public void removeByID(long id) {
        labWorks.removeIf((labwork) -> labwork.getId() == id);
    }

    /**
     * (Для команды Update id)
     * Заменяет элемент с данным id новым.
     *
     * @param id         id старого элемента.
     * @param newLabWork объект нового элемента.
     */
    public void updateByID(long id, LabWork newLabWork) {
        newLabWork.setId(id);
        labWorks.set(labWorks.indexOf(getByID(id)), newLabWork);
    }

    @Override
    public Collection<LabWork> getCollectionSlice(int firstIndex, int lastIndex) {
        return IntStream.rangeClosed(firstIndex, lastIndex).mapToObj(index -> labWorks.get(index)).collect(Collectors.toList());
    }

    @Override
    public void setVector(Vector<LabWork> labWorkVector) {
        labWorks = labWorkVector;
    }

    /**
     * Подсчитывает элементы, значение поля "описание" которых меньше заданного.
     *
     * @param description описание
     * @return количество таких элементов
     */
    public long countLessThanDescription(String description) {
        return labWorks.stream().filter(Objects::nonNull).filter(labWork -> labWork.getDescription().compareTo(description) < 0).count();
    }

    /**
     * @return List всех элементов коллекции или сообщение, что она пуста.
     */
    public List<LabWork> getListOfAllElements() {
        return labWorks.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Проверяет, пуста ли коллекция
     *
     * @return true, если пуста; false, если в ней есть элементы.
     */
    public boolean isEmpty() {
        return labWorks.isEmpty();
    }

    public LabWork getByID(Long id) {
        return labWorks.stream().filter(labWork -> labWork.getId().equals(id)).findAny().orElse(null);
    }
}
