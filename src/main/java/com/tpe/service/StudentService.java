package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.exception.ConflictException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    // getAll() *****************************************************************
    public List<Student> getAll() {
        // handle etmemiz gereken bir durum var mi? YOK
        return studentRepository.findAll(); //SELECT * FROM student
    }


    public void createStudent(Student student) {

        // handle etmemiz gereken bir durum var mi? Email unique mi?
        if(studentRepository.existsByEmail(student.getEmail())){
            throw new ConflictException("Email is already in use");
        }
        studentRepository.save(student);

    }
}
