package se1_prog_lab.collection;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Класс лабораторной работы (элемента коллекции)
 */
public class LabWork implements Comparable<LabWork>, Serializable {
    private final Person author;
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

    public LabWork() {
        author = new Person();
    }

    public LabWork(String name, Coordinates coordinates, Integer minimalPoint, String description,
                   Integer tunedInWorks, Difficulty difficulty, Person author) {
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.tunedInWorks = tunedInWorks;
        this.difficulty = difficulty;
        this.author = author;
    }

    public String[] toArray() {
        return new String[]{
                id.toString(),
                name,
                Long.toString(coordinates.getX()),
                Float.toString(coordinates.getY()),
                creationDate.toString(),
                minimalPoint.toString(),
                description,
                tunedInWorks.toString(),
                difficulty.name(),
                author.getName(),
                author.getHeight().toString(),
                author.getPassportID(),
                author.getHairColor().toString(),
                author.getLocation().getX().toString(),
                Float.toString(author.getLocation().getY()),
                author.getLocation().getZ().toString(),
        };
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "author=" + author +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ", description='" + description + '\'' +
                ", tunedInWorks=" + tunedInWorks +
                ", difficulty=" + difficulty +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTunedInWorks() {
        return tunedInWorks;
    }

    public void setTunedInWorks(Integer tunedInWorks) {
        this.tunedInWorks = tunedInWorks;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Integer minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public void setCoordinates(long x, Float y) {
        coordinates = new Coordinates();
        this.coordinates.setX(x);
        this.coordinates.setY(y);
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

    public LabWorkParams toParams() {
        return new LabWorkParams(
                name,
                coordinates.getX(),
                coordinates.getY(),
                minimalPoint,
                description,
                tunedInWorks,
                difficulty,
                author.getName(),
                author.getHeight(),
                author.getPassportID(),
                author.getHairColor(),
                author.getLocation().getX(),
                author.getLocation().getY(),
                author.getLocation().getZ()
        );
    }
}
