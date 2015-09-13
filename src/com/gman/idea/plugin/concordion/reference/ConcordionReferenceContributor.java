package com.gman.idea.plugin.concordion.reference;

import com.gman.idea.plugin.concordion.OgnlChainResolver;
import com.gman.idea.plugin.concordion.lang.ConcordionLanguage;
import com.gman.idea.plugin.concordion.lang.psi.*;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ConcordionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(
                psiElement(ConcordionTypes.FIELD).withLanguage(ConcordionLanguage.INSTANCE),
                new MemberReferenceProvider()
        );

        registrar.registerReferenceProvider(
                psiElement(ConcordionTypes.METHOD).withLanguage(ConcordionLanguage.INSTANCE),
                new MemberReferenceProvider()
        );

        registrar.registerReferenceProvider(
                psiElement(ConcordionTypes.VARIABLE).withLanguage(ConcordionLanguage.INSTANCE),
                new VariableReferenceProvider()
        );
    }

    private static final class MemberReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            PsiFile containingFile = unpackSpecFromLanguageInjection(element.getContainingFile());
            PsiClass psiClass = correspondingJavaRunner(containingFile);

            if (containingFile == null || psiClass == null) {
                return PsiReference.EMPTY_ARRAY;
            }

            PsiMember psiMember = OgnlChainResolver.create(psiClass).resolveReference(element);

            if (psiMember == null) {
                return PsiReference.EMPTY_ARRAY;
            }

            return new PsiReference[] {
                    new ConcordionReference<>(element, psiMember)
            };
        }
    }

    private static final class VariableReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            return PsiReference.EMPTY_ARRAY;
        }
    }
}