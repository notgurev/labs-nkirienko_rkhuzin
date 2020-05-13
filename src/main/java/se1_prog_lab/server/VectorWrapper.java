package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import static se1_prog_lab.util.BetterStrings.coloredYellow;
import static se1_prog_lab.util.BetterStrings.multiline;

/**
 * Принципиально данный класс должен только управлять коллекцией, но не оповещать ни о чем пользователя напрямую
 * Если нет туду, то я почти уверен, что метод уже сделан верно и работает.
 * TODO сделать нормальное назначение ID средствами БД.
 */
@Singleton
public class VectorWrapper implements CollectionWrapper {
    private final LocalDate initDate;
    private final DatabaseManager databaseManager;
    private Vector<LabWork> labWorks = new Vector<>();

    @Inject
    public VectorWrapper(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        initDate = LocalDate.now();
    }

    /**
     * TODO надо узнать, оставить это как есть или возвращать дату создания БД.
     *
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
     * TODO должен удалять только те элементы, на которые у юзера есть права.
     */
    public void clear() {
        databaseManager.clear();
    }

    /**
     * Добавляет элемент в коллекцию.
     * TODO проверить?
     *
     * @param labWork добавлямый элемент.
     * @return true если успешно
     */
    public boolean add(LabWork labWork) {
        Long result = databaseManager.addElement(labWork);
        if (result != null) {
            labWork.preSetId(result);
            labWorks.add(labWork);
            return true;
        }
        return false;
    }

    /**
     * (для команды insert_at)
     * TODO Понятия не имею, как это должно работать в БД (есть ли там индексы?). Ну и надо переделать.
     * Добавляет элемент в указанную позицию.
     *
     * @param labWork новый элемент
     * @param index   позиция
     * @return true если успешно
     */
    public boolean insertAt(LabWork labWork, int index) {
        return databaseManager.addElementToIndex(labWork, index);
    }

    /**
     * Аналог showAll, но с фильтром: только элементы, значение поля minimalPoint которых больше заданного.
     *
     * @param minimalPoint значение поля для фильтра.
     * @return все строки содержимого всех элементов, попадающих под условие.
     */
    public String filterGreaterThanMinimalPoint(Integer minimalPoint) {
        return multiline(labWorks.stream()
                .filter(labWork -> (labWork.getMinimalPoint() != null && labWork.getMinimalPoint() > minimalPoint))
                .map(LabWork::toString).toArray());
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
     * TODO код старый. Нужно поменять тело и случаи применения
     * TODO (упомянуть там о правах, мб придется даже менять возвращаемый тип)
     *
     * @return true, если успешно; false, если нет (например, коллекция пуста или нет прав на все)
     */
    public boolean sort() {
        if (labWorks.isEmpty()) return false;
        else return databaseManager.sortById();
    }

    /**
     * Удаляет элемент по его id.
     * TODO код старый, надо сделать через БД.
     *
     * @param id id элемента
     * @return true, если успешно; false, если нет (например, элемента с таким id нет)
     */
    public boolean removeByID(long id) {
        if (databaseManager.removeById(id)) {
            labWorks.removeIf((labwork) -> labwork.getId() == id);
            return true;
        }
        return false;
    }

    /**
     * (Для команды Update id)
     * Заменяет элемент с данным id новым.
     * TODO код старый. Нужно менять. Либо сделать вызов removeElement(id) и addElement(newLabWork), либо как-то ещё.
     * TODO тут надо аккуратнее с назначением id.
     *
     * @param id         id старого элемента.
     * @param newLabWork объект нового элемента.
     * @return true, если успешно; false, если нет (например, элемента с таким id нет)
     */
    public boolean updateByID(long id, LabWork newLabWork) {
        if (databaseManager.updateById(newLabWork, id)) {
            newLabWork.preSetId(id);
            labWorks.set(labWorks.indexOf(getByID(id)), newLabWork);
            return true;
        }
        return false;
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
        return labWorks.stream().filter(labWork -> labWork.getDescription().compareTo(description) < 0).count();
    }

    /**
     * @return все строки содержимого всех элементов коллекции или сообщение, что она пуста.
     */
    public String showAll() {
        if (labWorks.isEmpty()) {
            return coloredYellow("Коллекция пуста!");
        } else {
            return multiline(labWorks.stream().filter(Objects::nonNull).map(LabWork::toString).toArray());
        }
    }

    /**
     * Проверяет, пуста ли коллеция
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
