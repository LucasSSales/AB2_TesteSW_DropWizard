package br.ufal.ic.academico.ProfessorClasses;

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
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private int departamentId;

    @Setter
    private int[] subjects; //IDS DAS MATERIAS QUE ELE/A LESSIONA

    public Professor(String name, int departamentId){
        this.name = name;
        this.departamentId =departamentId;
    }

}
