package com.lambdaschool.school.service;

import com.lambdaschool.school.SchoolApplication;
import com.lambdaschool.school.exceptions.ResourceNotFoundException;
import com.lambdaschool.school.model.Course;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchoolApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourseServiceImplTest
{
    @Autowired
    private CourseService courseService;

    @Before
    public void AsetUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void BtearDown() throws Exception
    {

    }

    @Test
    public void CfindCourseById()
    {
        assertEquals("Data Science", courseService.findCourseById(1).getCoursename());
    }

    @Test (expected = ResourceNotFoundException.class)
    public void DdeleteNotFound()
    {
        courseService.delete(150);
        assertEquals(5, courseService.reallyFindAll().size());
    }

    @Test
    public void EdeleteFound()
    {
        courseService.delete(1);
        assertEquals(5, courseService.reallyFindAll().size());
    }

    @Test
    public void Fsave()
    {
        Course c1 = new Course("Fun with Java");

        Course addCourse = courseService.save(c1);

        assertNotNull(addCourse);

        Course foundCourse = courseService.findCourseById(addCourse.getCourseid());
        assertEquals(addCourse.getCoursename(), foundCourse.getCoursename());
    }

}
