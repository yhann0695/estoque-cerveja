package one.digitalinnovation.beerstock.controller;

import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundIdException;
import one.digitalinnovation.beerstock.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    @Autowired
    private BeerService beerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BeerDTO> createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
            return ResponseEntity.ok(beerService.createBeer(beerDTO));
    }

    @GetMapping
    public ResponseEntity<List<BeerDTO>> listAll() {
        List<BeerDTO> listBeer = beerService.listAll();
        return ResponseEntity.ok(listBeer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Long> delete(@PathVariable Long id) throws BeerNotFoundIdException {
       return ResponseEntity.ok(beerService.deleteById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<BeerDTO> findByName(@PathVariable String name) throws BeerNotFoundException {
        return ResponseEntity.ok(beerService.findByName(name));
    }
}
