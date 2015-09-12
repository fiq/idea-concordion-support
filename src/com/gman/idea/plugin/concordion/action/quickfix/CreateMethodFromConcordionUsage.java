package com.gman.idea.plugin.concordion.action.quickfix;

import com.gman.idea.plugin.concordion.lang.psi.ConcordionField;
import com.gman.idea.plugin.concordion.lang.psi.ConcordionMethod;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.PsiModifier.PUBLIC;
import static com.intellij.psi.search.ProjectScope.getAllScope;

public class CreateMethodFromConcordionUsage extends CreateFromConcordionUsage<ConcordionMethod> {

    public CreateMethodFromConcordionUsage(@Nullable PsiClass javaRunner, @NotNull ConcordionMethod source) {
        super(javaRunner, source, "Create method from usage");
    }

    @Override
    protected PsiMember createdMember(Project project, PsiElementFactory factory) {
        PsiType defaultType = PsiType.getTypeByName("java.lang.Object", project, getAllScope(project));

        PsiMethod createdMethod = factory.createMethod(source.getMethodName(), defaultType);
        createdMethod.getModifierList().setModifierProperty(PUBLIC, true);

        for (int i = 0; i < source.getMethodParametersCount(); i++) {
            createdMethod.getParameterList().add(factory.createParameter("param" + i, defaultType));
        }
        createdMethod.getBody().add(factory.createStatementFromText("return null;", null));

        return (PsiMethod) javaRunner.add(createdMethod);
    }

}
