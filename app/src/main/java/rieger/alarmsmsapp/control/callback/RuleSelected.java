package rieger.alarmsmsapp.control.callback;

import java.io.Serializable;

import rieger.alarmsmsapp.model.rules.Rule;

/**
 * Created by sebastian on 08.03.17.
 */

public interface RuleSelected extends Serializable {

    void selectedRule(Rule rule);

}