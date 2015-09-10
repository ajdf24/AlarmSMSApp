package rieger.alarmsmsapp.model;

/**
 * A enum class for difference the different rule types.
 * @author sebastian
 *
 */
public enum RuleType{
	
	/**
	 * Type for a E-Mail rule.
	 */
	EMAIL_RULE(){
		
	},
	
	/**
	 * Type for a SMS rule.
	 */
	SMS_RULE(){
		
	};
	
	/**
	 * Default constructor for creating a rule type.
	 */
	private RuleType() {
	}
	
	
}
