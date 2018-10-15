package br.ufal.ic.academico.SubjectClasses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
@Getter
@RequiredArgsConstructor
public class SubjectInfos {

    private Subject subject;
    private ArrayList<String> studentNames;
    private String profName;

    public SubjectInfos (Subject subject, ArrayList<String> studentNames, String profName){
        this.subject = subject;
        this.studentNames = studentNames;
        this.profName = profName;
    }

}
