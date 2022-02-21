package pl.kacperski.tydzien3.model;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Car extends RepresentationModel<Car> {

    @NotNull
    private long id;

    @NotBlank(message = "Mark is mandatory")
    private String mark;

    @NotBlank(message = "Model is mandatory")
    private String model;

    @NotBlank(message = "Color is mandatory")
    private String color;

    public Car(long id, String mark, String model, String color) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }

    public Car() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
