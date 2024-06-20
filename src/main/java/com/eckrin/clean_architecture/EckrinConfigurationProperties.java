package com.eckrin.clean_architecture;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "eckrin")
public class EckrinConfigurationProperties {

  private long transferThreshold = Long.MAX_VALUE;

}
