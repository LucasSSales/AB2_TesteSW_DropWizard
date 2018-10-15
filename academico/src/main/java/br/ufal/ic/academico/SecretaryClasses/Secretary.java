package br.ufal.ic.academico.SecretaryClasses;

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
public class Secretary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private boolean postgraduate;
    private Long departamentId;

    @Setter
    private ArrayList<Long> subjects; //ID DAS MATERIAS   

    public Secretary(String name, boolean postgraduate, Long departamentId) {
        this.name = name;
        this.postgraduate = postgraduate;
        this.departamentId = departamentId;
    }
}
