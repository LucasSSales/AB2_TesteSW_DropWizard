package br.ufal.ic.academico.CourseClasses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int departamentId;
    private int secretaryId;
    private int[] subjectIds;

    @Setter
    private long[] students; //id dos estudantes

    public Course(String name, int departamentId, int secretaryId, int[] subjectIds){
        this.name = name;
        this.departamentId = departamentId;
        this.secretaryId = secretaryId;
        this.subjectIds = subjectIds;
    }

}
