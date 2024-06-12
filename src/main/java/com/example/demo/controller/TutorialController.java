package com.example.demo.controller;

import com.example.demo.model.Tutorial;
import com.example.demo.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tutorials")
@RequiredArgsConstructor
public class TutorialController {

  private final TutorialService tutorialService;

  @GetMapping
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) final String title) {

    final List<Tutorial> tutorials = tutorialService.getAllTutorials(title);

    if (tutorials.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(tutorials);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") final long id) {
    final Optional<Tutorial> tutorial = tutorialService.getTutorialById(id);

    return tutorial.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/published")
  public ResponseEntity<List<Tutorial>> getPublishedTutorials() {
    final List<Tutorial> tutorials = tutorialService.getPublishedTutorials();

    if (tutorials.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(tutorials);
  }

  @PostMapping
  public ResponseEntity<Tutorial> createTutorial(@RequestBody final Tutorial tutorial) {
    return ResponseEntity.status(201).body(tutorialService.createTutorial(tutorial));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") final long id, @RequestBody final Tutorial tutorial) {
    final Tutorial updatedTutorial = tutorialService.updateTutorial(id, tutorial);

    if (updatedTutorial == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(tutorialService.updateTutorial(id, tutorial));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTutorial(@PathVariable("id") final long id) {
    tutorialService.deleteTutorial(id);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAllTutorials() {
    tutorialService.deleteAllTutorials();

    return ResponseEntity.noContent().build();
  }
}
