package br.ufal.ic.academico.CourseClasses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Long departamentId;
    private Long secretaryId;

    @Setter
    private ArrayList<Long> subjectIds;

    @Setter
    private ArrayList<Long> students; //id dos estudantes

    public Course(String name, Long departamentId, Long secretaryId){
        this.name = name;
        this.departamentId = departamentId;
        this.secretaryId = secretaryId;
    }

}
