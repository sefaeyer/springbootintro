package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
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


    // createStudent() *********************************************
    public void createStudent(Student student) {

        // kontrol etmemiz gereken bir durum var mi? Email unique mi?
        if(studentRepository.existsByEmail(student.getEmail())){
            throw new ConflictException("Email is already in use");
        }
        studentRepository.save(student);

    }


    // getStudentById ********************************************************
    public Student findStudent(Long id) {

        // kontrol etmemiz gereken bir durum var mi?
        return studentRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Student not found with id: "+id));

    }


    // deleteStudent *********************************************************
    public void deleteStudent(Long id) {

        // kontrol etmemiz gereken bir durum var mi?
        Student student = findStudent(id);
        //studentRepository.delete(student);
        studentRepository.deleteById(id);
    }

    // updateStudent **********************************************************
    public void updateStudent(Long id, StudentDTO studentDTO) {

        // kontrol etmemiz gereken bir durum var mi?
        // id li ogrenci var mi? kontrolu
        Student student = findStudent(id);













    }
}





