package one.digitalinnovation.beerstock.service;

import one.digitalinnovation.beerstock.builder.BeerDTOBuilder;
import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundIdException;
import one.digitalinnovation.beerstock.exception.BeerStockExceededException;
import one.digitalinnovation.beerstock.mapper.BeerMapper;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private BeerRepository beerRepository;

    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test //cadastrar cerveja
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedSavedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        lenient().when(beerRepository.findByNomeIgnoreCase(expectedBeerDTO.getNome())).thenReturn(Optional.empty());
        lenient().when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        //then
        BeerDTO createdBeerDTO = beerService.createBeer(expectedBeerDTO);

        assertThat(createdBeerDTO.getId(), is(equalTo(expectedBeerDTO.getId())));
        assertThat(createdBeerDTO.getNome(), is(equalTo(expectedBeerDTO.getNome())));
        assertThat(createdBeerDTO.getQuantity(), is(equalTo(expectedBeerDTO.getQuantity())));
    }

    @Test //verificar existe dois nomes iguais registrado
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        lenient().when(beerRepository.findByNomeIgnoreCase(expectedBeerDTO.getNome())).thenReturn(Optional.of(duplicatedBeer));

        //then
         assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO));
    }

    @Test //filtrar por nome
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        //when
        lenient().when(beerRepository.findByNomeIgnoreCase(expectedFoundBeer.getNome())).thenReturn(Optional.of(expectedFoundBeer));

        //then
        BeerDTO foundBeerDTO = beerService.findByName(expectedFoundBeerDTO.getNome());

        assertThat(foundBeerDTO.getNome(), is(equalTo(expectedFoundBeerDTO.getNome())));
    }

    @Test //verificar existe nome filtrado
    void whenNoRegisteredBeerNameIsGivenThenThrowAnException() {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        lenient().when(beerRepository.findByNomeIgnoreCase(expectedFoundBeerDTO.getNome())).thenReturn(Optional.empty());

        //then
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedFoundBeerDTO.getNome()));
    }

    @Test //busca de lista de cervejas
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        //given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        //when
        when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));

        //then
        List<BeerDTO> foundListBeerDTO = beerService.listAll();

        assertThat(foundListBeerDTO, is(not(empty())));
        assertThat(foundListBeerDTO.get(0), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test //verificar lista cervejas vazia
    void whenListBeerIsCalledThenReturnAEmptyListOfBeers() {
        //when
        when(beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<BeerDTO> foundListBeerDTO = beerService.listAll();

        assertThat(foundListBeerDTO, is(empty()));
    }

    @Test // teste de exclusão de uma cerveja
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundIdException {
        //given
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedDeletedBeer = beerMapper.toModel(expectedDeletedBeerDTO);

        //when
        when(beerRepository.findById(expectedDeletedBeer.getId())).thenReturn(Optional.of(expectedDeletedBeer));
        doNothing().when(beerRepository).deleteById(expectedDeletedBeer.getId());

        //then
        beerService.deleteById(expectedDeletedBeer.getId());

        verify(beerRepository, times(1)).findById(expectedDeletedBeerDTO.getId());
        verify(beerRepository, times(1)).deleteById(expectedDeletedBeerDTO.getId());
    }

    @Test // teste do incremento de cervejas no estoque
    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundIdException, BeerStockExceededException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        Mockito.when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        Mockito.when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;

        //then
        BeerDTO incrementBeerDTO = beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }

    @Test // verifica a quantidade a ser incrementada não ser maior que a máxima
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        Mockito.when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 60;

        assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }

    @Test // verifica a quantidade de cervejas no estoque para não exceder
    void whenIncrementAfterSunsIsGreatherThanMaxThenThrowException() {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        Mockito.when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 45;

        assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }

    @Test // incremento de cerveja caso não exista o ID
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        Mockito.when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundIdException.class, () -> beerService.increment(INVALID_BEER_ID, quantityToIncrement));
    }
}
