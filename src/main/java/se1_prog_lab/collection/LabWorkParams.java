package se1_prog_lab.collection;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class LabWorkParams {
    @Positive
    private Long id;
    @NotEmpty
    @NotNull
    private String name;
    @Max(value = 625)
    private long coordinateX;
    @NotNull
    private Float coordinateY;
    @NotNull
    private LocalDateTime creationDate;
    @Positive
    private Integer minimalPoint;
    @NotNull
    @NotEmpty
    private String description;
    private Integer tunedInWorks;
    @NotNull
    private Difficulty difficulty;
    // Person
    @NotNull
    @NotEmpty
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
    private String owner = "DefaultOwner";

    public LabWorkParams() {
    }

    public LabWorkParams(String name, long coordinateX, Float coordinateY,
                         Integer minimalPoint, String description, Integer tunedInWorks,
                         Difficulty difficulty, String authorName, Float authorHeight,
                         String authorPassportID, Color authorHairColor, Integer authorLocationX,
                         float authorLocationY, Integer authorLocationZ) {
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.creationDate = LocalDateTime.now();
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.tunedInWorks = tunedInWorks;
        this.difficulty = difficulty;
        this.authorName = authorName;
        this.authorHeight = authorHeight;
        this.authorPassportID = authorPassportID;
        this.authorHairColor = authorHairColor;
        this.authorLocationX = authorLocationX;
        this.authorLocationY = authorLocationY;
        this.authorLocationZ = authorLocationZ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(long coordinateX) {
        this.coordinateX = coordinateX;
    }

    public Float getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(Float coordinateY) {
        this.coordinateY = coordinateY;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Integer minimalPoint) {
        this.minimalPoint = minimalPoint;
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Float getAuthorHeight() {
        return authorHeight;
    }

    public void setAuthorHeight(Float authorHeight) {
        this.authorHeight = authorHeight;
    }

    public String getAuthorPassportID() {
        return authorPassportID;
    }

    public void setAuthorPassportID(String authorPassportID) {
        this.authorPassportID = authorPassportID;
    }

    public Color getAuthorHairColor() {
        return authorHairColor;
    }

    public void setAuthorHairColor(Color authorHairColor) {
        this.authorHairColor = authorHairColor;
    }

    public Integer getAuthorLocationX() {
        return authorLocationX;
    }

    public void setAuthorLocationX(Integer authorLocationX) {
        this.authorLocationX = authorLocationX;
    }

    public float getAuthorLocationY() {
        return authorLocationY;
    }

    public void setAuthorLocationY(float authorLocationY) {
        this.authorLocationY = authorLocationY;
    }

    public Integer getAuthorLocationZ() {
        return authorLocationZ;
    }

    public void setAuthorLocationZ(Integer authorLocationZ) {
        this.authorLocationZ = authorLocationZ;
    }

    public Long getId() {
        return id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
