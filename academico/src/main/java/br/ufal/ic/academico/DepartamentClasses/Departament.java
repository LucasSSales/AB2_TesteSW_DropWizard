package br.ufal.ic.academico.DepartamentClasses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@RequiredArgsConstructor
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    private int gradSec; //ID DA SECRETARIA DE GRADUAÇÃO, SE -1 É PORQUE NÃO TEM
    private int posGradSec; //ID DA SECRETARIA DE POS GRADUAÇÃO, SE -1, É PORQUE NÃO TEM
    private int[] professors; //IDS DOS PROFEOSSORES

}
