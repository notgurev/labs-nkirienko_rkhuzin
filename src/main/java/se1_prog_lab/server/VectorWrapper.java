package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static se1_prog_lab.util.BetterStrings.coloredYellow;
import static se1_prog_lab.util.BetterStrings.multiline;

/**
 * Принципиально данный класс должен только управлять коллекцией, но не оповещать ни о чем пользователя напрямую
 * Если нет туду, то я почти уверен, что метод уже сделан верно и работает.
 * TODO не уверен, нужно ли заново загружать коллекцию из БД каждый раз.
 * TODO сделать нормальное назначение ID средствами БД.
 */
@Singleton
public class VectorWrapper implements CollectionWrapper {
    private final LocalDate initDate;
    private final HashSet<Long> idSet = new HashSet<>();
    private final Comparator<LabWork> labWorkComparator = Comparator.naturalOrder();
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
        labWorks = databaseManager.loadCollectionFromDatabase();
    }

    /**
     * Назначает id элементу, если он его не имеет, либо элемент с таким id уже есть в коллекции.
     * TODO: не факт, что нужен вообще.
     *
     * @param labWork элемент, которому назначается id.
     */
    private void assignId(LabWork labWork) {
        if (labWork.getId() == null || idSet.contains(labWork.getId())) {
            for (long i = 0L; i <= idSet.size(); i++) {
                if (idSet.add(i)) {
                    labWork.safeSetId(i);
                    break;
                }
            }
        } else {
            idSet.add(labWork.getId());
        }
    }

    /**
     * Добавляет элемент в коллекцию.
     * TODO через БД. Код условный.
     * TODO назначение ID.
     *
     * @param labWork добавлямый элемент.
     * @return id элемента (назначается сам)
     */
    public long add(LabWork labWork) {
        assignId(labWork); //TODO refactor
        databaseManager.addElement(labWork);
        databaseManager.loadCollectionFromDatabase();
        return labWork.getId();
    }

    /**
     * (для команды insert_at)
     * TODO Понятия не имею, как это должно работать в БД (есть ли там индексы?). Ну и надо переделать.
     * Добавляет элемент в указанную позицию.
     *
     * @param labWork новый элемент
     * @param index   позиция
     * @return id нового элемента (TODO вещь косметическая, можно сделать void, если проблемы создает, но тогда поменять в контексте)
     */
    // TODO должна изменять БД + как есть ли там вообще индекс??? (insert_at)
    public long addToPosition(LabWork labWork, int index) {
        assignId(labWork);
        labWorks.setSize(index);
        labWorks.add(index, labWork);
        return labWork.getId();
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
     * @return true, если успешно; false, если нет (например, коллекция пуста)
     */
    public boolean sort() {
        if (labWorks.isEmpty()) return false;
        labWorks.sort(labWorkComparator);
        return true;
    }

    /**
     * Удаляет элемент по его id.
     * TODO код старый, надо сделать через БД.
     *
     * @param id id элемента
     * @return true, если успешно; false, если нет (например, элемента с таким id нет)
     */
    public boolean removeByID(long id) {
        if (getByID(id) != null) {
            labWorks.remove(getByID(id));
            return true;
        } else return false;
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
    public boolean replaceByID(long id, LabWork newLabWork) {
        if (getByID(id) != null) {
            LabWork oldLabWork = getByID(id);
            newLabWork.safeSetId(id);
            labWorks.set(labWorks.indexOf(oldLabWork), newLabWork);
            return true;
        } else return false;
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
     * Получает элемент по значению его id.
     * TODO был нужен для removeByID и replaceByID, так как удаление было по объекту. Думаю, можно обойтись без него.
     *
     * @param id id получаемого объекта.
     * @return объект с данным id / null, если такого нет.
     */
    public LabWork getByID(Long id) {
        return labWorks.stream().filter(labWork -> labWork.getId().equals(id)).findAny().orElse(null);
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
}
