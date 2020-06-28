/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.maven;

import org.openrewrite.RefactorVisitorSupport;
import org.openrewrite.Tree;
import org.openrewrite.maven.tree.Maven;
import org.openrewrite.xml.XmlRefactorVisitor;
import org.openrewrite.xml.tree.Xml;

public class MavenRefactorVisitor extends MavenSourceVisitor<Maven> implements RefactorVisitorSupport {
    XmlRefactorVisitor xmlRefactorVisitor = new XmlRefactorVisitor() {
    };

    @Override
    public Maven defaultTo(Tree t) {
        return (Maven) t;
    }

    @Override
    public Maven visitPom(Maven.Pom pom) {
        Maven.Pom p = refactor(pom, super::visitPom);
        p = p.withDependencyManagement(refactor(pom.getDependencyManagement()));
        p = p.withDependencies(refactor(pom.getDependencies()));
        p = p.withProperties(refactor(pom.getProperties()));
        return p;
    }

    @Override
    public Maven visitDependencyManagement(Maven.DependencyManagement dependencyManagement) {
        Maven.DependencyManagement d = refactor(dependencyManagement, super::visitDependencyManagement);
        d = d.withDependencies(refactor(dependencyManagement.getDependencies()));
        return d;
    }

    @Override
    public Maven visitDependency(Maven.Dependency dependency) {
        Maven.Dependency d = refactor(dependency, super::visitDependency);
        Xml.Tag t = (Xml.Tag) xmlRefactorVisitor.visitTag(d.getTag());
        if(t != d.getTag()) {
            return new Maven.Dependency(d.isManaged(), d.getModel(), t);
        }
        return d;
    }

    @Override
    public Maven visitProperty(Maven.Property property) {
        Maven.Property p = refactor(property, super::visitProperty);
        Xml.Tag t = (Xml.Tag) xmlRefactorVisitor.visitTag(p.getTag());
        if(t != p.getTag()) {
            return new Maven.Property(t);
        }
        return p;
    }
}
