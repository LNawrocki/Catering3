package pl.nawrockiit.catering3.rules;

import pl.nawrockiit.catering3.info.Info;

import java.util.List;

public interface RuleService {
    List<Rule> findAll();
    void save(Rule rule);
    Rule getRuleByRuleId(Integer ruleId);
    void delete(Rule rule);
}
