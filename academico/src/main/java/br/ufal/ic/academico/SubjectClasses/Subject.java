package br.ufal.ic.academico.SubjectClasses;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;


@Entity
@Getter
@RequiredArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private Long prerequisites; //-1 se nao tiver
    private Long courseID;
    private Long departamentId;
    private int credits;
    private int minCredits;
    private Long professor;
    private boolean postgraduate;
    @Setter
    private ArrayList<Long> studentsId;



    public Subject (String name, Long prerequisites, Long courseID, Long departamentId, int credits, int minCredits, Long professor, boolean postgraduate){
        this.name = name;
        this.prerequisites = prerequisites;
        this.courseID = courseID;
        this.departamentId = departamentId;
        this.credits = credits;
        this.minCredits = minCredits;
        this.professor = professor;
        this.postgraduate = postgraduate;
    }

}
