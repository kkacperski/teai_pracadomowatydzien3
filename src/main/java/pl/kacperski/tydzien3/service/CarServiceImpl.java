package pl.kacperski.tydzien3.service;

import org.springframework.stereotype.Service;
import pl.kacperski.tydzien3.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private List<Car> listOfCars() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1L, "Fiat", "Panda", "Red"));
        carList.add(new Car(2L, "Fiat", "Duplo", "White"));
        carList.add(new Car(3L, "Opel", "Corsa", "Red"));

        return carList;
    }

    @Override
    public List<Car> getAllCars() {
        return listOfCars();
    }

    @Override
    public Optional<Car> getCarById(long id) {
        return listOfCars().stream().filter(car -> car.getId() == id).findFirst();
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        return listOfCars().stream().filter(car -> car.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
    }

    @Override
    public boolean addCar(Car car) {
        return listOfCars().add(car);
    }

    @Override
    public void removeCar(Car car) {
        listOfCars().remove(car);
    }
}
