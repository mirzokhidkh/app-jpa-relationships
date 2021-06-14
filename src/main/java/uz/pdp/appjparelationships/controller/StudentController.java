package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    AddressRepository addressRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId,
                                                  @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId,
                                                @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroupId(groupId, pageable);
        return studentPage;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getOneStudent(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.orElseGet(Student::new);
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (optionalGroup.isEmpty()) {
            return "Group not found";
        }
        student.setGroup(optionalGroup.get());

        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());

        if (optionalAddress.isEmpty()) {
            return "Address not found";
        }
        student.setAddress(optionalAddress.get());

        studentRepository.save(student);

        return "Student added";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            studentRepository.deleteById(id);
            return "Student is deleted";
        }

        return "Student is not found";
    }

    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student editingStudent = optionalStudent.get();
            editingStudent.setFirstName(studentDto.getFirstName());
            editingStudent.setLastName(studentDto.getLastName());

            Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
            if (optionalGroup.isEmpty()) {
                return "Group not found";
            }
            editingStudent.setGroup(optionalGroup.get());

            Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());

            if (optionalAddress.isEmpty()) {
                return "Address not found";
            }
            editingStudent.setAddress(optionalAddress.get());


            studentRepository.save(editingStudent);
            return "Student is edited";
        }

        return "Student is not found";
    }

}
