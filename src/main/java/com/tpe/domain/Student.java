package com.tpe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor //student icinde yazilan her variable icin const uretir
@NoArgsConstructor //parametresiz const

@Entity
public class Student { //student

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //bu degiskeni setlenemez hale getirir
    private Long id;

    //----------------------------------------------------------------

    @NotNull(message = "first name can not be null")
    @NotBlank(message = "first name can not be white space")
    @Size(min = 2, max = 25, message = "First name '${validatedValue}' must be between {min} and {max} chars")
    @Column(nullable = false, length = 25) // -->  En son asamada kontrol eder
    private String name;

    //----------------------------------------------------------------

    @Column(nullable = false, length = 25)
    private String lastName;

    //----------------------------------------------------------------

    @Column
    private Integer grade;

    //----------------------------------------------------------------

    @Column(nullable = false, length = 60, unique = true)
    @Email(message = "Provide valid email")
    private String email;

    //----------------------------------------------------------------

    private String phoneNumber;

    //----------------------------------------------------------------

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss", timezone = "Turkey") //database deki veri degismez
    @Setter(AccessLevel.NONE)
    private LocalDateTime createDate=LocalDateTime.now();


    @OneToMany(mappedBy = "student")
    private List<Book> books = new ArrayList<>();




    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
