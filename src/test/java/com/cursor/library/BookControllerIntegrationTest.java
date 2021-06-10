package com.cursor.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {BookController.class, BookService.class, BookRepo.class})
@WebMvcTest
public class BookControllerIntegrationTest {

    private final String URI = "/books";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepo bookRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource(
            value = "limit:offset:3:0:1000000",
            delimiter = ':'
    )
    void getBooksPagedTest(
            String limitParam,
            String offsetParam,
            String limitValue,
            String offsetValue,
            String offsetNotFoundValue
    ) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(limitParam, limitValue)
                        .param(offsetParam, offsetValue)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(limitParam, limitValue)
                        .param(offsetParam, offsetNotFoundValue)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @ParameterizedTest
    @CsvSource(
            value = "sort:name:year:n",
            delimiter = ':'
    )
    void getSortedBooksTest(
            String sortParam,
            String sortValueName,
            String sortValueYear,
            String sortBadRequestValue
    ) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(sortParam, sortValueName)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(sortParam, sortValueYear)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(sortParam, sortBadRequestValue)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @ParameterizedTest
    @CsvSource(
            value = "author:George Orwell:test author",
            delimiter = ':'
    )
    void getBooksByAuthorTest(
            String authorParam,
            String authorValue,
            String authorValueNotFound
    ) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(authorParam, authorValue)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(authorParam, authorValueNotFound)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @ParameterizedTest
    @CsvSource(
            value = "updated book:unknown author:2021:novel",
            delimiter = ':'
    )
    void updateBookTest(
            String updatedName,
            String updatedAuthor,
            Integer updatedYear,
            String updatedGenre
    ) throws Exception {
        String idNotFound = UUID.randomUUID().toString();
        String id = bookRepo.getAll().get(0).getId();
        CreateBookDto bookDto = new CreateBookDto();
        bookDto.setName(updatedName);
        bookDto.setAuthor(updatedAuthor);
        bookDto.setYear(updatedYear);
        bookDto.setGenre(updatedGenre);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(URI + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(bookDto))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(URI + "/" + idNotFound)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(bookDto))
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }
}
