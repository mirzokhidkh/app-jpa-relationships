package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    //CREATE
    @RequestMapping(method = RequestMethod.POST)
    public String addSubject(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }

    //READ
//    @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public List<Subject> getSubjects() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList;
    }


    @GetMapping("/{id}")
    public Subject getOneSubject(@PathVariable Integer id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        return optionalSubject.orElseGet(Subject::new);
    }

    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()){
            subjectRepository.deleteById(id);
            return "Subject is deleted";
        }

        return "subject is not found";
    }

    @PutMapping("/{id}")
    public String editSubject(@PathVariable Integer id,@RequestBody Subject subject){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);

        if (optionalSubject.isPresent()){
            Subject editingSubject = optionalSubject.get();
            editingSubject.setName(subject.getName());
            subjectRepository.save(editingSubject);
            return "Subject is edited";
        }

        return "subject is not found";
    }
}
