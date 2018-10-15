package br.ufal.ic.academico;

import br.ufal.ic.academico.StudentClasses.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
@Getter
@RequiredArgsConstructor
public class ProofOfEnrollment {
    
    private Student student;
    @Setter
    private ArrayList<String> subjects;

    public ProofOfEnrollment(Student student){
        this.student = student;
    }
    
}
