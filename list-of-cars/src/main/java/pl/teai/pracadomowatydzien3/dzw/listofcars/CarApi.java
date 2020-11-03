package pl.teai.pracadomowatydzien3.dzw.listofcars;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
class CarApi {

    private CarService carService;

    @Autowired
    public CarApi(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        List<Car> cars = carService.getCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable long id) {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent()) {
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color")
    public ResponseEntity<Car> getCarByColor(@RequestParam String color) {
        Optional<Car> carById = carService.getCarByColor(color);
        if (carById.isPresent()) {
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@Validated @RequestBody Car car) {
        if (carService.addCar(car)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<Car> modCarById(@Validated @RequestBody Car newCar) {
        Optional<Car> carById = carService.getCarById(newCar.getId());
        if (carById.isPresent()) {
            Car oldCar = carById.get();
            carService.removeCar(oldCar);
            carService.addCar(newCar);
            return new ResponseEntity<>(oldCar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Car> patchCarById(@PathVariable long id, @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent()) {
            Car oldCar = carById.get();
            Car patchToCar = applyPatchToCar(jsonPatch, oldCar);
            carService.updateCar(patchToCar);
            return new ResponseEntity<>(patchToCar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable long id) {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent()) {
            Car carToDelete = carById.get();
            carService.removeCar(carToDelete);
            return new ResponseEntity<>(carToDelete, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private Car applyPatchToCar(JsonPatch patch, Car targetCustomer) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, Car.class);
    }

}
