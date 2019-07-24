package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.view.CountStudentsInCourses;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/courses")
public class CourseController
{
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "Returns all Courses", responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Courses Found", responseContainer = "List", response = Course.class), @ApiResponse(code = 500, message = "Error retrieving Courses", response = ErrorDetail.class)})
    @ApiImplicitParams({@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"), @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."), @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> listAllCourses(@PageableDefault(page = 0, size = 3) Pageable pageable, HttpServletRequest request)
    {
        logger.info("GET " + request.getRequestURI() + " accessed");
        ArrayList<Course> myCourses = courseService.findAll(pageable);
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    @GetMapping(value = "/allcourses", produces = {"application/json"})
    public ResponseEntity<?> reallyListAllCourses()
    {
        ArrayList<Course> allCourses = courseService.reallyFindAll();
        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

    @ApiOperation(value = "Return Students per Course", responseContainer = "ArrayList")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Student Count Found", responseContainer = "List", response = Course.class), @ApiResponse(code = 500, message = "Error retrieving Student Count", response = ErrorDetail.class)})
    @GetMapping(value = "/studcount", produces = {"application/json"})
    public ResponseEntity<?> getCountStudentsInCourses(HttpServletRequest request)
    {
        logger.info("GET " + request.getRequestURI() + " accessed");
        ArrayList<CountStudentsInCourses> myList = courseService.getCountStudentsInCourse();
        return new ResponseEntity<>(myList, HttpStatus.OK);
//        return new ResponseEntity<>(courseService.getCountStudentsInCourse(), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Course by ID", response = void.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Course Deleted", response = void.class), @ApiResponse(code = 500, message = "Error deleting Course", response = ErrorDetail.class)})
    @DeleteMapping("/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(@ApiParam(value = "Course ID", required = true, example = "1") @PathVariable long courseid, HttpServletRequest request)
    {
        logger.info("DELETE " + request.getRequestURI() + " accessed");
        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
