package udpm.hn.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import udpm.hn.server.entity.base.PrimaryEntity;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicUpdate
@Table(name = "head_subject_by_semester")
public class HeadSubjectBySemester extends PrimaryEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "id_subject")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "id_semester")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "id_facility")
    private Facility facility;

}
