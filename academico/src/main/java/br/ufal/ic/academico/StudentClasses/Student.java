package br.ufal.ic.academico.StudentClasses;

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
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Setter
    private int score;
    @Setter
    private int[] studying; //ID DAS MATERIAS EM Q ESTA MATRICULADO
    @Setter
    private int[] approved; //ID DAS MATERIAS EM Q FOI APROVADO
    @Setter
    private int[] disapproved; //ID DAS MATERIAS EM Q FOI REPROVADO

    public Student(String name) {
        this.name = name;
    }



}
