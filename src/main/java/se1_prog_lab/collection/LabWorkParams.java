package se1_prog_lab.collection;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class LabWorkParams {
    @Positive
    private Long id;
    private String name;
    private long coordinateX;
    private Float coordinateY;
    @NotNull
    private LocalDateTime creationDate;
    private Integer minimalPoint;
    private String description;
    private Integer tunedInWorks;
    @NotNull
    private Difficulty difficulty;
    @NotNull
    private String authorName;
    @Positive
    private Float authorHeight;
    @Length(min = 9)
    @NotNull
    private String authorPassportID;
    private Color authorHairColor;
    @NotNull
    private Integer authorLocationX;
    @NotNull
    private float authorLocationY;
    @NotNull
    private Integer authorLocationZ;

    public String getName() {
        return name;
    }

    public long getCoordinateX() {
        return coordinateX;
    }

    public Float getCoordinateY() {
        return coordinateY;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTunedInWorks() {
        return tunedInWorks;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Float getAuthorHeight() {
        return authorHeight;
    }

    public String getAuthorPassportID() {
        return authorPassportID;
    }

    public Color getAuthorHairColor() {
        return authorHairColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinateX(long coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setCoordinateY(Float coordinateY) {
        this.coordinateY = coordinateY;
    }

    public void setMinimalPoint(Integer minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTunedInWorks(Integer tunedInWorks) {
        this.tunedInWorks = tunedInWorks;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorHeight(Float authorHeight) {
        this.authorHeight = authorHeight;
    }

    public void setAuthorPassportID(String authorPassportID) {
        this.authorPassportID = authorPassportID;
    }

    public void setAuthorHairColor(Color authorHairColor) {
        this.authorHairColor = authorHairColor;
    }

    public Integer getAuthorLocationX() {
        return authorLocationX;
    }

    public float getAuthorLocationY() {
        return authorLocationY;
    }

    public Integer getAuthorLocationZ() {
        return authorLocationZ;
    }

    public void setAuthorLocationX(Integer authorLocationX) {
        this.authorLocationX = authorLocationX;
    }

    public void setAuthorLocationY(float authorLocationY) {
        this.authorLocationY = authorLocationY;
    }

    public void setAuthorLocationZ(Integer authorLocationZ) {
        this.authorLocationZ = authorLocationZ;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
