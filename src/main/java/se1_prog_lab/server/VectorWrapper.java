package se1_prog_lab.server;

import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.exceptions.NoElementWithSuchIdException;
import se1_prog_lab.server.interfaces.CollectionWrapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static se1_prog_lab.util.BetterStrings.coloredYellow;
import static se1_prog_lab.util.BetterStrings.multiline;

@Singleton
public class VectorWrapper implements CollectionWrapper {
    private final Vector<LabWork> labWorks = new Vector<>();
    private final LocalDate initDate;
    private final HashSet<Long> idSet = new HashSet<>();
    private final Comparator<LabWork> labWorkComparator = Comparator.naturalOrder();

    public VectorWrapper() {
        initDate = LocalDate.now();
    }

    // CORRECT
    public LocalDate getInitDate() {
        return initDate;
    }

    // CORRECT
    public int getSize() {
        return labWorks.size();
    }

    // CORRECT
    public String getCollectionType() {
        return labWorks.getClass().getSimpleName();
    }

    // TODO разобраться с правами доступа: удалять только своё?
    public void clear() {
        labWorks.clear();
    }

    // TODO переделать по ТЗ
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

    // TODO должна изменять БД
    public long add(LabWork labWork) {
        assignId(labWork);
        labWorks.add(labWork);
        return labWork.getId();
    }

    // TODO должна изменять БД + как есть ли там вообще индекс??? (insert_at)
    public long addToPosition(LabWork labWork, int index) {
        assignId(labWork);
        labWorks.setSize(index);
        labWorks.add(index, labWork);
        return labWork.getId();
    }

    // CORRECT
    public String filterGreaterThanMinimalPoint(Integer minimalPoint) {
        return multiline(labWorks.stream()
                .filter(labWork -> (labWork.getMinimalPoint() != null && labWork.getMinimalPoint() > minimalPoint))
                .map(LabWork::toString).toArray());
    }

    // CORRECT
    public Set<Integer> getUniqueTunedInWorks() {
        return labWorks.stream().map(LabWork::getTunedInWorks).collect(Collectors.toSet());
    }

    // TODO как влияет сортировка на базу, нужно ли обновлять
    public boolean sort() {
        if (labWorks.isEmpty()) return false;
        labWorks.sort(labWorkComparator);
        return true;
    }

    // TODO должна изменять БД
    public void removeByID(long id) throws NoElementWithSuchIdException {
        if (getByID(id) != null) {
            labWorks.remove(getByID(id));
        } else throw new NoElementWithSuchIdException();
    }

    // TODO должна изменять БД
    public boolean replaceByID(long id, LabWork newLabWork) {
        if (getByID(id) != null) {
            LabWork oldLabWork = getByID(id);
            newLabWork.safeSetId(id);
            labWorks.set(labWorks.indexOf(oldLabWork), newLabWork);
            return true;
        } else return false;
    }

    // CORRECT
    public long countLessThanDescription(String description) {
        return labWorks.stream().filter(labWork -> labWork.getDescription().compareTo(description) < 0).count();
    }

    // CORRECT
    public LabWork getByID(Long id) {
        return labWorks.stream().filter(labWork -> labWork.getId().equals(id)).findAny().orElse(null);
    }

    // CORRECT
    public String showAll() {
        if (labWorks.isEmpty()) {
            return coloredYellow("Коллекция пуста!");
        } else {
            return multiline(labWorks.stream().filter(Objects::nonNull).map(LabWork::toString).toArray());
        }
    }

    // CORRECT
    public boolean isEmpty() {
        return labWorks.isEmpty();
    }
}
