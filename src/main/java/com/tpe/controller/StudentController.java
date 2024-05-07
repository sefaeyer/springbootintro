package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
        ***** SORU-1 :  @Controller yerine , @Component kullanirsam ne olur ??
        **    CEVAP-1 : Dispatcher , @Controller ile annote edilmis sınıfları tarar ve
        bunların içindeki @RequestMapping annotationlari algilamaya calisir. Dikkat :
        @Component ile annote edilen siniflar taranmayacaktir..
        Ayrica  @RequestMapping'i yalnızca sınıfları @Controller ile annote edilmis olan
        methodlar üzerinde/içinde kullanabiliriz ve @Component, @Service, @Repository vb.
        ile ÇALIŞMAZ…
        ***** SORU-2 : @RestController ile @Controller arasindaki fark nedir ??
        **   CEVAP-2 : @Controller, Spring MVC framework'ünün bir parçasıdır.genellikle HTML
        sayfalarının görüntülenmesi veya yönlendirilmesi gibi işlevleri gerçekleştirmek
        üzere kullanılır.
                       @RestController annotation'ı, @Controller'dan türetilmiştir ve RESTful
         web servisleri sağlamak için kullanılır.Bir sınıfın üzerine konulduğunda, tüm
         metodlarının HTTP taleplerine JSON gibi formatlarda cevap vermesini sağlar.
         ***** SORU-3 : Controller'dan direk Repo ya gecebilir miyim
         **   CEVAP-3: HAYIR, BusinessLogic ( kontrol ) katmani olan Service'i atlamam gerekir.
 */


@RestController
@RequestMapping("/students") //http://localhost:8080/students
// 1 --> Student[] olur mu ? Olmaz List<> ile calismamam gerekiyor
// 2 --> Response icinde Status codunu rahat setlemek icin ResponseEntity ..
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    // getAll() *************************************************************
    @GetMapping //http://localhost:8080/students + GET
    public ResponseEntity<List<Student>> getAll(){
        List<Student> students = studentService.getAll();
        return ResponseEntity.ok(students); // 200 status kodu ile nesneleri client tarafina yonlendirdi
    }


    //Create Student ***********************************************************
    @PostMapping  // http://localhost:8080/students + POST + JSON
    public ResponseEntity<Map<String,String>> createStudent(@Valid @RequestBody Student student){ // JSON --> Student objesine donmesi lazim

        //@Valid : parametreler valid mi kontrol eder, bu ornekte Student
            //objesi olusturmak icin gonderilen fieldlar yani
            //name gibi ozellikler duzgun set edilmis mi onu kontrol eder
        // @RequestBody = gelen requestin bodysindeki bilgiyi
            //Student objesine map edilmesini sagliyor

        studentService.createStudent(student);

        Map<String,String> map = new HashMap<>();
        map.put("message","Student is created succesfully");
        map.put("status","true");

        return new ResponseEntity<>(map, HttpStatus.CREATED); //201
    }


    //Get Student By ID with -> RequestParam ******************************************
    @GetMapping("/query")  // http://localhost:8080/students/query?id=1 + GET  // coklu data almak icin @RequestParam
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student); // 200
    }


    //Get Student By ID with -> PathVariable ******************************************
    @GetMapping("/{id}")  // http://localhost:8080/students/1 + GET
    public ResponseEntity<Student> getStudentByPath(@PathVariable("id") Long id){
        Student student = studentService.findStudent(id);
        return ResponseEntity.ok(student); // 200
    }
    // !!! TRICK = 1 data alacaksam PathVariable ama birden fazla data alacaksam
    //  RequestParam daha kullanisli


    //DeleteStudent ******************************************************************
    @DeleteMapping("/{id}")  // http://localhost:8080/students/delete/2 + DELETE
    public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable("id") Long id){
        studentService.deleteStudent(id);
        Map<String ,String> map = new HashMap<>();
        map.put("message","Student is deleted succesfully");
        map.put("status","true");
        return new ResponseEntity<>(map, HttpStatus.OK);

    }


    // Update Student **************************************************************
    @PutMapping("/{id}")  // http://localhost:8080/students/1 + PUT + JSON
    public ResponseEntity<String> updateStudent(@PathVariable Long id,
                                                @RequestBody StudentDTO studentDTO){
        studentService.updateStudent(id,studentDTO);

        String message = "Student is updated successfully";
        return new ResponseEntity<>(message,HttpStatus.OK); // 200
    }


    // Pageable ************************************************
    @GetMapping("/page") // http://localhost:8080/students/page?page=0&size=2&sort=name&direction=ASC + GET
    public ResponseEntity<Page<Student>> getAllWithPage (
            @RequestParam("page") int page, // kacinci sayfa gelecek
            @RequestParam("size") int size, // page basi kac nesne
            @RequestParam("sort") String prop, // siralamada kullanilacak degisken
            @RequestParam("direction") Sort.Direction direction // siralama yonu
    ){

        Pageable pageable = PageRequest.of(page,size, Sort.by(direction, prop));
        Page<Student> studentPage = studentService.getAllWithPage(pageable);
        return ResponseEntity.ok(studentPage);

    }


    // Get By LastName  ****************************************
    @GetMapping("/querylastname") // http://localhost:8080/students/querylastname?lastName=Fatma + GET
    public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastName") String lastName){

        List<Student> list = studentService.findStudent(lastName);
        return  ResponseEntity.ok(list);

    }


    // Get All Student by Grade ( JPQL ) Java Persistance Query Language ****************
    @GetMapping("/query/{grade}") // http://localhost:8080/students/query/70 + GET
    public ResponseEntity<List<Student>> getStudentEqualsGrade(@PathVariable("grade") Integer grade){

        List<Student> list = studentService.findAllEqualsGrade(grade);

        return ResponseEntity.ok(list);

    }


    // DB den direk DTO cekmeye calisalim *********************************************
    @GetMapping("/query/dto") // http://localhost:8080/students/query/dto?id=1 + GET
    public ResponseEntity<StudentDTO> getStudentDtoById(@RequestParam("id") Long id){
        StudentDTO studentDTO = studentService.findStudentDTOById(id);
        return ResponseEntity.ok(studentDTO);
    }


    @GetMapping("/welcome") // http://localhost:8080/students/welcome + GET
    public String welcome(HttpServletRequest request){

        logger.warn("-------------------  W E L C O M E {}",request.getServletPath());
        return "Welcome to Student Controller";
    }



}
