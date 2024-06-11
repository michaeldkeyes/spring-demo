package com.example.demo.service;

import com.example.demo.model.Tutorial;
import com.example.demo.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorialService {

  private final TutorialRepository tutorialRepository;

  public List<Tutorial> getAllTutorials(final String title) {
    if (title == null) {
      return tutorialRepository.findAll();
    } else {
      return tutorialRepository.findByTitleContaining(title);
    }
  }
}
