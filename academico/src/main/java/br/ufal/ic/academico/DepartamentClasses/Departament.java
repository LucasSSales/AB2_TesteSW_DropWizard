package br.ufal.ic.academico.DepartamentClasses;

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
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int[] coursesIds;

    @Setter
    private int gradSec; //ID DA SECRETARIA DE GRADUAÇÃO, SE -1 É PORQUE NÃO TEM
    @Setter
    private int posGradSec; //ID DA SECRETARIA DE POS GRADUAÇÃO, SE -1, É PORQUE NÃO TEM
    @Setter
    private int[] professors; //IDS DOS PROFEOSSORES

    public Departament (String name, int[] coursesIds){
        this.name = name;
        this.coursesIds = coursesIds;
    }

}
