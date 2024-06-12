package com.example.demo.controller;

import com.example.demo.model.Tutorial;
import com.example.demo.service.TutorialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TutorialController.class)
public class TutorialControllerTests {

  private static final String BASE_URL = "/api/tutorials";

  @MockBean
  private TutorialService tutorialService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Should create tutorial")
  void shouldCreateTutorial() throws Exception {
    final Tutorial tutorial = new Tutorial(1, "title", "description", false);

    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tutorial)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("Should return tutorial")
  void shouldReturnTutorial() throws Exception {
    final long id = 1L;
    final Tutorial tutorial = new Tutorial(id, "title", "description", false);

    when(tutorialService.getTutorialById(id)).thenReturn(Optional.of(tutorial));

    mockMvc.perform(get(BASE_URL + "/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.title").value("title"))
        .andExpect(jsonPath("$.description").value("description"))
        .andExpect(jsonPath("$.published").value(false))
        .andDo(print());
  }

  @Test
  @DisplayName("Should return not found when tutorial not found")
  void shouldReturnNotFoundWhenTutorialNotFound() throws Exception {
    final long id = 1L;

    when(tutorialService.getTutorialById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/" + id))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  @DisplayName("Should return list of tutorials")
  void shouldReturnListOfTutorials() throws Exception {
    final List<Tutorial> tutorials = new ArrayList<>(
        Arrays.asList(
            new Tutorial(1, "title1", "description1", false),
            new Tutorial(2, "title2", "description2", true),
            new Tutorial(3, "title3", "description3", false)
        )
    );

    when(tutorialService.getAllTutorials(null)).thenReturn(tutorials);

    mockMvc.perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(tutorials.size()))
        .andDo(print());
  }

  @Test
  @DisplayName("Should return list of tutorials by title")
  void shouldReturnListOfTutorialsByTitle() throws Exception {
    List<Tutorial> tutorials = new ArrayList<>(
        Arrays.asList(
            new Tutorial(1, "title1", "description1", false),
            new Tutorial(2, "something", "description2", true),
            new Tutorial(3, "title3", "description3", false)
        )
    );
    final String title = "title";
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("title", title);

    final List<Tutorial> filteredTutorials = new ArrayList<>(Arrays.asList(tutorials.get(0), tutorials.get(2)));

    when(tutorialService.getAllTutorials(title)).thenReturn(filteredTutorials);

    mockMvc.perform(get(BASE_URL).params(params))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andDo(print());
  }

  @Test
  @DisplayName("Should return no content when no tutorials found by title")
  void shouldReturnNoContentWhenNoTutorialsFoundByTitle() throws Exception {
    final String title = "title";
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("title", title);

    when(tutorialService.getAllTutorials(title)).thenReturn(Collections.emptyList());

    mockMvc.perform(get(BASE_URL).params(params))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  @DisplayName("Should update tutorial")
  void shouldUpdateTutorial() throws Exception {
    final long id = 1L;

    final Tutorial tutorial = new Tutorial(id, "title", "description", false);
    final Tutorial updatedTutorial = new Tutorial(id, "new title", "new description", true);

    when(tutorialService.updateTutorial(id, updatedTutorial)).thenReturn(updatedTutorial);

    mockMvc.perform(put(BASE_URL + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTutorial)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(updatedTutorial.getTitle()))
        .andExpect(jsonPath("$.description").value(updatedTutorial.getDescription()))
        .andExpect(jsonPath("$.published").value(updatedTutorial.isPublished()))
        .andDo(print());
  }

  @Test
  @DisplayName("Should return not found when tutorial not found for update")
  void shouldReturnNotFoundWhenTutorialNotFoundForUpdate() throws Exception {
    final long id = 1L;

    final Tutorial updatedTutorial = new Tutorial(id, "new title", "new description", true);

    when(tutorialService.updateTutorial(id, updatedTutorial)).thenReturn(null);

    mockMvc.perform(put(BASE_URL + "/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTutorial)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  @DisplayName("Should delete tutorial")
  void shouldDeleteTutorial() throws Exception {
    final long id = 1L;

    doNothing().when(tutorialService).deleteTutorial(id);

    mockMvc.perform(delete(BASE_URL + "/" + id))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  @DisplayName("Should delete all tutorials")
  void shouldDeleteAllTutorials() throws Exception {
    doNothing().when(tutorialService).deleteAllTutorials();

    mockMvc.perform(delete(BASE_URL))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}
