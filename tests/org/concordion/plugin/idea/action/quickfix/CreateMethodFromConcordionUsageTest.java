package org.concordion.plugin.idea.action.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.concordion.plugin.idea.ConcordionCodeInsightFixtureTestCase;

public class CreateMethodFromConcordionUsageTest extends ConcordionCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/action";
    }

    public void testCreateMethodFromUsage() throws InterruptedException {

        copyTestFixtureToConcordionProject("CreateMethodFromUsage.java");
        VirtualFile htmlSpec = copySpecToConcordionProject("CreateMethodFromUsage.html");

        myFixture.configureFromExistingVirtualFile(htmlSpec);

        IntentionAction fix = myFixture.findSingleIntention("Create method from usage");
        assertTrue(fix.isAvailable(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile()));

        writeAction(() -> {
            fix.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile());
            return null;
        });

        myFixture.checkResultByFile("/src/com/test/CreateMethodFromUsage.java", "after/CreateMethodFromUsage.java", false);
    }
}