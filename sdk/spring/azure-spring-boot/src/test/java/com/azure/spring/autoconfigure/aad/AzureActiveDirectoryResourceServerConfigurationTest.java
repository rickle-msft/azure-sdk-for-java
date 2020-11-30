// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.autoconfigure.aad;

import static org.assertj.core.api.Assertions.assertThat;

import com.azure.spring.aad.resource.server.AzureActiveDirectoryResourceServerConfiguration;
import com.azure.spring.aad.resource.server.AzureActiveDirectoryResourceServerConfiguration.DefaultAzureOAuth2ResourceServerWebSecurityConfigurerAdapter;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;

public class AzureActiveDirectoryResourceServerConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
        .withPropertyValues("azure.activedirectory.user-group.allowed-groups=User");

    @Test
    public void testNotExistBearerTokenAuthenticationToken() {
        this.contextRunner
            .withUserConfiguration(AzureActiveDirectoryResourceServerConfiguration.class)
            .withClassLoader(new FilteredClassLoader(BearerTokenAuthenticationToken.class))
            .run(context -> {
                assertThat(context).doesNotHaveBean("jwtDecoderByJwkKeySetUri");
            });
    }

    @Test
    public void testCreateJwtDecoderByJwkKeySetUri() {
        this.contextRunner
            .withUserConfiguration(AzureActiveDirectoryResourceServerConfiguration.class)
            .run(context -> {
                final JwtDecoder jwtDecoder = context.getBean(JwtDecoder.class);
                assertThat(jwtDecoder).isNotNull();
                assertThat(jwtDecoder).isExactlyInstanceOf(NimbusJwtDecoder.class);
            });
    }

    @Test
    public void testNotAudienceDefaultValidator() {
        this.contextRunner
            .withUserConfiguration(AzureActiveDirectoryResourceServerConfiguration.class)
            .run(context -> {
                AzureActiveDirectoryResourceServerConfiguration bean = context
                    .getBean(AzureActiveDirectoryResourceServerConfiguration.class);
                List<OAuth2TokenValidator<Jwt>> defaultValidator = bean.createDefaultValidator();
                assertThat(defaultValidator).isNotNull();
                assertThat(defaultValidator).hasSize(2);
            });
    }

    @Test
    public void testExistAudienceDefaultValidator() {
        this.contextRunner
            .withUserConfiguration(AzureActiveDirectoryResourceServerConfiguration.class)
            .withPropertyValues("azure.activedirectory.app-id-uri=fake-app-id-uri")
            .run(context -> {
                AzureActiveDirectoryResourceServerConfiguration bean = context
                    .getBean(AzureActiveDirectoryResourceServerConfiguration.class);
                List<OAuth2TokenValidator<Jwt>> defaultValidator = bean.createDefaultValidator();
                assertThat(defaultValidator).isNotNull();
                assertThat(defaultValidator).hasSize(3);
            });
    }

    @Test
    public void testCreateWebSecurityConfigurerAdapter() {
        this.contextRunner
            .withUserConfiguration(AzureActiveDirectoryResourceServerConfiguration.class)
            .run(context -> {
                WebSecurityConfigurerAdapter webSecurityConfigurerAdapter = context
                    .getBean(DefaultAzureOAuth2ResourceServerWebSecurityConfigurerAdapter.class);
                assertThat(webSecurityConfigurerAdapter).isNotNull();
            });
    }

}
