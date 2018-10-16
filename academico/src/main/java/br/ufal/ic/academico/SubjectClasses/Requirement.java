package br.ufal.ic.academico.SubjectClasses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@RequiredArgsConstructor
public class Requirement {
    private Long subjId;

}
