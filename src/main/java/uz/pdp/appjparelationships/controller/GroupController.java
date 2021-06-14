package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

    //VAZIRLIK UCHUN
    //READ
    @GetMapping
    public List<Group> getGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }


    //UNIVERSITET MAS'UL XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId) {
        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(universityId);
        List<Group> groupsByUniversityId = groupRepository.getGroupsByUniversityId(universityId);
        List<Group> groupsByUniversityIdNative = groupRepository.getGroupsByUniversityIdNative(universityId);
        return allByFaculty_universityId;
    }

    @GetMapping("/{id}")
    public Group getOneGroup(@PathVariable Integer id) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        return optionalGroup.orElseGet(Group::new);
    }


    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {

        Group group = new Group();
        group.setName(groupDto.getName());

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (optionalFaculty.isEmpty()) {
            return "Such faculty not found";
        }

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable Integer id) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isPresent()) {
            groupRepository.deleteById(id);
            return "Group is deleted";
        }
        return "Group is not found";
    }

    @PutMapping("/{id}")
    public String editGroup(@PathVariable Integer id, @RequestBody GroupDto groupDto) {
        Optional<Group> optionalGroup = groupRepository.findById(id);

        if (optionalGroup.isPresent()) {
            Group editingGroup = optionalGroup.get();
            editingGroup.setName(groupDto.getName());
            Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
            editingGroup.setFaculty(optionalFaculty.get());
            groupRepository.save(editingGroup);
            return "Group is edited";
        }

        return "Group is not found";
    }



}
