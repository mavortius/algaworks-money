package com.algaworks.algamoney.service;

import com.algaworks.algamoney.model.Person;
import com.algaworks.algamoney.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonRepository repository;

  public PersonService(PersonRepository repository) {
    this.repository = repository;
  }

  public Person getById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
  }

  public Person save(Person person) {
    person.getContacts().forEach(c -> c.setPerson(person));
    return repository.save(person);
  }

  public void update(Long id, Boolean active) {
    Person person = getById(id);
    person.setActive(active);
    repository.save(person);
  }

  public Person update(Long id, Person person) {
    Person savedPerson = getById(id);
    savedPerson.getContacts().clear();
    savedPerson.getContacts().addAll(person.getContacts());
    person.getContacts().forEach(c -> c.setPerson(savedPerson));
    BeanUtils.copyProperties(person, savedPerson, "id", "contacts");
    return repository.save(savedPerson);
  }
}
