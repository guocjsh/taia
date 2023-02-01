package io.github.guocjsh.i18n.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {TaiaI18nProperties.class})
@ConfigurationProperties(prefix = "taia.i18n")
public class TaiaI18nProperties {

    private String basename;

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }
}