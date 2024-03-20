package pl.nawrockiit.catering3.info;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Info {
    @Id
    @Column(name = "info_id")
    private Integer infoId;
    @Column(name = "info_content", length = 8192)
    private String infoContent;
}
