package de.maggiwuerze.xdccloader.config.configConditions;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class UserDataSourceCondition extends AnyNestedCondition {

	public UserDataSourceCondition() {
		super(ConfigurationPhase.PARSE_CONFIGURATION);
	}

	@ConditionalOnProperty(prefix = "xdcc-datasource", name = "datasource", value = "POSTGRESQL")
	static class Value1Condition {

	}

	@ConditionalOnProperty(prefix = "xdcc-datasource", name = "datasource", value = "MYSQL")
	static class Value2Condition {

	}
}