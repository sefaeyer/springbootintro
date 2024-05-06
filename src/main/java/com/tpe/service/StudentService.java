package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
        // email exist mi? ve eger email degisecekse DB de emvcut olmayacak
        boolean emailExist = studentRepository.existsByEmail(studentDTO.getEmail());

        if(emailExist && !studentDTO.getEmail().equalsIgnoreCase(student.getEmail())){
           throw new ConflictException("Email is already exist");
        }

        student.setName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setGrade(studentDTO.getGrade());
        student.setEmail(studentDTO.getEmail());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        // !!! TRICK : asagidaki save ile beraber ogrencinin mevcut id bilgisi degisir mi ??
        // CEVAP : id degismez...
        studentRepository.save(student);
    }


    // Pageable *****************************************
    public Page<Student> getAllWithPage(Pageable pageable) {

        return studentRepository.findAll(pageable);
    }


    // Get By LastName ************************************
    public List<Student> findStudent(String lastName){

        return studentRepository.findByLastName(lastName);

    }


    // Get All Student by Grade ( JPQL ) Java Persistance Query Language ****************
    public List<Student> findAllEqualsGrade(Integer grade) {

        return studentRepository.findAllEqualsGrade(grade);//JPQL
    }


    // DB den direk DTO cekmeye calisalim *********************************************
    public StudentDTO findStudentDTOById(Long id) {
        return studentRepository.findStudentDTOById(id).orElseThrow(()->
                new ResourceNotFoundException("Student not found with id : "+id));
    }
}





