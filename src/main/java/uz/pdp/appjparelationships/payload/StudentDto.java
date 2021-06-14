package uz.pdp.appjparelationships.payload;

import lombok.Data;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;
    private Integer addressId;
    private Integer groupId;



}
