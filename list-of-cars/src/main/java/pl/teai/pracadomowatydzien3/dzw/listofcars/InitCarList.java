package pl.teai.pracadomowatydzien3.dzw.listofcars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
class InitCarList {

    private CarService carService;

    @Autowired
    InitCarList(CarService carService) {
        this.carService = carService;
    }

    @EventListener(ApplicationReadyEvent.class)
    void initCarList() {
        carService.addCar(new Car(1, "Audi", "A6", "white"));
        carService.addCar(new Car(2, "Toyota", "Auris", "red"));
        carService.addCar(new Car(3, "Opel", "Astra", "green"));
    }

}
