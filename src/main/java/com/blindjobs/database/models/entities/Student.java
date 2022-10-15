package com.blindjobs.database.models.entities;

import com.blindjobs.utils.UtilsValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@Entity(name = "TB_STUDENT")
public class Student extends User {

    // User Skills and Qualifications
//    private List<Skills> skills;

    // User Professional History (Jobs, Ongs, etc)
//    private List<WorkExperience> workExperiences;

    // User sStudent Qualification
//    private List<SchoolQualification> schoolQualification;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (UtilsValidation.isNull(obj) || getClass() != obj.getClass() || UtilsValidation.isNull(super.getId()))
            return false;
        User that = (User) obj;
        return super.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return UtilsValidation.isNull(super.getId()) ? 0 : super.getId().hashCode();
    }
}
