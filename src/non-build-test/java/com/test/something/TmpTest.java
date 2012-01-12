package com.test.something;

import static builderkit.test.XBandRequestBuilder.aXBandRequest;
import static builderkit.test.XBandRequestBuilder.CustomizationSelectionBuilder.aCustomizationSelection;
import static builderkit.test.XBandRequestBuilder.PrimaryGuestBuilder.aPrimaryGuest;
import static builderkit.test.XBandRequestBuilder.PrimaryGuestBuilder.AddressBuilder.anAddress;

import org.junit.Test;

public class TmpTest {

    @Test
    public void printJson() {
        System.out.println(
                aXBandRequest()
                    .withPrimaryGuest(aPrimaryGuest()
                            .withPhoneNumber(1234567L)
                            .withAddress(anAddress()
                                    .withCity("York")
                                    .withAddress1("1 Innovation Close")))
                    .withCustomizationSelections(
                            aCustomizationSelection(),
                            aCustomizationSelection()
                                .withFirstName("John"))
                .asJson());
    }
}
