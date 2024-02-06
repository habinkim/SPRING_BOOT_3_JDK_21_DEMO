package com.habin.demo.account;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class AccountBoundedContextDependencyRuleTest {

    private static final String ROOT_PACKAGE = "com.habin.demo.account";
    private static final String DOMAIN_PACKAGE = "domain";
    private static final String APPLICATION_PACKAGE = "application";
    private static final String PORT_PACKAGE = "application.port";
    private static final String SERVICE_PACKAGE = "application.service";
    private static final String ADAPTER_PACKAGE = "adapter";

    @Disabled
    @Test
    @DisplayName("Account Bounded Context satisfied Hexagonal Architecture.")
    void checkDependencyRule() {
        String importPackages = ROOT_PACKAGE + "..";
        JavaClasses classesToCheck = new ClassFileImporter().importPackages(importPackages);

        checkNoDependencyFromTo(DOMAIN_PACKAGE, APPLICATION_PACKAGE, classesToCheck);

        // TODO : 의존성 제거 필요
        checkNoDependencyFromTo(DOMAIN_PACKAGE, ADAPTER_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(APPLICATION_PACKAGE, ADAPTER_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(PORT_PACKAGE, SERVICE_PACKAGE, classesToCheck);

        checkNoDependencyFromTo(ADAPTER_PACKAGE, SERVICE_PACKAGE, classesToCheck);
    }

    private void checkNoDependencyFromTo(
            String fromPackage, String toPackage, JavaClasses classesToCheck) {
        noClasses()
                .that()
                .resideInAPackage(fullyQualified(fromPackage))
                .should()
                .dependOnClassesThat()
                .resideInAPackage(fullyQualified(toPackage))
                .check(classesToCheck);
    }

    private String fullyQualified(String packageName) {
        return ROOT_PACKAGE + '.' + packageName + "..";
    }

}
