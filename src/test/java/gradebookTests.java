import edu.course.gradebook.Gradebook;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class gradebookTests {
    @Test
    public void addStudentTest(){
        Gradebook gradebook = new Gradebook();
        assertEquals(Optional.empty(), gradebook.findStudentGrades("Chana"));
        gradebook.addStudent("Chana");
        assertTrue(gradebook.findStudentGrades("Chana").isPresent());
    }
    @Test
    public void addGradeTest(){
        Gradebook gradebook = new Gradebook();
        gradebook.addStudent("Chana");
        assertTrue(gradebook.findStudentGrades("Chana").get().isEmpty());
        gradebook.addGrade("Chana", 100);
        assertEquals(1,  gradebook.findStudentGrades("Chana").get().size());
        assertEquals(100, gradebook.findStudentGrades("Chana").get().getFirst());
    }
    @Test
    public void findGradeTest(){
        Gradebook gradebook = new Gradebook();
        assertEquals(Optional.empty(), gradebook.findStudentGrades("Chana"));
        gradebook.addStudent("Chana");
        assertTrue(gradebook.findStudentGrades("Chana").isPresent());
        assertTrue(gradebook.removeStudent("Chana"));
        assertEquals(Optional.empty(), gradebook.findStudentGrades("Chana"));
        assertFalse(gradebook.removeStudent("Chana"));

    }
    @Test
    public void averageStudentTest(){
        Gradebook gradebook = new Gradebook();
        gradebook.addStudent("Chana");
        gradebook.addGrade("Chana", 100);
        gradebook.addGrade("Chana", 100);
        gradebook.addGrade("Chana", 100);
        assertEquals(Optional.of(100.0), gradebook.averageFor("Chana"));
        assertEquals(Optional.empty(), gradebook.averageFor("Hanna"));
    }
    @Test
    public void letterGradeStudentTest(){
        Gradebook gradebook = new Gradebook();
        gradebook.addStudent("Chana");
        gradebook.addGrade("Chana", 100);
        assertEquals(Optional.of("A"), gradebook.letterGradeFor("Chana"));
        assertEquals(Optional.empty(), gradebook.letterGradeFor("Hanna"));
        gradebook.addGrade("Chana", 50);
        assertEquals(Optional.of("C"), gradebook.letterGradeFor("Chana"));
    }
    @Test
    public void classAverageTest(){
        Gradebook gradebook = new Gradebook();
        gradebook.addStudent("Chana");
        assertEquals(Optional.empty(), gradebook.classAverage());
        gradebook.addGrade("Chana", 100);
        assertEquals(Optional.of(100.0), gradebook.classAverage());
        gradebook.addStudent("Hanna");
        gradebook.addGrade("Chana", 50);
        assertEquals(Optional.of(75.0), gradebook.classAverage());
    }
    @Test
    public void undoTest(){
        Gradebook gradebook = new Gradebook();
        gradebook.addStudent("Chana");
        assertTrue(gradebook.findStudentGrades("Chana").isPresent());
        gradebook.removeStudent("Chana");
        assertFalse(gradebook.findStudentGrades("Chana").isPresent());
        assertTrue(gradebook.undo());
        assertTrue(gradebook.findStudentGrades("Chana").isPresent());
        assertFalse(gradebook.undo());
        gradebook.addGrade("Chana", 100);
        assertEquals(100, gradebook.findStudentGrades("Chana").get().getFirst());
        assertTrue(gradebook.undo());
        assertTrue(gradebook.findStudentGrades("Chana").get().isEmpty());
    }
    @Test
    public void logTest() {
        Gradebook gradebook = new Gradebook();
        assertTrue(gradebook.recentLog(5).isEmpty());
        gradebook.addStudent("Chana");
        assertEquals(1, gradebook.recentLog(1).size());
        gradebook.addGrade("Chana", 100);
        assertEquals(1, gradebook.recentLog(1).size());
        assertEquals(2, gradebook.recentLog(2).size());
        assertEquals(2, gradebook.recentLog(3).size());
    }
}
