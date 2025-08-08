package org.natwest.controller;

import org.natwest.algorithms.Algorithms;
import org.natwest.model.PrimeNumberResult;
import org.natwest.service.PrimeNumberGeneratorSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/primes", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class PrimeNumberController {

    private final static Logger log = LoggerFactory.getLogger(PrimeNumberController.class);

    private final PrimeNumberGeneratorSupplier primeNumberGeneratorSupplier;

    public PrimeNumberController(
            final PrimeNumberGeneratorSupplier primeNumberGeneratorSupplier) {
        this.primeNumberGeneratorSupplier = primeNumberGeneratorSupplier;
    }

    @GetMapping("/{number}")
    public ResponseEntity<PrimeNumberResult> getPrimes(
            @PathVariable("number") final int number,
            @RequestParam(name = "algorithm", defaultValue = Algorithms.HEURISTIC) final String algorithm) {
        log.info("Starting getting the primes for initial value:{} and with algorithm as {}", number, algorithm);
        PrimeNumberResult primeNumberResult = new PrimeNumberResult(number,
                primeNumberGeneratorSupplier.get(algorithm).primesTill(number));
        return ResponseEntity.ok(primeNumberResult);

    }
}
