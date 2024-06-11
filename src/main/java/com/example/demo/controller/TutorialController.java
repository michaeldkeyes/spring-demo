package com.example.demo.controller;

import com.example.demo.model.Tutorial;
import com.example.demo.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tutorials")
@RequiredArgsConstructor
public class TutorialController {

  private final TutorialService tutorialService;

  @GetMapping
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {

    final List<Tutorial> tutorials = tutorialService.getAllTutorials(title);

    if (tutorials.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(tutorials);
  }
}
