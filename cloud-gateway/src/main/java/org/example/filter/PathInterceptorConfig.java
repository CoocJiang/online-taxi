package org.example.filter;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "path-interceptor")
@Setter
@Getter
public class PathInterceptorConfig {
    private List<String> NoAuthPaths;
}
