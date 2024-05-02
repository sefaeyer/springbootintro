package com.tpe.repository;

import com.tpe.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // yazmasak da olur, spring JpaRepo dan extend edildigi icin repo oldugunu anliyor
public interface StudentRepository extends JpaRepository<Student, Long> {

}
