package pl.nawrockiit.catering3.rules;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nawrockiit.catering3.info.Info;

import java.util.List;

@Service
@AllArgsConstructor
public class RuleServiceImpl implements RuleService{

    private final RuleRepository ruleRepository;

    @Override
    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }

    @Override
    public void save(Rule rule) {
        ruleRepository.save(rule);
    }

    @Override
    public Rule getRuleByRuleId(Integer ruleId) {
        return ruleRepository.getRuleByRuleId(ruleId);
    }

    @Override
    public void delete(Rule rule) {
        ruleRepository.delete(rule);
    }

}
