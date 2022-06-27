package com.microsoft.azure.toolkit.intellij.appservice.task;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.EmptyAction;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;
import com.microsoft.azure.toolkit.ide.appservice.function.FunctionAppActionsContributor;
import com.microsoft.azure.toolkit.ide.guidance.ComponentContext;
import com.microsoft.azure.toolkit.ide.guidance.Course;
import com.microsoft.azure.toolkit.ide.guidance.Task;
import com.microsoft.azure.toolkit.intellij.common.action.IntellijAzureActionManager;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.appservice.entity.FunctionEntity;
import com.microsoft.azure.toolkit.lib.appservice.function.AzureFunctions;
import com.microsoft.azure.toolkit.lib.appservice.function.FunctionApp;
import com.microsoft.azure.toolkit.lib.common.action.Action;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class TriggerFunctionTask implements Task {
    public static final String FUNCTION_ID = "functionId";
    public static final String TRIGGER = "trigger";
    private final Project project;
    private final Course guidance;
    private final ComponentContext context;

    public TriggerFunctionTask(@Nonnull ComponentContext context) {
        this.context = context;
        this.project = context.getProject();
        this.guidance = context.getCourse();
    }

    @Override
    public void execute() throws Exception {
        final String functionId = (String) context.getParameter(FUNCTION_ID);
        final String trigger = (String) context.getParameter(TRIGGER);
        final FunctionApp functionApp = Azure.az(AzureFunctions.class).functionApp(functionId);
        final List<FunctionEntity> functionEntities = functionApp.listFunctions(true);
        final FunctionEntity target = functionEntities.stream().filter(entity -> StringUtils.equals(entity.getName(), trigger))
                .findFirst().orElse(functionEntities.get(0));
        final Action.Id<FunctionEntity> action = PlatformUtils.isIdeaUltimate() ?
                FunctionAppActionsContributor.TRIGGER_FUNCTION_WITH_HTTP_CLIENT : FunctionAppActionsContributor.TRIGGER_FUNCTION_IN_BROWSER;
        final DataContext context = dataId -> CommonDataKeys.PROJECT.getName().equals(dataId) ? project : null;
        final AnActionEvent event = AnActionEvent.createFromAnAction(new EmptyAction(), null, "azure.guidance.summary", context);
        IntellijAzureActionManager.getInstance().getAction(action).handle(target, event);
    }

    @Nonnull
    @Override
    public String getName() {
        return "task.function.trigger_function";
    }
}
