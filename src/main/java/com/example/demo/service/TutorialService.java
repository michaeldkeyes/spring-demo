package com.example.demo.service;

import com.example.demo.model.Tutorial;
import com.example.demo.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

  public Optional<Tutorial> getTutorialById(final long id) {
    return tutorialRepository.findById(id);
  }

  public List<Tutorial> getPublishedTutorials() {
    return tutorialRepository.findByPublished(true);
  }

  public Tutorial createTutorial(final Tutorial tutorial) {
    return tutorialRepository.save(tutorial);
  }

  public Tutorial updateTutorial(final long id, final Tutorial tutorial) {
    final Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

    if (tutorialData.isPresent()) {
      final Tutorial updatedTutorial = tutorialData.get();
      updatedTutorial.setTitle(tutorial.getTitle());
      updatedTutorial.setDescription(tutorial.getDescription());
      updatedTutorial.setPublished(tutorial.isPublished());
      return tutorialRepository.save(updatedTutorial);
    } else {
      return null;
    }
  }

  public void deleteTutorial(final long id) {
    tutorialRepository.deleteById(id);
  }

  public void deleteAllTutorials() {
    tutorialRepository.deleteAll();
  }
}
