package udpm.hn.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "facility")
public class Facility extends PrimaryEntity implements Serializable {

    @Column(name = "code", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String code;

    @Column(name = "name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String name;

    @JoinColumn(name = "facility_identity_id")
    private Long facilityIdentityId;

}
