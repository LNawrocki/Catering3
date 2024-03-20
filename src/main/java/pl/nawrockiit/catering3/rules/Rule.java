package pl.nawrockiit.catering3.rules;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Rule {
    @Id
    @Column(name = "rule_id")
    private Integer ruleId;
    @Column(name = "rule_content", length = 8192)
    private String ruleContent;
}
