package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!

    @ApiOperation(value = "Returns all Students", responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Students Found", responseContainer = "List", response = Student.class), @ApiResponse(code = 500, message = "Error retrieving Students", response = ErrorDetail.class)})
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(HttpServletRequest request)
    {
        logger.info("GET " + request.getRequestURI() + " accessed");

        List<Student> myStudents = studentService.findAll();
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns Student by ID", response = Student.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Student Retrieved", response = void.class), @ApiResponse(code = 404, message = "Student not found", response = ErrorDetail.class)})
    @GetMapping(value = "/Student/{StudentId}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @ApiParam(value = "Student ID", required = true, example = "1")
            @PathVariable
                    Long StudentId, HttpServletRequest request)
    {
        logger.info("GET " + request.getRequestURI() + " accessed");
        Student r = studentService.findStudentById(StudentId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }


    @ApiOperation(value = "Returns Student by Name", response = Student.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Student Retrieved", response = void.class), @ApiResponse(code = 404, message = "Student not found", response = ErrorDetail.class)})
    @GetMapping(value = "/student/namelike/{name}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @ApiParam(value = "Student Name", required = true, example = "John")
            @PathVariable String name, HttpServletRequest request)
    {
        logger.info("GET " + request.getRequestURI() + " accessed");
        List<Student> myStudents = studentService.findStudentByNameLike(name);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }


    @ApiOperation(value = "Add a new Student", response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Student Created", response = void.class), @ApiResponse(code = 500, message = "Error creating new Student", response = ErrorDetail.class)})
    @PostMapping(value = "/Student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid
                                           @RequestBody
                                                   Student newStudent, HttpServletRequest request) throws URISyntaxException
    {
        logger.info("POST " + request.getRequestURI() + " accessed");
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Update a Student", response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Student Updated", response = void.class), @ApiResponse(code = 500, message = "Error Updating Student", response = ErrorDetail.class)})
    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(
            @ApiParam(value = "Student ID", required = true, example = "1")
            @RequestBody
                    Student updateStudent,
            @PathVariable
                    long Studentid, HttpServletRequest request)
    {
        logger.info("PUT " + request.getRequestURI() + " accessed");
        studentService.update(updateStudent, Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Delete a Student", response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Student Deleted", response = void.class), @ApiResponse(code = 500, message = "Error Deleting Student", response = ErrorDetail.class)})
    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            @ApiParam(value = "Student ID", required = true, example = "1")
            @PathVariable
                    long Studentid, HttpServletRequest request)
    {
        logger.info("DELETE " + request.getRequestURI() + " accessed");
        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
