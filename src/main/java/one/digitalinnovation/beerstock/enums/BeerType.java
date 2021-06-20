package one.digitalinnovation.beerstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerType {

    LAGER("Lager"),
    MALZBIER("Malzbier"),
    WITBIER("Witbier"),
    WEISSION("Weission"),
    ALE("Ale"),
    IPA("Ipa"),
    STOUT("Stout");

    private final String description;
}
