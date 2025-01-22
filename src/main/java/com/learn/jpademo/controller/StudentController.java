package com.learn.jpademo.controller;

import com.learn.jpademo.entity.Student;
import com.learn.jpademo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentRepository.save(student));
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        // si l'étudiant est présent, on le renvoit
        if(student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }
        // sinon 404
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            return ResponseEntity.noContent().build();
        }
        // Je vous fais voir d'autres syntaxes
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        boolean exists = studentRepository.existsById(id);
        if(exists) {
            studentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        // sinon 404
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getByName")
    public Student getStudentByName(@RequestParam String name) {
        return studentRepository.findByName(name).orElse(null);
    }

    @GetMapping("/getByEmail")
    public List<Student> getStudentByEmail(@RequestParam String email) {
        return studentRepository.findByEmailContaining(email);
    }
}
