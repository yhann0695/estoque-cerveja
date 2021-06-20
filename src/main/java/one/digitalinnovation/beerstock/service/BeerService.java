package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundIdException;
import one.digitalinnovation.beerstock.mapper.BeerMapper;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerService {

    @Autowired
    private BeerRepository beerRepository;

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getNome());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByNomeStartingWithIgnoreCase(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(foundBeer);
    }

    public List<BeerDTO> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Long deleteById(Long id) throws BeerNotFoundIdException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
        return id;
    }

    private void verifyIfIsAlreadyRegistered(String nome) throws BeerAlreadyRegisteredException {
        Optional<Beer> optSaveBeer = beerRepository.findByNomeStartingWithIgnoreCase(nome);
        if(optSaveBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(nome);
        }
    }

    private Beer verifyIfExists(Long id) throws BeerNotFoundIdException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundIdException(id));
    }
}
