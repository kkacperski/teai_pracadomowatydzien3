package pl.kacperski.tydzien3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kacperski.tydzien3.model.Car;
import pl.kacperski.tydzien3.service.CarService;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class CarController {

    private final CarService carService;

    private void addLinkToCar(Car car){
        car.add(linkTo(CarController.class).slash(car.getId()).withSelfRel());
    }

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    // metoda do pobierania wszystkich pozycji
    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        List<Car> carList = carService.getAllCars();
        carList.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).slash(car.getId()).withSelfRel()));
        return ResponseEntity.ok(carList);
    }


    // metoda do pobierania elementu po jego id
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarsById(@PathVariable long id){
        Optional<Car> carById = carService.getCarById(id);

        return carById.map(car -> {
            addLinkToCar(car);
            return ResponseEntity.ok(car);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // metoda do pobierania elementów w określonym kolorze (query)
    @GetMapping("color/{color}")
    public ResponseEntity<List<Car>> getCarsByColor(@PathVariable String color) {
        List<Car> carsByColor = carService.getCarsByColor(color);
        carsByColor.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).slash(car.getId()).withSelfRel()));

        if (!carsByColor.isEmpty()) {
            return new ResponseEntity<>(carsByColor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // metoda do dodawania pozycji
    @PostMapping
    public ResponseEntity addCar(@Validated @RequestBody Car car) {
        if (carService.addCar(car)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // metoda do modyfikowania pozycji
    @PutMapping
    public ResponseEntity modCar(@RequestBody Car newCar) {
        Optional<Car> first = carService.getCarById(newCar.getId());
        if (first.isPresent()) {
            carService.removeCar(first.get());
            carService.addCar(newCar);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // metoda do modyfikowania pozycji
//    @PatchMapping("/{id}")
//    public ResponseEntity modPartCar(@PathVariable long id, @RequestBody Map<Object, Object> fields) {
//        Optional<Car> first = carService.getCarById(id);
//        if (first.isPresent()) {
//            fields.forEach((key, value) -> {
//                Field field = ReflectionUtils.findField(Car.class, (String) key);
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, first.get(), value);
//            });
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @PatchMapping("/{id}")
    public ResponseEntity modPartCar(@PathVariable long id,
                                     @RequestParam(required = false) String mark,
                                     @RequestParam(required = false) String model,
                                     @RequestParam(required = false) String color) {
        Optional<Car> first = carService.getCarById(id);
        if (first.isPresent()) {
            if (mark != null) {
                first.get().setMark(mark);
            }
            if (model != null) {
                first.get().setModel(model);
            }
            if (color != null) {
                first.get().setColor(color);
            }

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // metoda do usuwania jeden pozycji
    @DeleteMapping("/{id}")
    public ResponseEntity removeCar(@PathVariable long id) {
        Optional<Car> first = carService.getCarById(id);
        if (first.isPresent()) {
            carService.removeCar(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
