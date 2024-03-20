package pl.nawrockiit.catering3.rules;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Integer> {
    Rule getRuleByRuleId(Integer ruleId);
}
