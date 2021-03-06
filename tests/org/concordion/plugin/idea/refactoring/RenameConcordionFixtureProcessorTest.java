package org.concordion.plugin.idea.refactoring;

import com.intellij.refactoring.rename.RenameProcessor;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.concordion.plugin.idea.ConcordionPsiUtils.classIn;

public class RenameConcordionFixtureProcessorTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/navigation";
    }

    public void testRenameConcordionPairFromFixtureClass() {

        copyTestFixtureToConcordionProject("Spec.java");
        copySpecToConcordionProject("Spec.html");

        new RenameProcessor(getProject(), classIn(findFileInProject("/src/com/test/Spec.java")), "NewSpec", false, false).run();

        assertThat(findFileInProject("/src/com/test/Spec.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/Spec.html")).isNull();

        assertThat(findFileInProject("/src/com/test/NewSpec.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NewSpec.html")).isNotNull();
    }

    public void testRenameNonConcordionPairFromClass() {

        copyTestFixtureToConcordionProject("NoRunnerAnnotation.java");
        copySpecToConcordionProject("NoRunnerAnnotation.html");

        new RenameProcessor(getProject(), classIn(findFileInProject("/src/com/test/NoRunnerAnnotation.java")), "NewNoRunnerAnnotation", false, false).run();

        assertThat(findFileInProject("/src/com/test/NoRunnerAnnotation.java")).isNull();
        assertThat(findFileInProject("/resources/com/test/NoRunnerAnnotation.html")).isNotNull();

        assertThat(findFileInProject("/src/com/test/NewNoRunnerAnnotation.java")).isNotNull();
        assertThat(findFileInProject("/resources/com/test/NewNoRunnerAnnotation.html")).isNull();
    }
}