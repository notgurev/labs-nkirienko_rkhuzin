package se1_prog_lab.collection;

import se1_prog_lab.exceptions.LabWorkFieldException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;

import static se1_prog_lab.util.BetterStrings.blueStringIfNull;
import static se1_prog_lab.util.BetterStrings.multiline;


/**
 * Класс лабораторной работы (элемента коллекции).
 */
public class LabWork implements Comparable<LabWork>, Serializable {
    /**
     * Количество полей, чтобы скрипт знал, сколько пропускать строк.
     */
    private static final int NUMBER_OF_FIELDS = 14;
    @Positive // без NotNull т.к. сервер сам назначает ID
    private Long id; // > 0, unique, auto-gen, not null

    @NotEmpty
    @NotNull
    private String name;
    @NotNull
    private Coordinates coordinates;
    @NotNull
    private LocalDateTime creationDate; // auto-gen
    @Positive
    private Integer minimalPoint;
    @NotNull
    @NotEmpty
    private String description;
    private Integer tunedInWorks;
    @NotNull
    private Difficulty difficulty;
    private final Person author;

    public LabWork() {
        author = new Person();
    }

    public LabWork(String name, Coordinates coordinates, Integer minimalPoint, String description, Integer tunedInWorks, Difficulty difficulty, Person author) {
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.tunedInWorks = tunedInWorks;
        this.difficulty = difficulty;
        this.author = author;
    }

    public static int getNumberOfFields() {
        return NUMBER_OF_FIELDS;
    }

    @Override
    public String toString() {
        return multiline(
                "ID: " + id,
                "Имя: " + name,
                coordinates.toString(),
                "Дата создания: " + creationDate.withNano(0),
                "minimalPoint: " + blueStringIfNull(minimalPoint),
                "Описание: " + description,
                "tunedInWorks: " + blueStringIfNull(tunedInWorks),
                "Сложность: " + difficulty.name(),
                author.toString() + '\n'
        );
    }

    /**
     * "Безопасная" установка id, когда мы знаем, что больше элементов с таким id в коллекции нет.
     *
     * @param id устанавливаемый id.
     */
    public void safeSetId(Long id) {
        this.id = id;
    }

    /**
     * "Предустановка" id при чтении из CSV, когда неясно, есть ли коллизии с другими id.
     *
     * @param id устанавливаемый id.
     * @throws LabWorkFieldException если id не соответствует ограничениям (для скрипта).
     */
    public void preSetId(Long id) throws LabWorkFieldException {
        if (id == null || id < 0) throw new LabWorkFieldException();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws LabWorkFieldException {
        if (description == null || description.equals("")) throw new LabWorkFieldException();
        this.description = description;
    }

    public Integer getTunedInWorks() {
        return tunedInWorks;
    }

    public void setTunedInWorks(Integer tunedInWorks) {
        // Нет условий
        this.tunedInWorks = tunedInWorks;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Integer minimalPoint) throws LabWorkFieldException {
        if (minimalPoint != null && minimalPoint <= 0) throw new LabWorkFieldException();
        this.minimalPoint = minimalPoint;
    }

    public void setName(String name) throws LabWorkFieldException {
        if (name == null || name.equals("")) throw new LabWorkFieldException();
        this.name = name;
    }

    public void setCoordinates(long x, Float y) throws LabWorkFieldException {
        if (y == null || x > 625) throw new LabWorkFieldException();
        coordinates = new Coordinates();
        this.coordinates.setX(x);
        this.coordinates.setY(y);
    }

    public void setDifficulty(Difficulty difficulty) throws LabWorkFieldException {
        if (difficulty == null) throw new LabWorkFieldException();
        this.difficulty = difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    /**
     * Для сортировки по умолчанию.
     *
     * @param o с чем сравниваем.
     * @return разница в id.
     */
    @Override
    public int compareTo(LabWork o) {
        return (int) (id - o.getId());
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
