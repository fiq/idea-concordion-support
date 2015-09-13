package com.gman.idea.plugin.concordion.lang;

import com.gman.idea.plugin.concordion.Concordion;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Collections.singletonList;

public class ConcordionLanguageInjector implements MultiHostInjector {

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
        if (!(context instanceof XmlAttributeValue)) {
            return;
        }
        XmlAttributeValue attributeValue = (XmlAttributeValue) context;
        XmlAttribute attribute = PsiTreeUtil.getParentOfType(attributeValue, XmlAttribute.class);
        if (!Concordion.NAMESPACE.equalsIgnoreCase(attribute.getNamespace())) {
            return;
        }

        registrar
                .startInjecting(ConcordionLanguage.INSTANCE)
                .addPlace(null, null, (PsiLanguageInjectionHost) attributeValue, new TextRange(1, attributeValue.getTextLength()-1))
                .doneInjecting();
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return singletonList(XmlAttributeValue.class);
    }
}