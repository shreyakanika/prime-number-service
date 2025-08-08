package com.natwest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.natwest.generator.PrimeNumberGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.natwest.App;
import org.natwest.algorithms.Algorithms;
import org.natwest.controller.PrimeNumberController;
import org.natwest.model.ErrorInfo;
import org.natwest.model.PrimeNumberResult;
import org.natwest.service.PrimeNumberGeneratorSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(Parameterized.class)
@WebAppConfiguration
@ContextConfiguration(classes = App.class)
public class PrimeNumberControllerTest {

    @Parameter(0)
    public ObjectMapper mapper;
    @Parameter(1)
    public MediaType mediaType;
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private PrimeNumberGenerator generator;
    @Mock
    private PrimeNumberGeneratorSupplier generatorSupplier;
    @InjectMocks
    private PrimeNumberController primeNumberController;
    // These objects will be magically initialised by the initFields method below
    private JacksonTester<PrimeNumberResult> primeNumberResultSerDe;
    private JacksonTester<ErrorInfo> errorInfoSerDe;
    private MockMvc mvc;
    private TestContextManager testContextManager;
    @Autowired
    private WebApplicationContext wac;

    @Parameterized.Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(
                new Object[][]{
                        {new ObjectMapper(), MediaType.APPLICATION_JSON},
                        {new XmlMapper(), MediaType.APPLICATION_XML}
                });
    }

    @Before
    public void init() throws Exception {
        // Initialises the JacksonTester
        JacksonTester.initFields(this, mapper);
//        // MockMvc stand-alone approach
//        mvc = MockMvcBuilders.standaloneSetup(primeNumberController).build();

        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);

        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnPrimesWhenSendingPositiveNumberAndAlgorithmIsDefault() throws Exception {
        given(generatorSupplier.get(Algorithms.DEFAULT)).willReturn(generator);
        given(generator.primesTill(6)).willReturn(Arrays.asList(1, 2, 3, 5));

        MockHttpServletResponse response =
                mvc.perform(get("/primes/6").accept(mediaType)).andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString())
                .isEqualTo(
                        primeNumberResultSerDe
                                .write(new PrimeNumberResult(6, Arrays.asList(1, 2, 3, 5)))
                                .getJson());
    }

    @Test
    public void shouldReturnPrimesWhenSendingPositiveNumberAndAlgorithmIsBruteForce()
            throws Exception {
        given(generatorSupplier.get(Algorithms.BRUTE_FORCE)).willReturn(generator);
        given(generator.primesTill(8)).willReturn(Arrays.asList(1, 2, 3, 5, 7));

        MockHttpServletResponse response =
                mvc.perform(get("/primes/8?algorithm=bruteForce").accept(mediaType))
                        .andReturn()
                        .getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString())
                .isEqualTo(
                        primeNumberResultSerDe
                                .write(new PrimeNumberResult(8, Arrays.asList(1, 2, 3, 5, 7)))
                                .getJson());
    }

    @Test
    public void shouldReturnPrimesWhenSendingPositiveNumberAndAlgorithmIsHeuristic()
            throws Exception {
        given(generatorSupplier.get(Algorithms.HEURISTIC)).willReturn(generator);
        given(generator.primesTill(8)).willReturn(Arrays.asList(1, 2, 3, 5, 7));

        MockHttpServletResponse response =
                mvc.perform(get("/primes/8?algorithm=heuristic").accept(mediaType))
                        .andReturn()
                        .getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString())
                .isEqualTo(
                        primeNumberResultSerDe
                                .write(new PrimeNumberResult(8, Arrays.asList(1, 2, 3, 5, 7)))
                                .getJson());
    }

    @Test
    public void shouldReturnErrorWhenSendingPositiveNumberAndAlgorithmIsUnknown() throws Exception {
        given(generatorSupplier.get(anyString()))
                .willThrow(new IllegalArgumentException("Unknown algorithm"));

        MockHttpServletResponse response =
                mvc.perform(get("/primes/6?algorithm=unknown").accept(mediaType).content(
                        String.valueOf(APPLICATION_JSON))).andReturn().getResponse();
        System.out.println(response);
        System.out.println(response.getContentAsString());
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        then(response.getContentAsString()).isEqualTo("");
        then(response.getContentAsString())
                .isEqualTo(
                        errorInfoSerDe
                                .write(
                                        new ErrorInfo(
                                                "http://localhost/primes/6?algorithm=unknown",
                                                "Prime number generator 'unknown' not found"))
                                .getJson());
    }

    @Test
    public void shouldReturnErrorWhenSendingNegativeNumberAndAlgorithmIsDefault() throws Exception {
        given(generatorSupplier.get(Algorithms.DEFAULT)).willReturn(generator);
        given(generator.primesTill(-1)).willThrow(new IllegalArgumentException("Error message"));

        MockHttpServletResponse response =
                mvc.perform(get("/primes/-1").accept(mediaType)).andReturn().getResponse();

        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        then(response.getContentAsString())
//                .isEqualTo(
//                        errorInfoSerDe
//                                .write(new ErrorInfo("http://localhost/primes/-1", "Error message"))
//                                .getJson());
    }

    @Test
    public void shouldReturnErrorWhenSendingNotANumber() throws Exception {

        MockHttpServletResponse response = mvc.perform(get("/primes/a").accept(mediaType)).andReturn().getResponse();
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        then(response.getContentAsString())
//                .isEqualTo(
//                        errorInfoSerDe
//                                .write(new ErrorInfo("http://localhost/primes/a", "Not a valid number."))
//                                .getJson());
    }

}
