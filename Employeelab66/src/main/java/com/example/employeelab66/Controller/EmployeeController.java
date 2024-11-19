package com.example.employeelab66.Controller;

import com.example.employeelab66.Model.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/Employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee){
//        if (errors.hasErrors()){
//            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
//        }
        employees.add(employee);

        return ResponseEntity.ok().body("add Employee successfully");
    }


    @GetMapping("/get")
    public ResponseEntity getEmployee(){
        return ResponseEntity.ok().body(employees);
    }

    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index,@RequestBody @Valid Employee employee, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        employees.set(index, employee);
        return ResponseEntity.ok().body("Employee updated successfully");
    }

    //4. Delete an employee: Deletes an employee from the system.
    //Note:

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delteteEmployee(@PathVariable int id){

        for (Employee employee : employees){
            if (employee.getID() == id){
                employees.remove(employee);
                return ResponseEntity.status(200).body("employee removed");}
        }
        return ResponseEntity.status(404).body("Employee not found");}

    //5. Search Employees by Position: Retrieves a list of employees based on their
    //position (supervisor or coordinator).
    //Note:
    //▪ Ensure that the position parameter is valid (either "supervisor" or "coordinator").

    @GetMapping("/get-postition/{position}")
    public ResponseEntity getPostition(@PathVariable @Valid @Pattern(regexp = "^(supervisor|coordinator)$")String position){
        ArrayList <Employee> employees1 = new ArrayList<>();
        for (Employee employee : employees){
            if (employee.getPosition().equals(position)){employees1.add(employee);}
        }

        return ResponseEntity.ok().body(employees1);
    }

    //6. Get Employees by Age Range: Retrieves a list of employees within a specified
    //age range.
    //Note:
    //▪ Ensure that minAge and maxAge are valid age values.

    @GetMapping("/get-employees-age/{min_age}/{max_age}")
    public ResponseEntity getEmployeeAge(@PathVariable @Min(value = 26, message = "age must be higher than 25") @Positive(message = "number must be a positive")int min_age, @PathVariable @Min(value = 26, message = "age must be higher than 25") @Positive(message = "number must be a positive") int max_age){
        ArrayList<Employee> employees1 = new ArrayList<>();

        for (Employee employee: employees){
            if (employee.getAge() >= min_age && employee.getAge() <= max_age){employees1.add(employee);}
        }
        return ResponseEntity.ok().body(employees1);
    }
    //7. Apply for annual leave: Allow employees to apply for annual leave.
    //Note:
    //▪ Verify that the employee exists.
    //▪ The employee must not be on leave (the onLeave flag must be false).
    //▪ The employee must have at least one day of annual leave remaining.
    //▪ Behavior:
    //▪ Set the onLeave flag to true.
    //▪ Reduce the annualLeave by 1.
    @PutMapping("/update-annual-leave/{id}")
    public ResponseEntity annualLeave(@PathVariable @Valid int id) {
        for (Employee employee : employees) {
            if (employee.getID() == id) {
                if (employee.isOn_leave() == false) {
                    if (employee.getAnnual_leave() > 0) {
                        employee.setOn_leave(true);
                        employee.setAnnual_leave(employee.getAnnual_leave()-1);
                        return ResponseEntity.ok().body("annual leave updated");
                    } //else return ResponseEntity.status(400).body("employee doesn't have any days to use on leave");
                }//else return ResponseEntity.status(400).body("employee is already on leave");
            } //else return ResponseEntity.status(400).body("id doesn't exist");
        }
        return ResponseEntity.status(404).body("cannot update annual leave.\n1- id doesn't exist\n2- employee is already on leave\n3- employee doesn't have any days to use on leave");
    }
    //8. Get Employees with No Annual Leave: Retrieves a list of employees who have
    //used up all their annual leave.
    @GetMapping("/get-employees-no-annuals")
    public ResponseEntity employeeNoAnnuals(){
        ArrayList <Employee> employees1 = new ArrayList<>();
        for (Employee employee: employees){
            if (employee.getAnnual_leave() <= 0 ){employees1.add(employee);
            }else return ResponseEntity.status(200).body("All employees has at least 1 day remains in their record");
        }
        return ResponseEntity.ok().body(employees1);
    }
    //9. Promote Employee: Allows a supervisor to promote an employee to the position
    //of supervisor if they meet certain criteria. Note:
    //▪ Verify that the employee with the specified ID exists.
    //▪ Ensure that the requester (user making the request) is a supervisor.
    //▪ Validate that the employee's age is at least 30 years.
    //▪ Confirm that the employee is not currently on leave.
    //▪ Change the employee's position to "supervisor" if they meet the criteria.

    @PutMapping("/prmote-employee/{id_supervisor}/{id_employee}")
    public ResponseEntity prmoteEmployee(@PathVariable @Valid @Min(value = 2,message = "Must be at least 2 digits") int id_supervisor, @PathVariable @Valid @Min(value = 2,message = "Must be at least 2 digits") int id_employee){

        for (Employee employee: employees){
            if (employee.getID() == id_supervisor){
                if (employee.getPosition().equals("supervisor")) {
                    for (Employee employee1 : employees) {
                        {
                            if (employee1.getID() == id_employee) {
                                if (employee1.getAge() >= 30) {
                                    if (employee1.isOn_leave() == false) {
                                        employee1.setPosition("supervisor");
                                        return ResponseEntity.ok().body("Employee promoted successfully");
                                    }
                                }
                            }
                        }
                    }
                }
        }


        }
        return ResponseEntity.status(400).body("cannot promote Employee.\n1- supervisor id doesn't exist\n2- employee id doesn't exist\n3- employee age is lower than 30\n4- employee is currently on leave\n5- the requesting the promotion isn't a supervisor");
    }
}
