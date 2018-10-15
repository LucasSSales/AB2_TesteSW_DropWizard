package br.ufal.ic.academico.DepartamentClasses;

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
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Setter
    private ArrayList<Long> coursesIds;
    @Setter
    private Long gradSec; //ID DA SECRETARIA DE GRADUAÇÃO, SE -1 É PORQUE NÃO TEM
    @Setter
    private Long posGradSec; //ID DA SECRETARIA DE POS GRADUAÇÃO, SE -1, É PORQUE NÃO TEM
    @Setter
    private ArrayList<Long> professors; //IDS DOS PROFEOSSORES

    public Departament (String name){
        this.name = name;
    }

}
