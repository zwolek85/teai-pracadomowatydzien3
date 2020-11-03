package pl.teai.pracadomowatydzien3.dzw.listofcars;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class CarService {
    private List<Car> cars;

    CarService() {
        cars = new ArrayList<>();
    }

    List<Car> getCars() {
        return cars;
    }

    Optional<Car> getCarById(long id) {
        return cars.stream().filter(car -> car.getId() == id).findFirst();
    }

    Optional<Car> getCarByColor(String color) {
        return cars.stream().filter(car -> car.getColor().equals(color)).findFirst();
    }

    boolean addCar(Car car) {
        return cars.add(car);
    }

    boolean removeCar(Car car) {
        return cars.remove(car);
    }

    public void updateCar(final Car patchToCar) {
        Optional<Car> carById = getCarById(patchToCar.getId());
        carById.ifPresent(car -> {
            cars.remove(car);
            cars.add(patchToCar);
        });
    }
}
